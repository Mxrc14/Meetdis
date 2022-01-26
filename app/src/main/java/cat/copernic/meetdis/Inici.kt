package cat.copernic.meetdis

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.OnBackPressedDispatcherOwner
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cat.copernic.meetdis.adapters.OfertaRecyclerAdapter
import cat.copernic.meetdis.databinding.FragmentIniciBinding
import cat.copernic.meetdis.models.Oferta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.HashMap
import kotlin.concurrent.fixedRateTimer
import android.content.Intent





class Inici : Fragment() {


    private var myAdapter: OfertaRecyclerAdapter = OfertaRecyclerAdapter()

    private val db = FirebaseFirestore.getInstance()

    private var ofertes: ArrayList<Oferta> = arrayListOf()

    private lateinit var binding: FragmentIniciBinding


    val user = FirebaseAuth.getInstance().currentUser

    val uid = user?.email

    var dni: String = uid.toString().substring(0, uid.toString().length - 11).uppercase()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("Inici", "$dni")
        binding = DataBindingUtil.inflate<FragmentIniciBinding>(
            inflater,
            R.layout.fragment_inici, container, false
        )


        //val args = IniciArgs.fromBundle(requireArguments())

        setFragmentResult("dniKey", bundleOf("DNIKey" to dni))

        // setFragmentResult("tancar", bundleOf("singOut" to 1))

        Log.i("MainActivityxd1", dni)

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d(TAG, "Fragment back pressed invoked")
                    // Do custom work here

                    // if you want onBackPressed() to be called as normal afterwards
                    if (isEnabled) {

                        val builder = AlertDialog.Builder(context)
                        builder.setTitle(getString(R.string.pregunta_sortir))
                        builder.setMessage(R.string.selecciona)
                        builder.setPositiveButton(getString(R.string.SORTIR)) { dialog, id ->

                            val intent = Intent(Intent.ACTION_MAIN)
                            intent.addCategory(Intent.CATEGORY_HOME)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                        builder.setNegativeButton(getString(R.string.CANCELA)) { dialog, which ->

                            view?.findNavController()
                                ?.navigate(IniciDirections.actionIniciFragmentSelf())

                        }

                        builder.show()

                    }

                    }

            }
            )


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
                            document.get("tipus").toString(),
                            document.id,
                            document.get("latitut") as Double,
                            document.get("longitud") as Double
                        )
                    )
                }

                context?.let { myAdapter.OfertaRecyclerAdapter(ofertes, it) }
                binding.rvOfertes.adapter = myAdapter

            }

        binding.crearButton.setOnClickListener { view: View ->

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


