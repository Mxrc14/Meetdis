package cat.copernic.meetdis

import android.R.attr
import android.R.attr.*
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import cat.copernic.meetdis.databinding.FragmentLogInBinding
import android.text.method.HideReturnsTransformationMethod

import android.text.method.PasswordTransformationMethod

import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_registre.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment() {

    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMenuVisibility(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<cat.copernic.meetdis.databinding.FragmentLogInBinding>(inflater,
            R.layout.fragment_log_in,container,false)

        binding.bRegistre.setOnClickListener { view: View ->

           Log.i("login Fragment", "Estem al listener boto registre")
            view.findNavController()
                .navigate(LogInFragmentDirections.actionLogInFragmentToRegistreFragment())



        }

        binding.bEntra.setOnClickListener { view: View ->
            Log.i("login Fragment", "Estem al listener boto entra")
            view.findNavController()
                .navigate(LogInFragmentDirections.actionLogInFragmentToIniciFragment())


        }

        binding.oblidar.setOnClickListener { view: View ->
            Log.i("login Fragment", "Estem al listener boto oblidar")
            view.findNavController()
                .navigate(LogInFragmentDirections.actionLogInFragmentToOblidatContrasenyaFragment())
        }

        setHasOptionsMenu(false)

        return binding.root
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        setMenuVisibility(false)

    }

}
