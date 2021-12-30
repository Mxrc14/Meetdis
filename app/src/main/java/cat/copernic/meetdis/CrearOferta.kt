package cat.copernic.meetdis


import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.TargetApi
import android.app.*
import android.content.ContentValues.TAG
import android.content.Context

import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.location.LocationRequest
import android.media.audiofx.BassBoost
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import cat.copernic.meetdis.databinding.FragmentCrearOfertaBinding


import com.github.dhaval2404.colorpicker.util.setVisibility
import com.google.android.gms.common.api.ResolvableApiException

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.CollectionReference

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_crear_oferta.*
import kotlinx.android.synthetic.main.fragment_registre.*
import kotlinx.android.synthetic.main.fragment_registre_familiar.*
import kotlinx.android.synthetic.main.fragment_registre_usuari.*
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.net.URI.create
import java.util.*
import kotlin.properties.Delegates

class CrearOferta : Fragment(), AdapterView.OnItemSelectedListener {

    private val db = FirebaseFirestore.getInstance()

    private val storageRef = FirebaseStorage.getInstance().getReference()

    lateinit var binding: FragmentCrearOfertaBinding

    private var latestTmpUri: Uri? = null

    var lat: Double? = 0.0

    var lon: Double? = 0.0


    lateinit var localitzacio: LatLng

    private val runningQOrLater =
        android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q


    val takeImageResult =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
            if (isSuccess) {
                latestTmpUri?.let { uri ->
                    binding.imageCamara.setImageURI(uri)
                }
            }
        }


    private var spinner: Spinner? = null
    private var opcion: String? = null
    private var identificadorOferta: String? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




            // Inflate the layout for this fragment
            binding = DataBindingUtil.inflate<FragmentCrearOfertaBinding>(
                inflater,
                R.layout.fragment_crear_oferta, container, false
            )
            ArrayAdapter.createFromResource(
                requireContext(), R.array.tipus_Events,

                R.layout.spinner_item
            ).also { adapter ->

                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
                binding.spinnerEvents.adapter = adapter

            }

            val args = CrearOfertaArgs.fromBundle(requireArguments())

            binding.imageCamara.setOnClickListener {
                escollirCamaraGaleria()


            }


            var tasca1: Job? = null
            binding.bCrear.setOnClickListener { view: View ->


                if (textTitol.text.isNotEmpty() && descripcio.text.isNotEmpty()) {


                    val dni: String = args.dni.uppercase();


                    var collUsersRef: CollectionReference = db.collection("ofertes")
                    val doc = collUsersRef.document()
                    var data: HashMap<String, Any> = hashMapOf(
                        "dni" to args.dni.uppercase(),
                        "titol" to textTitol.text.toString(),
                        "descripcio" to descripcio.text.toString(),
                        "data" to textData.text.toString(),
                        "tipus" to opcion.toString()
                    )


                    doc.set(data)
                    identificadorOferta = doc.id
                    Log.i("crearOferta", "doc.id: ${doc.id}")

                    pujarImatge(view)

                    tasca1 = crearCorrutina(
                        5, //Temps de durada de la corrutina 1
                        binding.bCrear, //Botó per activar la corrutina 1
                        binding.progressBarUn, //Progrés de la corrutina 1
                    )
                    view.findNavController()
                        .navigate(CrearOfertaDirections.actionCrearOfertaFragmentToIniciFragment(dni))


                } else {
                    val toast =
                        Toast.makeText(requireContext(), "Algun camp esta buit", Toast.LENGTH_LONG)
                    toast.show()
                }

                setFragmentResultListener("latKey") { latKey, bundle ->
                    // We use a String here, but any type that can be put in a Bundle is supported
                    lat = bundle.getDouble("bundleKey1")
                }

                setFragmentResultListener("lonKey") { longKey, bundle ->
                    // We use a String here, but any type that can be put in a Bundle is supported
                    lon = bundle.getDouble("bundleKey2")
                }
                localitzacio = LatLng(lat!!.toDouble(), lon!!.toDouble())

                Log.i("CrearOferta", "${localitzacio.latitude}, ${localitzacio.longitude}")

            }








            binding.mapView.setOnClickListener { view: View ->

                Log.i("CrearOferta", "Boto de Maps")

                setFragmentResult("dniKey", bundleOf("DNIKey" to args.dni))

                view.findNavController()
                    .navigate(CrearOfertaDirections.actionCrearOfertaFragmentToMapsFragment())
            }


            binding.textData.setOnClickListener { showDatePickerDialog() }


            binding.spinnerEvents.onItemSelectedListener = this


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
        val alertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle("Selecciona l´ opció amb la que vols obtenir la foto")
        alertDialog.setMessage("Selecciona:")
        alertDialog.setButton(
            AlertDialog.BUTTON_POSITIVE, "CAMARA"
        ) { dialog, which -> obrirCamera() }
        alertDialog.setButton(
            AlertDialog.BUTTON_NEGATIVE, "GALERIA"
        ) { dialog, which -> obrirGaleria() }
        alertDialog.show()

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
        // pujar imatge al Cloud Storage de Firebase
        // https://firebase.google.com/docs/storage/android/upload-files?hl=es
        val args = CrearOfertaArgs.fromBundle(requireArguments())
        // Creem una referència amb el path i el nom de la imatge per pujar la imatge
        Log.i("OFERTITA", "$identificadorOferta")
        val pathReference = storageRef.child("ofertes/${this.identificadorOferta}")
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

    private fun showDatePickerDialog() {
        val datePicker =
            DatePickerFragment { day, month, year -> onDateSelected(day, month, year) }

        activity?.let { datePicker.show(it.supportFragmentManager, "Date Picker") }


    }


    private fun setMapClick(map: GoogleMap) {
        map.setOnMapLongClickListener { }
    }

    fun onDateSelected(day: Int, month: Int, year: Int) {
        textData.setText("$day/$month/$year")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        opcion = parent?.getItemAtPosition(position).toString()
        Log.i("Registre", "Opció: $opcion")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }


    @DelicateCoroutinesApi
    private fun crearCorrutina(durada: Int, inici: Button, progres: ProgressBar) =
        GlobalScope.launch(

            Dispatchers.Main
        ) {

            progres.setVisibility(true)
            progres.progress = 0 //Situem el ProgressBar a l'inici del procés
            delay(5000)
            withContext(Dispatchers.IO) {
                var comptador = 0
                //Inciem el progrés de la barra de progrés
                while (comptador < durada) {
                    //Retardem el progrés de la barra
                    if (suspensio((durada * 50).toLong())) {
                        comptador++
                        progres.progress = (comptador * 50) / durada
                    }
                }
            }

            progres.progress = 0 //Situem el ProgressBar a l'inici del procés
            progres.setVisibility(false)
        }

    suspend fun suspensio(duracio: Long): Boolean {
        delay(duracio)
        return true
    }
}

