package cat.copernic.meetdis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.meetdis.databinding.FragmentBuscarBinding
import cat.copernic.meetdis.databinding.FragmentCrearOfertaBinding
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