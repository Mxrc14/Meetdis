package cat.copernic.meetdis

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.meetdis.adapters.MembreRecyclerAdapter
import cat.copernic.meetdis.adapters.OfertaRecyclerAdapter
import cat.copernic.meetdis.databinding.*
import cat.copernic.meetdis.models.Membre
import cat.copernic.meetdis.models.Oferta
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_notificacio.*

class Notificacio : Fragment() {

    private val myAdapter: MembreRecyclerAdapter = MembreRecyclerAdapter()

    private val db = FirebaseFirestore.getInstance()

    private var membres: ArrayList<Membre> = arrayListOf()

    private var layoutManager: RecyclerView.LayoutManager? = null

//    private var adapter: RecyclerView.Adapter<RecyclerAdapter.>? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

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
                            document.get("cognoms").toString(),
                            document.get("contrasenya").toString(),
                            document.get("dni").toString(),
                            document.get("nom").toString(),
                            document.get("tipus d'usuari").toString()
                        )
                    )
                }

                context?.let { myAdapter.MembreRecyclerAdapter(membres, it) }
                binding.rvMembres.adapter = myAdapter

            }

        return binding.root
    }



        // Lloreria 41.210103794078506, 1.6732920326085583



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, findNavController())
                || super.onOptionsItemSelected(item)
    }

}