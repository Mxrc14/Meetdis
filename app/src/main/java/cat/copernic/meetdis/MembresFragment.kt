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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Membres : Fragment() {



    private var myAdapter: MembreRecyclerAdapter = MembreRecyclerAdapter()

    private val db = FirebaseFirestore.getInstance()


    private var membres: ArrayList<Membre> = arrayListOf();

    private lateinit var idOferta: String

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

        lista.clear()
        membres.clear()
        db.collection("inscrit").document(idOferta).get().addOnSuccessListener { inscripcio ->

            lista = inscripcio.get("users") as ArrayList<String>


            for (posicion in lista.indices) {

                var dniUserActual = lista[posicion]


                db.collection("users").get()
                    .addOnSuccessListener { usuario ->
                        for (usuari in usuario) {
                            Log.i("membreOferta", usuari.id)
                            Log.i("membreOferta", usuari.get("cognoms").toString())
                            Log.i("membreOferta", usuari.get("contrasenya").toString())
                            Log.i("membreOferta", usuari.get("correu").toString())
                            Log.i("membreOferta", usuari.get("nom").toString())
                            Log.i("membreOferta", usuari.get("tipus d´usuari").toString())

                            if (usuari.id == dniUserActual) {

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
                        context?.let { myAdapter.MembreRecyclerAdapter(membres, it) }
                        binding.rvMembres.adapter = myAdapter

                    }


            }


        }


        return binding.root
    }
}


