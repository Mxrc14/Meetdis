package cat.copernic.meetdis

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cat.copernic.meetdis.databinding.FragmentPerfilUsuariBinding
import com.github.dhaval2404.colorpicker.util.setVisibility
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_registre.*
import android.text.SpannableStringBuilder

import android.text.Editable
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import coil.api.load
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.File



class PerfilUsuari : Fragment() {

    val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    binding.imageCamara.setImageURI(uri)
                }
            }
        }

    private var latestTmpUri: Uri? = null

    private val storageRef = FirebaseStorage.getInstance().getReference()

    private val db = FirebaseFirestore.getInstance()

    lateinit var binding: FragmentPerfilUsuariBinding

    var tipo: String? = null
    var nom: String? = null
//    var cognom: String? = null
//    var descrip: String? = null
//    var img: String? = null
//    var dniF: String? = null
//    var monitorC: String? = null
    val user = FirebaseAuth.getInstance().currentUser

    val uid = user?.email

    var dniUser: String = uid.toString().substring(0, uid.toString().length - 11).uppercase()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate<FragmentPerfilUsuariBinding>(
            inflater,
            R.layout.fragment_perfil_usuari, container, false
        )





        val userdni = db.collection("users").document(dniUser.uppercase())

        userdni.get().addOnSuccessListener { document ->
            if (document.exists()) {
                tipo = document.data?.get("tipus").toString()
            }


        }

        val storageRef1 = FirebaseStorage.getInstance().reference

        val imageRef = storageRef1.child("users/$dniUser")
        imageRef.downloadUrl.addOnSuccessListener { url ->
            binding.imageCamara.load(url)
            // Log.i("proba_id", "$imageMembre")
        }.addOnFailureListener {
            //mostrar error
        }


        when (tipo) {
            "Monitor" -> {
                binding.dniUsuariProdis.setVisibility(false)
                userdni.get().addOnSuccessListener { document ->
                    if (document.exists()) {

                        binding.textNom.text = SpannableStringBuilder(document.data?.get("nom").toString())
                        binding.textCognom.text = SpannableStringBuilder(document.data?.get("cognoms").toString())
                        binding.descripcio.text = SpannableStringBuilder(document.data?.get("descripcio").toString())
                        binding.textCorreu.text = SpannableStringBuilder(document.data?.get("correuMonitor").toString())


                    }


                }
            }
            "Familiar" -> {
                binding.textCorreu.setVisibility(false)

                userdni.get().addOnSuccessListener { document ->
                    if (document.exists()) {

                        binding.textNom.text = SpannableStringBuilder(document.data?.get("nom").toString())
                        binding.textCognom.text = SpannableStringBuilder(document.data?.get("cognoms").toString())
                        binding.descripcio.text = SpannableStringBuilder(document.data?.get("descripcio").toString())
                        binding.dniUsuariProdis.text = SpannableStringBuilder(document.data?.get("dniUsuariProdis").toString())

                    }

                }

            }
            else -> {
                binding.dniUsuariProdis.setVisibility(false)
                binding.textCorreu.setVisibility(false)

                userdni.get().addOnSuccessListener { document ->
                    if (document.exists()) {

                        binding.textNom.text = SpannableStringBuilder(document.data?.get("nom").toString())
                        binding.textCognom.text = SpannableStringBuilder(document.data?.get("cognoms").toString())
                        binding.descripcio.text = SpannableStringBuilder(document.data?.get("descripcio").toString())


                    }


                }

            }
        }



        binding.imageCamara.setOnClickListener{  view: View ->

            escollirCamaraGaleria()

        }

        binding.bActualitza.setOnClickListener { view: View ->

            pujarImatge(view)

            if (binding.textNom.text.toString().isNotEmpty()) {
                userdni.update("nom", binding.textNom.text.toString())
            }

            if (binding.textCognom.text.toString().isNotEmpty()) {
                userdni.update("cognoms", binding.textCognom.text.toString())
            }
            if (binding.descripcio.text.toString().isNotEmpty()) {
                userdni.update("descripcio", binding.descripcio.text.toString())
            }
            if (binding.textCorreu.text.toString().isNotEmpty()) {
                userdni.update("correu", binding.textCorreu.text.toString())
            }

            if (binding.dniUsuariProdis.text.toString().isNotEmpty()) {
                userdni.update("dniUsuariProdis", binding.dniUsuariProdis.text.toString())
            }

        }


        return binding.root

    }



    private val startForActivityGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
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

        return activity?.let {
            FileProvider.getUriForFile(
                it.applicationContext,
                "cat.copernic.meetdis.provider",
                tmpFile
            )
        }
    }

    fun pujarImatge(root: View) {


        // Creem una referència amb el path i el nom de la imatge per pujar la imatge

        val pathReference = storageRef.child("users/$dniUser")
        val bitmap =
            (binding.imageCamara.drawable as BitmapDrawable).bitmap // agafem la imatge del imageView
        val baos = ByteArrayOutputStream() // declarem i inicialitzem un outputstream

        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            baos
        ) // convertim el bitmap en outputstream
        val data = baos.toByteArray() //convertim el outputstream en array de bytes.

        val uploadTask = pathReference.putBytes(data)
        uploadTask.addOnFailureListener {
            Toast.makeText(
                activity,
                resources.getText(R.string.Error_pujar_foto),
                Toast.LENGTH_LONG
            ).show()


        }.addOnSuccessListener {
            Toast.makeText(
                activity,
                resources.getText(R.string.Exit_pujar_foto),
                Toast.LENGTH_LONG
            )
                .show()

        }
    }



}
