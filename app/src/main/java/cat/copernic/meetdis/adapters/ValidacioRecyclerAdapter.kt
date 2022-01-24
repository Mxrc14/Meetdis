package cat.copernic.meetdis.adapters


import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController

import androidx.recyclerview.widget.RecyclerView
import cat.copernic.meetdis.ContingutOfertaDirections
import cat.copernic.meetdis.R
import cat.copernic.meetdis.databinding.ItemMembreListBinding
import cat.copernic.meetdis.models.Membre
import com.google.firebase.storage.FirebaseStorage
import kotlin.collections.ArrayList
import coil.api.load
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap

class ValidacioRecyclerAdapter: RecyclerView.Adapter<ValidacioRecyclerAdapter.ViewHolder>(){


    var membres: ArrayList<Membre> = ArrayList()
    lateinit var context: Context
    lateinit var id: String

    private val db = FirebaseFirestore.getInstance()

    fun ValidacioRecyclerAdapter(membresList: ArrayList<Membre>, contxt: Context) {
        this.membres = membresList
        this.context = contxt
    }

    //és l'encarregat de retornar el ViewHolder ja configurat

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemMembreListBinding.inflate(layoutInflater, parent, false)
        )
    }

    //Aquest mètode s'encarrega de passar els objectes, un a un al ViewHolder personalitzat
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(membres[position]) {
                binding.txtNom.text = this.nomMembre

                var imatge = this.imageMembre
                //Monstrar la imatge des de Storage de Firebase
                val storageRef = FirebaseStorage.getInstance().reference

                this.imageMembre?.let { Log.i("cash", it) }

                val imageRef = storageRef.child("users/$imageMembre")
                imageRef.downloadUrl.addOnSuccessListener { url ->
                    binding.imgMembre.load(url)

                }.addOnFailureListener {
                    Log.i("cash", "Falla")
                }
            }
        }

        val item = membres[position]

        Log.i("cash1", "${item.dni}")

        holder.bind(item)

        var dnifinal = item.dniMembre

        dnifinal = dnifinal.toString().substring(0, dnifinal.toString().length - 11).uppercase()

        holder.itemView.setOnClickListener {


            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.eliminar_usuario)
            builder.setMessage(R.string.selecciona)
            builder.setPositiveButton(R.string.eliminar) { dialog, id ->

                Log.i("cash2", "${item.dniMembre}")

                db.collection("users").document(dnifinal).delete()
                db.collection("userinscrit").document(dnifinal).delete()
                db.collection("ofertes").get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.get("dni") == dnifinal) {
                            db.collection("ofertes").document().delete()
                        }
                    }

                }
                db.collection("inscrit").get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.get("users") == dnifinal) {
                            db.collection("ofertes").document().delete()
                        }
                    }

                }
            }

            builder.setNegativeButton(R.string.conservar, { dialog, which ->  })
            builder.show()

        }






//           holder.itemView.findNavController()
//                .navigate(IniciDirections.actionIniciFragmentToFragmentContingutOferta())
        }





    override fun getItemCount(): Int {
        return membres.size
    }


    class ViewHolder(val binding: ItemMembreListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Membre) {

        }
    }
}
