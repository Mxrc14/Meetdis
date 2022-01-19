package cat.copernic.meetdis


import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import cat.copernic.meetdis.databinding.FragmentContingutOfertaBinding
import coil.api.load
import com.github.dhaval2404.colorpicker.util.setVisibility
import com.google.android.gms.maps.GoogleMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class ContingutOferta : Fragment() {

    var ofertaTitol: String? = null
    var ofertaDesc: String? = null
    var ofertaData: String? = null
    var ofertaDNI: String? = null
    var ofertaImg: String? = null
    var ofertaLat: Double? = null
    var ofertaLon: Double? = null
    var ofertaTipus: String? = null

    var usuariJaInscrit = false

    private val db = FirebaseFirestore.getInstance()

    val storageRef = FirebaseStorage.getInstance().reference

    private val user = FirebaseAuth.getInstance().currentUser

    private val uid = user?.email

    private var dniS: String = uid.toString().substring(0, uid.toString().length - 11).uppercase()

    private var latestTmpUri: Uri? = null

    private lateinit var map: GoogleMap

    private var lista = arrayListOf<String>()

    lateinit var binding: FragmentContingutOfertaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate<FragmentContingutOfertaBinding>(
            inflater,
            R.layout.fragment_contingut_oferta, container, false
        )


        val i: Intent = activity!!.intent


//        val bundle = Bundle()

        ofertaTipus = arguments?.getSerializable("ofertaTipus") as String?
        ofertaTitol = arguments?.getSerializable("ofertaTitol") as String?
        ofertaDesc = arguments?.getSerializable("ofertaDesc") as String?
        ofertaData = arguments?.getSerializable("ofertaData") as String?
        ofertaDNI = arguments?.getSerializable("ofertaDNI") as String?
        ofertaImg = arguments?.getSerializable("ofertaImg") as String?
        ofertaLat = arguments?.getSerializable("ofertaLat") as Double?
        ofertaLon = arguments?.getSerializable("ofertaLon") as Double?







        binding.textTitol.text = ofertaTitol
        binding.descripcio.text = ofertaDesc
        binding.textData.text = ofertaData
        binding.textTipus.text = ofertaTipus


        usuariInscrit()


        val imageRef = storageRef.child("ofertes/$ofertaImg")
        imageRef.downloadUrl.addOnSuccessListener { url ->
            binding.imageCamara.load(url)
            // Log.i("proba_id", "$imageMembre")
        }.addOnFailureListener {
            //mostrar error
        }




        binding.icMembres.setOnClickListener { view: View ->

            view.findNavController()
                .navigate(
                    ContingutOfertaDirections.actionOfertaFragmentFragmentToMembresFragment(
                        ofertaImg!!
                    )
                )
        }

        binding.mapView.setOnClickListener { view: View ->

            var intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("geo:<$ofertaLat>,<$ofertaLon>?q=<$ofertaLat>,<$ofertaLon>")
            )

            startActivity(intent)

        }




        if (ofertaLat == 0.0 && ofertaLon == 0.0) {
            binding.telematicText.setVisibility(true)
        } else {
            binding.mapView.setVisibility(true)
        }


        if (dniS.uppercase() == ofertaDNI.toString().uppercase()) {
            binding.bEliminar.setVisibility(true)
            binding.bModificar.setVisibility(true)
            binding.bInscriuMe.setVisibility(false)

        } else {
            binding.bEliminar.setVisibility(false)
            binding.bModificar.setVisibility(false)
            binding.bInscriuMe.setVisibility(true)
        }


        binding.bEliminar.setOnClickListener { view: View ->

            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(getString(R.string.eliminar_oferta_pregunta))
            builder.setPositiveButton(R.string.eliminar) { dialog, id ->

                db.collection("ofertes").document(ofertaImg.toString())
                    .delete()
                    .addOnSuccessListener {

                        db.collection("ofertes").document(ofertaImg.toString()).delete()
                        db.collection("inscrit").document(ofertaImg.toString()).delete()
                        storageRef.child("ofertes/$ofertaImg").delete()
                        val toast =
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.oferta_eliminada),
                                Toast.LENGTH_LONG
                            )
                        toast.show()

                        view.findNavController()
                            .navigate(ContingutOfertaDirections.actionOfertaFragmentFragmentToIniciFragment())
                    }
                    .addOnFailureListener {
                        val toast =
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.error_eliminar),
                                Toast.LENGTH_LONG
                            )
                        toast.show()
                    }


            }
            builder.show()

        }



        binding.bDesapuntarMe.setOnClickListener { view: View ->


            binding.bDesapuntarMe.setVisibility(false)
            binding.bInscriuMe.setVisibility(true)


        }


        binding.bInscriuMe.setOnClickListener { view: View ->

            Log.i("ContingutOferta", "$ofertaImg")

            db.collection("inscrit").document(ofertaImg.toString()).get().addOnSuccessListener {

                if (it.get("users") != null) {

                    lista = it.get("users") as ArrayList<String>
                    val userdetail = HashMap<String, Any>()


                    for (posicion in lista.indices) {

                        Log.i("VALORA", lista[posicion])


                        if (posicion == lista.size - 1) {

                            lista.add(dniS)

                            userdetail["users"] = lista
                        }
                        db.collection("inscrit").document(ofertaImg.toString()).delete()
                        db.collection("inscrit").document(ofertaImg.toString()).set(userdetail)
                    }
                }
                binding.bInscriuMe.setVisibility(false)
                binding.bDesapuntarMe.setVisibility(true)
            }
        }
        return binding.root
    }


    private fun usuariInscrit() {
        db.collection("inscrit").document(ofertaImg.toString()).get().addOnSuccessListener {

            if (it.get("users") != null) {
                lista = it.get("users") as ArrayList<String>

                for (posicion in lista.indices) {

                    if (lista[posicion] == dniS) {
                        usuariJaInscrit = true
                    }
                    var dniCreador: String = ""
                    db.collection("ofertes").document(ofertaImg.toString()).get()
                        .addOnSuccessListener {

                            dniCreador = it.get("dni").toString()
                            if (dniCreador != dniS) {
                                if (usuariJaInscrit) {
                                    binding.bInscriuMe.setVisibility(false)
                                    binding.bDesapuntarMe.setVisibility(true)
                                } else {
                                    binding.bInscriuMe.setVisibility(true)
                                    binding.bDesapuntarMe.setVisibility(false)
                                }
                            } else {
                                binding.bInscriuMe.setVisibility(false)
                                binding.bDesapuntarMe.setVisibility(false)
                            }
                        }


                }

            }
        }
    }


}
