package cat.copernic.meetdis.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.meetdis.databinding.ItemNotificacioListBinding
import cat.copernic.meetdis.models.Notifica
import coil.api.load
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class NotificacioRecyclerAdapter : RecyclerView.Adapter<NotificacioRecyclerAdapter.ViewHolder>() {

    var notifica: ArrayList<Notifica> = ArrayList()
    lateinit var context: Context
    lateinit var id: String

    private val db = FirebaseFirestore.getInstance()

    fun NotificacioRecyclerAdapter(notificaList: ArrayList<Notifica>, contxt: Context) {
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
                binding.txtNom.text = this.nomNotifica

                //TODO Monstrar la imatge des de Storage de Firebase
                val storageRef = FirebaseStorage.getInstance().reference

                Log.i("proba_id", "$imageNotifica")

                Log.i("idFoto", "${this.imageNotifica}")


                val imageRef = storageRef.child("ofertes/$imageNotifica")
                imageRef.downloadUrl.addOnSuccessListener { url ->
                    binding.imgNotificacio.load(url)
                    Log.i("proba_id", "$imageNotifica")
                }.addOnFailureListener {
                    //mostrar error
                }
            }
        }

        val item = notifica[position]

        holder.bind(item)
    }


    override fun getItemCount(): Int {
        return notifica.size
    }


    class ViewHolder(val binding: ItemNotificacioListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Notifica) {

        }
    }
}





