package cat.copernic.meetdis.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.meetdis.IniciDirections
import cat.copernic.meetdis.databinding.ItemBuscaListBinding
import cat.copernic.meetdis.models.Busca
import coil.api.load
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class BuscaRecyclerAdapter : RecyclerView.Adapter<BuscaRecyclerAdapter.ViewHolder>() {
    private val db = FirebaseFirestore.getInstance()
    var busca: ArrayList<Busca> = ArrayList()
    lateinit var context: Context

    fun BuscaRecyclerAdapter(buscaList: ArrayList<Busca>, contxt: Context) {
        this.busca = buscaList
        this.context = contxt
    }

    //és l'encarregat de retornar el ViewHolder ja configurat

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        return ViewHolder(
            ItemBuscaListBinding.inflate(layoutInflater, parent, false)
        )

    }

    //Aquest mètode s'encarrega de passar els objectes, un a un al ViewHolder personalitzat
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(
            busca[position]
        )

        with(holder) {
            with(busca[position]) {
                binding.txtTitol.text = this.titolBusca

                //Monstrar la imatge des de Storage de Firebase
                val storageRef = FirebaseStorage.getInstance().reference
                Log.i("proba_id1", "$imatge")
                val imageRef = storageRef.child("imatges/$imatge")
                imageRef.downloadUrl.addOnSuccessListener { url ->
                    binding.imgBusca.load(url)
                     Log.i("proba_id1", "$imatge")
                }.addOnFailureListener {
                    //mostrar error
                }
            }
        }

        val item = busca[position]
        holder.bind(item)


        holder.itemView.setOnClickListener {
            val bundle = Bundle()

            bundle.putSerializable("buscaTitol", busca[position].titolBusca)

//            holder.itemView.findNavController().navigate(
//                R.id.action_buscarFragment_to_filtroFragment, bundle
//            )

            holder.itemView.findNavController()
                .navigate(IniciDirections.actionIniciFragmentToOfertaFragment())
        }
    }


    override fun getItemCount(): Int {
        return busca.size
    }


    class ViewHolder(val binding: ItemBuscaListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Busca) {

        }
    }
}





