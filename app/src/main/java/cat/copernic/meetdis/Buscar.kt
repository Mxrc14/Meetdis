package cat.copernic.meetdis
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.meetdis.adapters.BuscaRecyclerAdapter
import cat.copernic.meetdis.databinding.FragmentBuscarBinding
import cat.copernic.meetdis.models.Busca
import com.google.firebase.firestore.FirebaseFirestore


class Buscar: Fragment() {

    private val myAdapter: BuscaRecyclerAdapter = BuscaRecyclerAdapter()

    private val db = FirebaseFirestore.getInstance()

    private var busca: ArrayList<Busca> = arrayListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentBuscarBinding>(
            inflater,
            R.layout.fragment_buscar, container, false
        )

        binding.rvBuscar.setHasFixedSize(true)

        binding.rvBuscar.layoutManager = LinearLayoutManager(requireContext())



        db.collection("busca")
            .get()
            .addOnSuccessListener { documents ->
                busca.clear()
                for (document in documents) {
                    busca.add(
                        Busca(
                            document.get("titol").toString(),
                            document.id
                        )
                    )
                }
                context?.let { myAdapter.BuscaRecyclerAdapter(busca, it) }
                binding.rvBuscar.adapter = myAdapter

            }
        return binding.root

    }
}