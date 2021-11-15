package cat.copernic.meetdis

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController

class RegistreMonitor : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<cat.copernic.meetdis.databinding.FragmentRegistreMonitorBinding>(inflater,
            R.layout.fragment_registre_monitor,container,false)


        var myCheck = binding.checkBoxTerminis
        var finalitza = binding.bFinalitzar


        myCheck!!.setOnCheckedChangeListener { buttonView, isChecked ->
            finalitza!!.isEnabled = myCheck!!.isChecked
        }



        binding.bFinalitzar.setOnClickListener { view: View ->

            Log.i("Registre Monitor", "Estem al listener boto Finalitzar")
            view.findNavController()
                .navigate(RegistreMonitorDirections.actionLogInFragmentToIniciFragment())

        }

        return binding.root
    }


}