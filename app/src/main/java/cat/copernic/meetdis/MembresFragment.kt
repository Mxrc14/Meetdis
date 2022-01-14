package cat.copernic.meetdis

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.meetdis.adapters.MembreRecyclerAdapter
import cat.copernic.meetdis.databinding.FragmentMembresBinding
import cat.copernic.meetdis.models.Membre
import com.google.firebase.firestore.FirebaseFirestore


class Membres : Fragment() {


    private var myAdapter: MembreRecyclerAdapter = MembreRecyclerAdapter()

    private val db = FirebaseFirestore.getInstance()

    private var membres: ArrayList<Membre> = arrayListOf();


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentMembresBinding>(
            inflater,
            R.layout.fragment_membres, container, false
        )

        binding.rvMembres.setHasFixedSize(true)

        binding.rvMembres.layoutManager = LinearLayoutManager(requireContext())




        db.collection("users")
            .get()
            .addOnSuccessListener { documents ->
                membres.clear()
                for (document in documents) {
                    Log.i("proba_id", document.id)
                    membres.add(
                        Membre(
                            document.get("cognoms").toString(),
                            document.get("contrasenya").toString(),
                            document.get("correu").toString(),
                            document.get("nom").toString(),
                            document.get("tipus d'usuari").toString(),
                            document.id

                        )
                    )
                }

                context?.let { myAdapter.MembreRecyclerAdapter(membres, it) }
                binding.rvMembres.adapter = myAdapter

            }

        return binding.root
    }
}
