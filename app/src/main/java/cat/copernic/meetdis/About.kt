package cat.copernic.meetdis

import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.ViewModelProvider
import cat.copernic.meetdis.databinding.FragmentAboutBinding


class About : Fragment() {

    lateinit var binding: FragmentAboutBinding


    private lateinit var viewModel: AboutViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

       binding = FragmentAboutBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)

        viewModel.text3.observe(viewLifecycleOwner, {
            binding.textView3.text = it.toString()
        })
        viewModel.text4.observe(viewLifecycleOwner, {
            binding.textView4.text = it.toString()
        })
        viewModel.text5.observe(viewLifecycleOwner, {
            binding.textView5.text = it.toString()
        })


        return binding.root

    }






}