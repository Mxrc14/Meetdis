package cat.copernic.meetdis.adapters

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.meetdis.R
import cat.copernic.meetdis.databinding.ItemNotificacioListBinding
import cat.copernic.meetdis.models.Oferta
import coil.api.load
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class NotificacioRecyclerAdapter : RecyclerView.Adapter<NotificacioRecyclerAdapter.ViewHolder>() {

    var notifica: ArrayList<Oferta> = ArrayList()
    lateinit var context: Context
    lateinit var id: String

    private val db = FirebaseFirestore.getInstance()

    fun NotificacioRecyclerAdapter(notificaList: ArrayList<Oferta>, contxt: Context) {
        this.notifica = notificaList
        this.context = contxt
    }

    //és l'encarregat de retornar el ViewHolder ja configurat

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            ItemNotificacioListBinding.inflate(layoutInflater, parent, false)
        )
    }

    //Aquest mètode s'encarrega de passar els objectes, un a un al ViewHolder personalitzat
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(notifica[position]) {
                binding.txtNom.text = this.titolOferta

                //TODO Monstrar la imatge des de Storage de Firebase
                val storageRef = FirebaseStorage.getInstance().reference

                Log.i("proba_id", "$imatgeOferta")

                Log.i("idFoto", "${this.imatgeOferta}")


                val imageRef = storageRef.child("ofertes/$imatgeOferta")
                imageRef.downloadUrl.addOnSuccessListener { url ->
                    binding.imgNotificacio.load(url)
                    Log.i("proba_id", "$imatgeOferta")
                }.addOnFailureListener {
                    //mostrar error
                }
            }
        }

        val item = notifica[position]

        holder.bind(item)


        holder.itemView.setOnClickListener {

            val bundle = Bundle()


            bundle.putSerializable("ofertaTitol", notifica[position].titolOferta)
            bundle.putSerializable("ofertaDesc", notifica[position].descripcioOferta)
            bundle.putSerializable("ofertaData", notifica[position].dataOferta)
            bundle.putSerializable("ofertaDNI", notifica[position].dniOferta)
            bundle.putSerializable("ofertaImg", notifica[position].imatgeOferta)
            bundle.putSerializable("ofertaLat", notifica[position].latitutOferta)
            bundle.putSerializable("ofertaLon", notifica[position].longitudOferta)
            bundle.putSerializable("ofertaTipus", notifica[position].tipusOferta)



            holder.itemView.findNavController().navigate(
                R.id.action_notificacioFragment_to_ofertaFragment, bundle
            )

//           holder.itemView.findNavController()
//                .navigate(IniciDirections.actionIniciFragmentToFragmentContingutOferta())
        }



    }


    override fun getItemCount(): Int {
        return notifica.size
    }


    class ViewHolder(val binding: ItemNotificacioListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Oferta) {


        }
    }
}





