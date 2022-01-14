package cat.copernic.meetdis


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cat.copernic.meetdis.databinding.FragmentContingutOfertaBinding
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import coil.api.load
import com.github.dhaval2404.colorpicker.util.setVisibility
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import cat.copernic.meetdis.MainActivity as MainActivity1

var ofertaTitol: String? = null
var ofertaDesc: String? = null
var ofertaData: String? = null
var ofertaDNI: String? = null
var ofertaImg: String? = null
var ofertaLat: Double? = null
var ofertaLon: Double? = null
var ofertaTipus: String? = null

class ContingutOferta : Fragment(), OnMapReadyCallback {


    val user = FirebaseAuth.getInstance().currentUser

    val uid = user?.email

    var dniS: String = uid.toString().substring(0, uid.toString().length - 11).uppercase()

    private var latestTmpUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentContingutOfertaBinding>(
            inflater,
            R.layout.fragment_contingut_oferta, container, false
        )

        val i:Intent = activity!!.intent



Log.i("ContingutOferta", "$ofertaTitol")

//        val bundle = Bundle()

        ofertaTipus = arguments?.getSerializable("ofertaTipus") as String?
        ofertaTitol = arguments?.getSerializable("ofertaTitol") as String?
        ofertaDesc = arguments?.getSerializable("ofertaDesc") as String?
        ofertaData = arguments?.getSerializable("ofertaData") as String?
        ofertaDNI = arguments?.getSerializable("ofertaDNI") as String?
        ofertaImg= arguments?.getSerializable("ofertaImg") as String?
        ofertaLat= arguments?.getSerializable("ofertaLat") as Double?
        ofertaLon= arguments?.getSerializable("ofertaLon") as Double?



Log.i("ContingutOferta1", "$ofertaTitol")





        binding.textTitol.text = ofertaTitol
        binding.descripcio.text = ofertaDesc
        binding.textData.text = ofertaData
        binding.textTipus.text = ofertaTipus

        val storageRef = FirebaseStorage.getInstance().reference

        val imageRef = storageRef.child("ofertes/$ofertaImg")
        imageRef.downloadUrl.addOnSuccessListener { url ->
            binding.imageCamara.load(url)
            // Log.i("proba_id", "$imageMembre")
        }.addOnFailureListener {
            //mostrar error
        }

        binding.icMembres.setOnClickListener { view: View ->

            view.findNavController()
                .navigate(ContingutOfertaDirections.actionOfertaFragmentFragmentToMembresFragment())
        }

        binding.mapView.setOnClickListener{ view: View ->


        //cridarMapa()


            val gmmIntentUri =
                Uri.parse("geo:$ofertaLat,$ofertaLon")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)



//            // Display a label at the location of Google's Sydney office
//            // Display a label at the location of Google's Sydney office
//            val gmmIntentUri = Uri.parse("41°35'16.4\"N 1°58'51.0\"E")
//            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//            mapIntent.setPackage("com.google.android.apps.maps")
//            startActivity(mapIntent)


        }

        Log.i("ContingutOfertaDNI", "$ofertaLat")
        Log.i("ContingutOfertaDNI", "$ofertaLon")



        if (ofertaLat == 0.0 && ofertaLon == 0.0){
            binding.telematicText.setVisibility(true)
        }else{
            binding.mapView.setVisibility(true)
        }


            if (dniS.uppercase() == ofertaDNI.toString().uppercase()){
                binding.bEliminar.setVisibility(true)
                binding.bModificar.setVisibility(true)
                binding.bInscriuMe.setVisibility(false)

            }else{
                binding.bEliminar.setVisibility(false)
                binding.bModificar.setVisibility(false)
                binding.bInscriuMe.setVisibility(true)
            }


        //val args = BuscarArgs.fromBundle(requireArguments())


        return binding.root
    }

    fun cridarMapa() {

//        val gmmIntentUri =
//            Uri.parse("geo:41.210103794078506,1.6732920326085583")
//        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//        mapIntent.setPackage("com.google.android.apps.maps")
//        startActivity(mapIntent)

    }

    override fun onMapReady(p0: GoogleMap) {
//        var mLatLng1 = ofertaLon?.let {
//            if (ofertaLat != null) {
//                LatLng(ofertaLat!!, it)
//            }
//        }

        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        val sydney = LatLng(-33.852, 151.211)
        p0.addMarker(
            MarkerOptions()
                .position(sydney)
                .title("Marker in Sydney")
        )
        p0.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }


}