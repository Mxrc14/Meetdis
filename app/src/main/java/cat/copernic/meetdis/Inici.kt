package cat.copernic.meetdis

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.meetdis.adapters.OfertaRecyclerAdapter
import cat.copernic.meetdis.databinding.FragmentIniciBinding
import cat.copernic.meetdis.models.Oferta
import com.google.firebase.firestore.FirebaseFirestore


class Inici : Fragment() {


    private val myAdapter: OfertaRecyclerAdapter = OfertaRecyclerAdapter()

    private val db = FirebaseFirestore.getInstance()

    private var ofertes: ArrayList<Oferta> = arrayListOf()

    private lateinit var binding: FragmentIniciBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<FragmentIniciBinding>(
            inflater,
            R.layout.fragment_inici, container, false
        )
        val args = IniciArgs.fromBundle(requireArguments())

        binding.rvOfertes.setHasFixedSize(true)

        binding.rvOfertes.layoutManager = LinearLayoutManager(requireContext())




        db.collection("ofertes")
            .get()
            .addOnSuccessListener { documents ->
                ofertes.clear()
                for (document in documents) {
                    ofertes.add(
                        Oferta(
                            document.get("titol").toString(),
                            document.get("descripcio").toString(),
                            document.get("dni").toString(),
                            document.get("data").toString(),
                            document.get("tipus").toString()
                        )
                    )
                }
                context?.let { myAdapter.OfertaRecyclerAdapter(ofertes, it) }
                binding.rvOfertes.adapter = myAdapter

            }

        binding.crearButton.setOnClickListener { view: View ->
            val dni: String = args.dni;
            view.findNavController()
                .navigate(IniciDirections.actionIniciFragmentToCrearOfertaFragment(dni))

        }

        return binding.root
    }


}


//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu, inflater)
//        inflater.inflate(R.menu.options_menu, menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
//                || super.onOptionsItemSelected(item)
//    }


