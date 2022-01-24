package cat.copernic.meetdis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.meetdis.adapters.MembreRecyclerAdapter
import cat.copernic.meetdis.adapters.ValidacioRecyclerAdapter
import cat.copernic.meetdis.databinding.FragmentMembresBinding
import cat.copernic.meetdis.databinding.FragmentValidacioUsuarisBinding
import cat.copernic.meetdis.models.Busca
import cat.copernic.meetdis.models.Membre
import com.google.firebase.firestore.FirebaseFirestore

class ValidacioUsuaris : Fragment() {

    private var myAdapter: ValidacioRecyclerAdapter = ValidacioRecyclerAdapter()

    private val db = FirebaseFirestore.getInstance()


    private var membres: ArrayList<Membre> = arrayListOf();


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = DataBindingUtil.inflate<FragmentValidacioUsuarisBinding>(
            inflater,
            R.layout.fragment_validacio_usuaris, container, false
        )


        binding.rvValidacion.setHasFixedSize(true)

        binding.rvValidacion.layoutManager = LinearLayoutManager(requireContext())



        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                membres.clear()
                for (document in documents) {
                    membres.add(
                        Membre(
                            document.get("cognoms").toString(),
                            document.get("contrasenya").toString(),
                            document.get("correu").toString(),
                            document.get("nom").toString(),
                            document.get("tipus d´usuari").toString(),
                            document.id
                        )
                    )
                }
                context?.let { myAdapter.ValidacioRecyclerAdapter(membres, it) }
                binding.rvValidacion.adapter = myAdapter

            }


            return binding.root
                    }


            }






