package cat.copernic.meetdis


import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import cat.copernic.meetdis.databinding.FragmentCrearOfertaBinding
import com.github.dhaval2404.colorpicker.util.setVisibility
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_crear_oferta.*
import kotlinx.coroutines.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.properties.Delegates


class CrearOferta : Fragment(), AdapterView.OnItemSelectedListener {

    private val db = FirebaseFirestore.getInstance()

    private val storageRef = FirebaseStorage.getInstance().getReference()

    lateinit var binding: FragmentCrearOfertaBinding

    var day by Delegates.notNull<Int>()
    var month by Delegates.notNull<Int>()
    var year by Delegates.notNull<Int>()


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
        createNotificationChannel()


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



            if (textTitol.text.isNotEmpty() && descripcio.text.isNotEmpty()) {


                val dni: String = args.dni.uppercase();


                var collUsersRef: CollectionReference = db.collection("ofertes")
                val doc = collUsersRef.document()
                var data: HashMap<String, Any> = hashMapOf(
                    "dni" to args.dni.uppercase(),
                    "titol" to textTitol.text.toString(),
                    "descripcio" to descripcio.text.toString(),
                    "data" to textData.text.toString(),
                    "tipus" to opcion.toString(),
                    "latitut" to localitzacio.latitude,
                    "longitud" to localitzacio.longitude
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


                generateNotification()




                //crearEvento()

                view.findNavController()
                    .navigate(CrearOfertaDirections.actionCrearOfertaFragmentToIniciFragment(dni))


            } else {
                val toast =
                    Toast.makeText(requireContext(), R.string.camp_buit, Toast.LENGTH_LONG)
                toast.show()
            }


        }

        binding.mapView.setOnClickListener { view: View ->

            Log.i("CrearOferta", "Boto de Maps")



            view.findNavController()
                .navigate(CrearOfertaDirections.actionCrearOfertaFragmentToMapsFragment())
        }


        binding.textData.setOnClickListener { showDatePickerDialog() }


        binding.spinnerEvents.onItemSelectedListener = this


        return binding.root
    }

    private fun crearEvento() {
        //Crear Evento Calendario

        val startMillis: Long = Calendar.getInstance().run {
            set(year, month, day, 0, 0)
            timeInMillis
        }
        val endMillis: Long = Calendar.getInstance().run {
            set(year, month, day, 23, 59)
            timeInMillis
        }

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startMillis)
            put(CalendarContract.Events.DTEND, endMillis)
            put(CalendarContract.Events.TITLE, textTitol.text.toString())
            put(CalendarContract.Events.DESCRIPTION, descripcio.text.toString())
            put(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
        }

        val uri: Uri? = activity?.contentResolver?.insert(Events.CONTENT_URI, values)

        // get the event ID that is the last element in the Uri
        val eventID: Long = uri?.lastPathSegment!!.toLong()


//                    val calIntent: Intent
//
//                    calIntent = Intent(Intent.ACTION_INSERT)
//                    calIntent.type = "vnd.android.cursor.item/event"
//
//                    calIntent.putExtra(Events.TITLE, textTitol.text.toString())
//                    calIntent.putExtra(Events.DESCRIPTION, descripcio.text.toString())
//
//                    startActivity(calIntent)
    }

    private fun generateNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.basic_channel_id)
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                activity!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


        val channelId = getString(R.string.basic_channel_id) // (1)

        var titol = "Event Creat"
        var descripcio = "L'event ha siguit creat correctament"

        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.meetdis) // (5)
            .setContentTitle(titol) // (6)
            .setContentText(descripcio) // (7)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT) // (9)
            .setAutoCancel(true)

        val notificationId: Int = 1

        with(NotificationManagerCompat.from(requireContext())) {
            // notificationId is a unique int for each notification that you must define
            notify(notificationId, notification.build())
        }

    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.basic_channel_id)
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }

            // Register the channel with the system
            val notificationManager: NotificationManager =
                activity!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

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
        this.day = day
        this.month = month
        this.year = year
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

