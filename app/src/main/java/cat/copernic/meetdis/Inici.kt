package cat.copernic.meetdis

import android.app.ActionBar
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import cat.copernic.meetdis.databinding.FragmentIniciBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.fragment_inici.*
import java.util.zip.Inflater

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
lateinit var toolbar: ActionBar
/**
 * A simple [Fragment] subclass.
 * Use the [Inici.newInstance] factory method to
 * create an instance of this fragment.
 */
class Inici : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentIniciBinding>(inflater,
            R.layout.fragment_inici,container,false)
        val args = IniciArgs.fromBundle(requireArguments())

 binding.crearButton.setOnClickListener {view: View ->
     val dni: String = args.dni;
     view.findNavController()
         .navigate(IniciDirections.actionIniciFragmentToCrearOfertaFragment(dni))

 }

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,requireView().findNavController())
                ||super.onOptionsItemSelected(item)
    }



}