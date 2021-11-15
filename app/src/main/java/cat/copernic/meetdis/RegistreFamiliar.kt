package cat.copernic.meetdis

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistreFamiliar.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistreFamiliar : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<cat.copernic.meetdis.databinding.FragmentRegistreFamiliarBinding>(inflater,
            R.layout.fragment_registre_familiar,container,false)


        val myCheck = binding.checkBoxTerminis
        val finalitza = binding.bFinalitzar


        myCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            finalitza.isEnabled = myCheck.isChecked
        }



        binding.bFinalitzar.setOnClickListener { view: View ->

            Log.i("Registre Familiar", "Estem al listener boto Finalitzar")
            view.findNavController()
                .navigate(RegistreFamiliarDirections.actionLogInFragmentToIniciFragment())

        }

        return binding.root
    }


}