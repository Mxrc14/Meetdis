package cat.copernic.meetdis

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import cat.copernic.meetdis.databinding.FragmentBuscarBinding
import cat.copernic.meetdis.databinding.FragmentIniciBinding






class Buscar : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentBuscarBinding>(
            inflater,
            R.layout.fragment_buscar, container, false
        )

        val args = BuscarArgs.fromBundle(requireArguments())


        return binding.root
    }

}