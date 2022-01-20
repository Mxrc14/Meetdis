package cat.copernic.meetdis.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView
import cat.copernic.meetdis.databinding.ItemMembreListBinding
import cat.copernic.meetdis.models.Membre
import com.google.firebase.storage.FirebaseStorage
import kotlin.collections.ArrayList
import coil.api.load
import com.google.firebase.firestore.FirebaseFirestore


class MembreRecyclerAdapter : RecyclerView.Adapter<MembreRecyclerAdapter.ViewHolder>() {

    var membres: ArrayList<Membre> = ArrayList()
    lateinit var context: Context
    lateinit var id: String

    private val db = FirebaseFirestore.getInstance()

    fun MembreRecyclerAdapter(membresList: ArrayList<Membre>, contxt: Context) {
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
    }


    override fun getItemCount(): Int {
        return membres.size
    }


    class ViewHolder(val binding: ItemMembreListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Membre) {

        }
    }
}





