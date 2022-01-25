package cat.copernic.meetdis

import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat.Token.fromBundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.meetdis.adapters.NotificacioRecyclerAdapter
import cat.copernic.meetdis.adapters.NotificacioUsuariRecyclerAdapter
import cat.copernic.meetdis.databinding.FragmentNotificacioUsuariBinding
import cat.copernic.meetdis.models.Oferta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList
import java.util.HashMap


class NotificacioUsuari : Fragment() {

    private val myAdapter: NotificacioUsuariRecyclerAdapter = NotificacioUsuariRecyclerAdapter()

    private val db = FirebaseFirestore.getInstance()

    private val user = FirebaseAuth.getInstance().currentUser

    private val uid = user?.email

    private var dniS: String = uid.toString().substring(0, uid.toString().length - 11).uppercase()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentNotificacioUsuariBinding>(
            inflater,
            R.layout.fragment_notificacio_usuari, container, false
        )

        binding.rvNotificacio.setHasFixedSize(true)

        binding.rvNotificacio.layoutManager = LinearLayoutManager(requireContext())


        var ofertas: ArrayList<Oferta> = arrayListOf();

        var lista = arrayListOf<String>()

        val args = NotificacioUsuariArgs.fromBundle(requireArguments())

        val dniUsuari: String = args.dniUsuari

        lista.clear()
        ofertas.clear()
        db.collection("userinscrit").document(dniUsuari).get().addOnSuccessListener { inscripcio ->

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
                            context?.let { myAdapter.NotificacioUsuariRecyclerAdapter(ofertas, it) }
                            binding.rvNotificacio.adapter = myAdapter

                        }


                }

            }else{

                userdetailoferta["ofertes"] = lista
                db.collection("userinscrit").document(dniUsuari).set(userdetailoferta)
            }

        }
        return binding.root

    }
}