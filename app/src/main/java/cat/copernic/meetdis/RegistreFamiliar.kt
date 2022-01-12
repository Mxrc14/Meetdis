package cat.copernic.meetdis

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.text.isDigitsOnly
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import cat.copernic.meetdis.databinding.FragmentRegistreFamiliarBinding
import cat.copernic.meetdis.databinding.FragmentRegistreMonitorBinding
import com.github.dhaval2404.colorpicker.util.setVisibility
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_registre.*
import kotlinx.android.synthetic.main.fragment_registre_familiar.*
import kotlinx.android.synthetic.main.fragment_registre_usuari.*
import kotlinx.android.synthetic.main.fragment_registre_usuari.textCognom
import kotlinx.android.synthetic.main.fragment_registre_usuari.textNom
import java.io.ByteArrayOutputStream
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegistreFamiliar.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegistreFamiliar : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    private val storageRef = FirebaseStorage.getInstance().getReference()

    lateinit var binding: FragmentRegistreFamiliarBinding

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
        binding = DataBindingUtil.inflate<cat.copernic.meetdis.databinding.FragmentRegistreFamiliarBinding>(inflater,
            R.layout.fragment_registre_familiar,container,false)

        val args = RegistreUsuariArgs.fromBundle(requireArguments())

        binding.imageCamara.setOnClickListener {
            escollirCamaraGaleria()


        }


        val myCheck = binding.checkBoxTerminis
        val finalitza = binding.bFinalitzar


        myCheck.setOnCheckedChangeListener { buttonView, isChecked ->
            finalitza.isEnabled = myCheck.isChecked
        }



        binding.bFinalitzar.setOnClickListener { view: View ->

            if (textCorreu.text.isNotEmpty() && textNom.text.isNotEmpty() && textCognom.text.isNotEmpty()) {

                var letra: Char = textCorreu.text.toString()
                    .substring(textCorreu.length() - 1, textCorreu.length()).get(0)

                var numeros: String =
                    textCorreu.text.toString().substring(0, textCorreu.length() - 1)

                if (letra.isLetter() && numeros.isDigitsOnly() && letra.isUpperCase()) {
                    var dni: String = args.dni;
                    dni = dni.lowercase()


                    view.findNavController()
                        .navigate(RegistreFamiliarDirections.actionLogInFragmentToIniciFragment(dni))

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(dni + "@prodis.cat",
                    args.contrasenya)

                    db.collection("users").document(args.dni).set(
                        hashMapOf(
                            "correu" to dni + "@prodis.cat",
                            "contrasenya" to args.contrasenya,
                            "tipus d´usuari" to args.tipus,
                            "nom" to textNom.text.toString(),
                            "cognoms" to textCognom.text.toString(),
                            "dniUsuariProdis" to textCorreu.text.toString(),
                            "descripcio" to ""

                        )
                    )
                } else {
                    val toast = Toast.makeText(requireContext(), R.string.dni_invalid, Toast.LENGTH_LONG)
                    toast.show()
                }
            } else {
                val toast = Toast.makeText(requireContext(), R.string.algun_camp_buit, Toast.LENGTH_LONG)
                toast.show()
            }
            pujarImatge(view)
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

    fun escollirCamaraGaleria() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.seleccio_opcio)
        builder.setMessage(R.string.selecciona)
        builder.setPositiveButton(R.string.camara, { dialog, which -> obrirCamera() })
        builder.setNegativeButton(R.string.galeria, { dialog, which -> obrirGaleria() })
        builder.show()
    }

    private fun getTmpFileUri(): Uri? {
        val tmpFile = File.createTempFile("tmp_image_file", ".png", activity?.cacheDir).apply {
            createNewFile()
            deleteOnExit()
        }

        return activity?.let { FileProvider.getUriForFile(it.applicationContext, "cat.copernic.meetdis.provider", tmpFile) }
    }
    fun pujarImatge(root: View){
        // pujar imatge al Cloud Storage de Firebase
        // https://firebase.google.com/docs/storage/android/upload-files?hl=es
        val args = RegistreFamiliarArgs.fromBundle(requireArguments())
        // Creem una referència amb el path i el nom de la imatge per pujar la imatge
        val pathReference = storageRef.child("users/"+ args.dni)
        val bitmap = (binding.imageCamara.drawable as BitmapDrawable).bitmap // agafem la imatge del imageView
        val baos = ByteArrayOutputStream() // declarem i inicialitzem un outputstream

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos) // convertim el bitmap en outputstream
        val data = baos.toByteArray() //convertim el outputstream en array de bytes.

        val uploadTask = pathReference.putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(activity, resources.getText(R.string.Error_pujar_foto), Toast.LENGTH_LONG).show()


        }.addOnSuccessListener {
            Toast.makeText(activity, resources.getText(R.string.Exit_pujar_foto), Toast.LENGTH_LONG).show()

        }
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