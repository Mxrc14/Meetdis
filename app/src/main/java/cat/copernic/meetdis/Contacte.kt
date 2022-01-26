package cat.copernic.meetdis

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cat.copernic.meetdis.databinding.FragmentContacteBinding
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import cat.copernic.meetdis.viewmodels.ContacteViewModel


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

        binding.bEnviar.setOnClickListener(){
            enviarmail()

            view?.findNavController()
                ?.navigate(ContacteDirections.actionContacteFragmentToIniciFragment())
        }


        requireActivity()
            .onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    Log.d(ContentValues.TAG, "Fragment back pressed invoked")
                    // Do custom work here

                    // if you want onBackPressed() to be called as normal afterwards
                    if (isEnabled) {
                        view?.findNavController()
                            ?.navigate(ContacteDirections.actionContacteFragmentToIniciFragment())
                    }
                }
            }
            )


        return binding.root

    }


    fun enviarmail(){
        var mail = arrayOf("meetdisProdis@gmail.com")
        val email = Intent(Intent.ACTION_SEND)
        email.setData(Uri.parse("mail to:"))
        email.setType("text/plain")
        email.putExtra(android.content.Intent.EXTRA_EMAIL, mail)
        email.putExtra(Intent.EXTRA_SUBJECT, binding.textTitol.text.toString())
        email.putExtra(Intent.EXTRA_TEXT, getString(R.string.nomICognoms)+ ": " + binding.textDNI.text.toString() + "\n\n" + getString(R.string.descripcio) + ": "+ "\n"   + binding.descripcio.text.toString() );

            startActivity(Intent.createChooser(email, "Enviar email."))

        }

    }






