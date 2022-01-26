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
    private var lista = arrayListOf<String>()

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

        holder.bind(item)

        var dnifinal = item.dniMembre

        dnifinal = dnifinal.toString().substring(0, dnifinal.toString().length - 11).uppercase()

        holder.itemView.setOnClickListener {

            val builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.eliminar_usuario)
            builder.setMessage(R.string.selecciona)
            builder.setPositiveButton(R.string.eliminar) { dialog, id ->

                db.collection("userspendents").document(dnifinal).delete()
                db.collection("users").document(dnifinal).delete()
                db.collection("userinscrit").document(dnifinal).delete()
                db.collection("ofertes").get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        if (document.get("dni").toString() == dnifinal) {
                            db.collection("ofertes").document(document.id).delete()
                            db.collection("inscrit").document(document.id).delete()

                            lista.clear()
                            db.collection("userinscrit").get().addOnSuccessListener { userActu ->

                                if (userActu != null) {

                                    for (user in userActu) {
                                        Log.i("ContingutOfertaUser", user.id)
                                        if (user.get("ofertes") != null) {

                                            lista = user.get("ofertes") as java.util.ArrayList<String>
                                            val userdetail = HashMap<String, Any>()
                                            for (posicion in lista.indices) {
                                                Log.i("ContingutO", lista[posicion])
                                                if (lista[posicion] == document.id) {

                                                    lista.removeAt(posicion)

                                                    userdetail["ofertes"] = lista

                                                    db.collection("userinscrit").document(user.id).delete()
                                                    db.collection("userinscrit").document(user.id)
                                                        .set(userdetail)
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                            lista.clear()
                            db.collection("inscrit").get().addOnSuccessListener { ofertaActu ->

                                if (ofertaActu != null) {

                                    for (user in ofertaActu) {
                                        Log.i("ContingutOfertaUser", user.id)
                                        if (user.get("users") != null) {

                                            lista = user.get("users") as java.util.ArrayList<String>
                                            val userdetail = HashMap<String, Any>()
                                            for (posicion in lista.indices) {
                                                Log.i("ContingutO", lista[posicion])
                                                if (lista[posicion] == dnifinal) {

                                                    lista.removeAt(posicion)

                                                    userdetail["users"] = lista

                                                    db.collection("inscrit").document(user.id).delete()
                                                    db.collection("inscrit").document(user.id)
                                                        .set(userdetail)
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

                holder.itemView.findNavController().navigate(
                    R.id.action_validacioUsuarisFragment_self
                )

            }
            builder.setNegativeButton(R.string.conservar) { dialog, which ->

                db.collection("userspendents").document(dnifinal).delete()

                holder.itemView.findNavController().navigate(
                    R.id.action_validacioUsuarisFragment_self
                )
            }
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
