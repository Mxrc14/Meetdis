package cat.copernic.meetdis

import android.R.attr
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import cat.copernic.meetdis.databinding.FragmentLogInBinding
import android.text.method.HideReturnsTransformationMethod

import android.R.attr.password

import android.text.method.PasswordTransformationMethod

import android.R.attr.visible
import android.util.Log


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

        var esVisible = false




        //setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu,menu)
    }







    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LogInFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LogInFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
