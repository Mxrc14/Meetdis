package cat.copernic.meetdis

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import android.R
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_registre_usuari.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistreUsuari.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistreUsuari : Fragment() {

    var myCheck: CheckBox? = null
    var finalitza: Button? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = DataBindingUtil.inflate<cat.copernic.meetdis.databinding.FragmentRegistreUsuariBinding>(inflater,
            cat.copernic.meetdis.R.layout.fragment_registre_usuari,container,false)



        myCheck = binding.checkBoxTerminis
        finalitza = binding.bFinalitzar


        myCheck!!.setOnCheckedChangeListener { buttonView, isChecked ->
            finalitza!!.isEnabled = myCheck!!.isChecked
        }





        binding.bFinalitzar.setOnClickListener { view: View ->

            Log.i("Registre Usuari", "Estem al listener boto Finalitzar")
            view.findNavController()
                .navigate(RegistreUsuariDirections.actionLogInFragmentToIniciFragment())

        }
        return binding.root

    }

}

