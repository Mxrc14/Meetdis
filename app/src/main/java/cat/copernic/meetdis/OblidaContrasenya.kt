package cat.copernic.meetdis

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.meetdis.databinding.FragmentContacteBinding
import cat.copernic.meetdis.databinding.FragmentLogInBinding
import cat.copernic.meetdis.databinding.FragmentOblidaContrasenyaBinding
import com.github.dhaval2404.colorpicker.util.setVisibility
import com.google.android.material.bottomnavigation.BottomNavigationView


class OblidaContrasenya : Fragment() {

    lateinit var binding: FragmentOblidaContrasenyaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

         binding = DataBindingUtil.inflate<FragmentOblidaContrasenyaBinding>(inflater,
            R.layout.fragment_oblida_contrasenya,container,false)

        binding.bEnviar.setOnClickListener { view: View ->

            enviarmail()
            view.findNavController()
                .navigate(OblidaContrasenyaDirections.actionOblidatContrasenyaFragmentToLogInFragment())
        }
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


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        val navBar: BottomNavigationView = activity!!.findViewById(R.id.bottomMenu)
        navBar.setVisibility(visible = false)

    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        val navBar: BottomNavigationView = activity!!.findViewById(R.id.bottomMenu)
        navBar.setVisibility(visible = true)

    }


    }
