package cat.copernic.meetdis

import android.os.Bundle
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

    private val myAdapter: MembreRecyclerAdapter = MembreRecyclerAdapter()

    private val db = FirebaseFirestore.getInstance()

    private var membres: ArrayList<Membre> = arrayListOf()

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
                    membres.add(
                        Membre(
                            document.get("nom").toString(),
                            document.get("cognoms").toString(),
                            document.get("dni").toString(),
                            document.get("contrasenya").toString(),
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
