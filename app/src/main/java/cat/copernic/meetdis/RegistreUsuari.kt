package cat.copernic.meetdis

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import android.R
import android.widget.Button
import android.widget.CompoundButton
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_registre_familiar.*
import kotlinx.android.synthetic.main.fragment_registre_monitor.*
import kotlinx.android.synthetic.main.fragment_registre_monitor.textCorreu
import kotlinx.android.synthetic.main.fragment_registre_usuari.*
import kotlinx.android.synthetic.main.fragment_registre_usuari.textCognom
import kotlinx.android.synthetic.main.fragment_registre_usuari.textNom


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

    var myCheck: CheckBox? = null
    var finalitza: Button? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = DataBindingUtil.inflate<cat.copernic.meetdis.databinding.FragmentRegistreUsuariBinding>(inflater,
            cat.copernic.meetdis.R.layout.fragment_registre_usuari,container,false)

        val args = RegistreUsuariArgs.fromBundle(requireArguments())



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

                db.collection("users").document(args.dni).set(
                    hashMapOf(
                        "dni" to dni + "@prodis.cat",
                        "contrasenya" to args.contrasenya,
                        "tipus d´usuari" to args.tipus,
                        "nom" to textNom.text.toString(),
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

}

