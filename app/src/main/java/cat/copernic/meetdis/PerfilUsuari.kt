package cat.copernic.meetdis

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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PerfilUsuari.newInstance] factory method to
 * create an instance of this fragment.
 */
class PerfilUsuari : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    lateinit var binding: FragmentPerfilUsuariBinding

    var tipo: String? = null
    var nom: String? = null
    var cognom: String? = null
    var descrip: String? = null
    var img: String? = null
    var dniF: String? = null
    var monitorC: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate<FragmentPerfilUsuariBinding>(
            inflater,
            R.layout.fragment_perfil_usuari, container, false
        )

        val user = FirebaseAuth.getInstance().currentUser

        val uid = user?.email

        var dni: String = uid.toString().substring(0, 9)

        val userdni = db.collection("users").document(dni.uppercase())

        userdni.get().addOnSuccessListener { document ->
            if (document.exists()) {
                tipo = document.data?.get("tipus").toString()
            }


        }

        when (tipo) {
            "Monitor" -> {
                binding.dniUsuariProdis.setVisibility(false)
                userdni.get().addOnSuccessListener { document ->
                    if (document.exists()) {

                        nom = document.data?.get("nom").toString()
                        cognom = document.data?.get("nom").toString()
                        descrip = document.data?.get("nom").toString()
                        img = document.data?.get("nom").toString()
                        monitorC = document.data?.get("nom").toString()

                    }


                }

            }
            "Familiar" -> {
                binding.textCorreu.setVisibility(false)

                userdni.get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        nom = document.data?.get("nom").toString()
                        cognom = document.data?.get("nom").toString()
                        descrip = document.data?.get("nom").toString()
                        img = document.data?.get("nom").toString()
                        dniF = document.data?.get("nom").toString()

                    }


                }

            }
            else -> {
                binding.dniUsuariProdis.setVisibility(false)
                binding.textCorreu.setVisibility(false)

                userdni.get().addOnSuccessListener { document ->
                    if (document.exists()) {
                        nom = document.data?.get("nom").toString()
                        cognom = document.data?.get("nom").toString()
                        descrip = document.data?.get("nom").toString()
                        img = document.data?.get("nom").toString()


                    }


                }

                //binding.textNom.text = nom





            }
        }

        binding.bActualitza.setOnClickListener {


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


}
