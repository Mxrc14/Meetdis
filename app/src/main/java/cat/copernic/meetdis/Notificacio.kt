package cat.copernic.meetdis
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.meetdis.adapters.NotificacioRecyclerAdapter
import cat.copernic.meetdis.databinding.FragmentNotificacioBinding
import cat.copernic.meetdis.models.Oferta
import com.google.firebase.firestore.FirebaseFirestore


class Notificacio: Fragment() {

    private val myAdapter: NotificacioRecyclerAdapter = NotificacioRecyclerAdapter()

    private val db = FirebaseFirestore.getInstance()

    private var notifica: ArrayList<Oferta> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentNotificacioBinding>(
            inflater,
            R.layout.fragment_notificacio, container, false
        )

        binding.rvNotificacio.setHasFixedSize(true)

        binding.rvNotificacio.layoutManager = LinearLayoutManager(requireContext())



        db.collection("ofertes")
            .get()
            .addOnSuccessListener { documents ->
                notifica.clear()
                for (document in documents) {
                    notifica.add(
                        Oferta(
                            document.get("titol").toString(),
                            document.get("descripcio").toString(),
                            document.get("dni").toString(),
                            document.get("data").toString(),
                            document.get("tipus").toString(),
                            document . id,
                            document.get("latitut") as Double,
                            document.get("longitud") as Double
                        )
                    )
                }
                context?.let { myAdapter.NotificacioRecyclerAdapter(notifica, it) }
                binding.rvNotificacio.adapter = myAdapter

            }
        return binding.root

    }
}