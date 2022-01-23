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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap


class Notificacio : Fragment() {

    private val myAdapter: NotificacioRecyclerAdapter = NotificacioRecyclerAdapter()

    private val db = FirebaseFirestore.getInstance()






    private val user = FirebaseAuth.getInstance().currentUser

    private val uid = user?.email

    private var dniS: String = uid.toString().substring(0, uid.toString().length - 11).uppercase()

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

        var ofertas: ArrayList<Oferta> = arrayListOf();

        var lista = arrayListOf<String>()

        lista.clear()
        ofertas.clear()
        db.collection("userinscrit").document(dniS).get().addOnSuccessListener { inscripcio ->

            val userdetailoferta = HashMap<String, Any>()

            if (inscripcio.get("ofertes") != null) {


            lista = inscripcio.get("ofertes") as ArrayList<String>


            for (posicion in lista.indices) {

                var ofertaActual = lista[posicion]


                db.collection("ofertes").get()
                    .addOnSuccessListener { ofertes ->

                        for (oferta in ofertes) {
                            if (oferta.id == ofertaActual) {

                                ofertas.add(
                                    Oferta(
                                        oferta.get("titol").toString(),
                                        oferta.get("descripcio").toString(),
                                        oferta.get("dni").toString(),
                                        oferta.get("data").toString(),
                                        oferta.get("tipus").toString(),
                                        oferta.id,
                                        oferta.get("latitut") as Double,
                                        oferta.get("longitud") as Double
                                    )
                                )

                            }

                        }
                        context?.let { myAdapter.NotificacioRecyclerAdapter(ofertas, it) }
                        binding.rvNotificacio.adapter = myAdapter

                    }


            }


        }else{

                userdetailoferta["ofertes"] = lista
                db.collection("userinscrit").document(dniS).set(userdetailoferta)
            }

        }
        return binding.root

    }
}