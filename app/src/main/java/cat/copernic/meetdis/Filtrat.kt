package cat.copernic.meetdis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.meetdis.adapters.OfertaFiltrarRecyclerAdapter
import cat.copernic.meetdis.adapters.OfertaRecyclerAdapter
import cat.copernic.meetdis.databinding.FragmentFiltratBinding
import cat.copernic.meetdis.databinding.FragmentIniciBinding
import cat.copernic.meetdis.models.Oferta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Filtrat : Fragment() {

    private var myAdapter: OfertaFiltrarRecyclerAdapter = OfertaFiltrarRecyclerAdapter()

    private val db = FirebaseFirestore.getInstance()

    private var ofertes: ArrayList<Oferta> = arrayListOf()

    private lateinit var binding: FragmentFiltratBinding

    var buscarTipus: String? = null


    val user = FirebaseAuth.getInstance().currentUser

    val uid = user?.email

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<FragmentFiltratBinding>(
            inflater,
            R.layout.fragment_filtrat, container, false
        )

        buscarTipus = arguments?.getSerializable("buscaTitol") as String?


Log.i("Filtratxd", "$buscarTipus")
        var dni: String = uid.toString().substring(0, uid.toString().length - 11).uppercase()

        binding.rvBuscar.setHasFixedSize(true)

        binding.rvBuscar.layoutManager = LinearLayoutManager(requireContext())


        db.collection("ofertes")
            .get()
            .addOnSuccessListener { documents ->
                ofertes.clear()
                for (document in documents) {






                    if (document.get("tipus").toString() == buscarTipus) {
                        ofertes.add(
                            Oferta(
                                document.get("titol").toString(),
                                document.get("descripcio").toString(),
                                document.get("dni").toString(),
                                document.get("data").toString(),
                                document.get("tipus").toString(),
                                document.id,
                                document.get("latitut") as Double,
                                document.get("longitud") as Double
                            )
                        )

                    }

                }

                context?.let { myAdapter.OfertaFiltrarRecyclerAdapter(ofertes, it) }
                binding.rvBuscar.adapter = myAdapter

            }


        return binding.root
    }



}