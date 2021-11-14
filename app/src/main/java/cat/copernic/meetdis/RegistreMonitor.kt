package cat.copernic.meetdis

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat.Token.fromBundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.Person.fromBundle
import androidx.databinding.DataBindingUtil
import androidx.media.AudioAttributesCompat.fromBundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_registre.*
import kotlinx.android.synthetic.main.fragment_registre_monitor.*

class RegistreMonitor : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<cat.copernic.meetdis.databinding.FragmentRegistreMonitorBinding>(inflater,
            R.layout.fragment_registre_monitor,container,false)

        val args = RegistreMonitorArgs.fromBundle(requireArguments())


        var myCheck = binding.checkBoxTerminis
        var finalitza = binding.bFinalitzar


        myCheck!!.setOnCheckedChangeListener { buttonView, isChecked ->
            finalitza!!.isEnabled = myCheck!!.isChecked
        }

//       binding.imageCamara.setOnClickListener { cameraIntent() }




        binding.bFinalitzar.setOnClickListener { view: View ->

            Log.i("Registre Monitor", "Estem al listener boto Finalitzar")
            view.findNavController()
                .navigate(RegistreMonitorDirections.actionLogInFragmentToIniciFragment())

            db.collection("users").document(args.dni).set(
                hashMapOf(
                    "dni" to args.dni,
                    "contrasenya" to args.contrasenya,
                    "tipus d´usuari" to args.tipus,
                    "nom" to textNom.text.toString(),
                    "cognoms" to textCognom.text.toString(),
                    "correu" to textCorreu.text.toString()
                )
            )

        }

        //RECOGER INFO DE OTRO FRAGMENT
        //MENU

        return binding.root
    }


    fun cameraIntent() : Intent {
        val imageSelector = Intent(Intent.ACTION_PICK)

        return imageSelector
    }

}