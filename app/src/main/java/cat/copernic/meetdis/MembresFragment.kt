package cat.copernic.meetdis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.meetdis.adapters.MembreRecyclerAdapter
import cat.copernic.meetdis.databinding.FragmentMembresBinding
import cat.copernic.meetdis.models.Membre
import com.google.firebase.firestore.FirebaseFirestore


class Membres : Fragment() {


    private var myAdapter: MembreRecyclerAdapter = MembreRecyclerAdapter()

    private val db = FirebaseFirestore.getInstance()

    private var membres: ArrayList<Membre> = arrayListOf();

    private lateinit var idOferta: String

    var dniUserActual: String = ""

    var lista = arrayListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val args = MembresArgs.fromBundle(requireArguments())

        idOferta = args.idOferta

        Log.i("MembresF", idOferta)

        val binding = DataBindingUtil.inflate<FragmentMembresBinding>(
            inflater,
            R.layout.fragment_membres, container, false
        )





        binding.rvMembres.setHasFixedSize(true)

        binding.rvMembres.layoutManager = LinearLayoutManager(requireContext())


//db.collection("inscrit").get(oferta)




        db.collection("inscrit").document(idOferta).get().addOnSuccessListener { inscripcio ->

            lista = inscripcio.get("users") as ArrayList<String>
            membres.clear()
            for (posicion in lista.indices) {
                Log.i("VALORA", lista[posicion])

                dniUserActual = lista[posicion]

                db.collection("users").document(dniUserActual).get()
                    .addOnSuccessListener { usuari ->
                        Log.i("membreOferta","${usuari.id}")
                        Log.i("membreOferta",usuari.get("cognoms").toString())
                        Log.i("membreOferta",usuari.get("contrasenya").toString())
                        Log.i("membreOferta",usuari.get("correu").toString())
                        Log.i("membreOferta",usuari.get("nom").toString())
                        Log.i("membreOferta",usuari.get("tipus d´usuari").toString())
                        membres.add(
                            Membre(
                                usuari.get("cognoms").toString(),
                                usuari.get("contrasenya").toString(),
                                usuari.get("correu").toString(),
                                usuari.get("nom").toString(),
                                usuari.get("tipus d´usuari").toString(),
                                usuari.id
                            )
                        )
                    }


            }
Log.i("VALOR", membres.toString())
            context?.let { myAdapter.MembreRecyclerAdapter(membres, it) }
            binding.rvMembres.adapter = myAdapter


        }


//        db.collection("users")
//            .get()
//            .addOnSuccessListener { documents ->
//                membres.clear()
//                for (document in documents) {
//                    Log.i("proba_id", document.id)
//                    membres.add(
//                        Membre(
//                            document.get("cognoms").toString(),
//                            document.get("contrasenya").toString(),
//                            document.get("correu").toString(),
//                            document.get("nom").toString(),
//                            document.get("tipus d'usuari").toString(),
//                            document.id
//
//                        )
//                    )
//                }
//
//                context?.let { myAdapter.MembreRecyclerAdapter(membres, it) }
//                binding.rvMembres.adapter = myAdapter
//
//            }


        return binding.root
    }
}
