package cat.copernic.meetdis

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController

class Log_In : Fragment() {




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            R.layout.activity_main,
            container,
            false
        )


        var navController = findNavController()
        var b_entrar: Button


        NavController.b_entrar.setOnClickListener() { view: View ->
            view.findNavController().navigate(R.id.action_mainActivityFragment_to_iniciFragment)
        }

        cambio()

        return binding.root
    }

    fun cambio(){

        return


    }
}
