package cat.copernic.meetdis

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import cat.copernic.meetdis.databinding.ActivityMainBinding
import cat.copernic.meetdis.databinding.FragmentContacteBinding
import cat.copernic.meetdis.databinding.FragmentRegistreUsuariBinding
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_registre_familiar.*
import kotlinx.android.synthetic.main.fragment_registre_monitor.*
import kotlinx.android.synthetic.main.fragment_registre_monitor.textCorreu
import kotlinx.android.synthetic.main.fragment_registre_usuari.*
import kotlinx.android.synthetic.main.fragment_registre_usuari.textCognom
import kotlinx.android.synthetic.main.fragment_registre_usuari.textNom
import java.io.File



// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistreUsuari.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistreUsuari : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    lateinit var binding: FragmentRegistreUsuariBinding

    var myCheck: CheckBox? = null
    var finalitza: Button? = null

    private var latestTmpUri: Uri? = null

    val takeImageResult = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        if (isSuccess) {
            latestTmpUri?.let { uri ->
                binding.imageCamara.setImageURI(uri)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<cat.copernic.meetdis.databinding.FragmentRegistreUsuariBinding>(inflater,
            cat.copernic.meetdis.R.layout.fragment_registre_usuari,container,false)


        val args = RegistreUsuariArgs.fromBundle(requireArguments())

        //Camara
        binding.imageCamara.setOnClickListener {
            escollirCamaraGaleria()

        }

        myCheck = binding.checkBoxTerminis
        finalitza = binding.bFinalitzar


        myCheck!!.setOnCheckedChangeListener { buttonView, isChecked ->
            finalitza!!.isEnabled = myCheck!!.isChecked
        }


        binding.bFinalitzar.setOnClickListener { view: View ->


            if (textNom.text.isNotEmpty() && textCognom.text.isNotEmpty()) {
                var dni: String = args.dni;
                dni = dni.lowercase()
                view.findNavController()
                    .navigate(RegistreUsuariDirections.actionLogInFragmentToIniciFragment(dni))

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(dni + "@prodis.cat",
                    args.contrasenya)

                db.collection("users").document(args.dni).set(
                    hashMapOf(
                        "dni" to dni + "@prodis.cat",
                        "contrasenya" to args.contrasenya,
                        "tipus d´usuari" to args.tipus,
                        "nom" to textNom.text.toString(),
                        "imatge" to getTmpFileUri(),
                        "cognoms" to textCognom.text.toString()

                    )
                )

            } else {
                val toast = Toast.makeText(requireContext(), "Algun camp esta buit", Toast.LENGTH_LONG)
                toast.show()
            }
        }
        return binding.root

    }
    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){ result ->
        if (result.resultCode == Activity.RESULT_OK){
            val data = result.data?.data
            //setImageUri només funciona per rutes locals, no a internet
            binding?.imageCamara?.setImageURI(data)
        }
    }

    private fun obrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startForActivityGallery.launch(intent)
    }

    private fun obrirCamera() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                latestTmpUri = uri

                takeImageResult.launch(uri)
            }
        }
    }

    fun escollirCamaraGaleria(){
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle("Selecciona l´ opció amb la que vols obtenir la foto")
        alertDialog.setMessage("Selecciona:")
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "CAMARA"
        ){dialog, which -> obrirCamera()}
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, "GALERIA"
        ){dialog, which -> obrirGaleria()}
        alertDialog.show()

    }

    private fun getTmpFileUri(): Uri? {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", activity?.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return activity?.let { FileProvider.getUriForFile(it.applicationContext, "cat.copernic.meetdis.provider", tmpFile) }
    }

}

