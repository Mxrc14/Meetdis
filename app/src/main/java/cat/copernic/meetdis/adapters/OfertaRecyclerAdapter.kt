package cat.copernic.meetdis.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.RecyclerView

import cat.copernic.meetdis.models.Oferta
import cat.copernic.meetdis.databinding.ItemOfertaListBinding
import coil.api.load
import com.google.firebase.storage.FirebaseStorage
import kotlin.collections.ArrayList


class OfertaRecyclerAdapter : RecyclerView.Adapter<OfertaRecyclerAdapter.ViewHolder>() {

    var ofertes: ArrayList<Oferta> = ArrayList()
    lateinit var context: Context

    fun OfertaRecyclerAdapter(ofertasList: ArrayList<Oferta>, contxt: Context) {
        this.ofertes = ofertasList
        this.context = contxt
    }

    //és l'encarregat de retornar el ViewHolder ja configurat
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return ViewHolder(
            ItemOfertaListBinding.inflate(layoutInflater, parent, false)
        )
    }

    //Aquest mètode s'encarrega de passar els objectes, un a un al ViewHolder personalitzat
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(ofertes[position]) {
                binding.txtTitol.text = this.titolOferta
                binding.txtDesc.text = this.descripcioOferta





                 //Monstrar la imatge des de Storage de Firebase
                val storageRef = FirebaseStorage.getInstance().reference

                val imageRef = storageRef.child("ofertes/$imatge")
                imageRef.downloadUrl.addOnSuccessListener { url ->
                    binding.imgOferta.load(url)
                   // Log.i("proba_id", "$imageMembre")
                }.addOnFailureListener {
                    //mostrar error
                }
            }
        }

        val item = ofertes[position]
        holder.bind(item)
    }


    override fun getItemCount(): Int {
        return ofertes.size
    }


    class ViewHolder(val binding: ItemOfertaListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Oferta) {

        }
    }
}





