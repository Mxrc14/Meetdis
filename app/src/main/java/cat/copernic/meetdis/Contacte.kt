package cat.copernic.meetdis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cat.copernic.meetdis.databinding.FragmentAboutBinding
import cat.copernic.meetdis.databinding.FragmentContacteBinding


class Contacte : Fragment() {

    lateinit var binding: FragmentContacteBinding

    private lateinit var viewModel: ContacteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentContacteBinding.inflate(layoutInflater)


        viewModel = ViewModelProvider(this).get(ContacteViewModel::class.java)



       viewModel.nomICognom.observe(viewLifecycleOwner, {
            binding.textDNI.hint = it.toString()
        })
       viewModel.titol.observe(viewLifecycleOwner, {
            binding.textTitol.hint = it.toString()
        })
        viewModel.descripcio.observe(viewLifecycleOwner, {
            binding.descripcio.hint = it.toString()
        })
        viewModel.enviar.observe(viewLifecycleOwner, {
            binding.bEnviar.text = it.toString()
        })


        return binding.root

    }






}