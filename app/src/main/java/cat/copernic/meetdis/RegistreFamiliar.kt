package cat.copernic.meetdis

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.github.dhaval2404.colorpicker.util.setVisibility
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_registre.*
import kotlinx.android.synthetic.main.fragment_registre_familiar.*
import kotlinx.android.synthetic.main.fragment_registre_usuari.*
import kotlinx.android.synthetic.main.fragment_registre_usuari.textCognom
import kotlinx.android.synthetic.main.fragment_registre_usuari.textNom

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<cat.copernic.meetdis.databinding.FragmentRegistreFamiliarBinding>(inflater,
            R.layout.fragment_registre_familiar,container,false)

        val args = RegistreUsuariArgs.fromBundle(requireArguments())


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
                            "dni" to dni + "@prodis.cat",
                            "contrasenya" to args.contrasenya,
                            "tipus d´usuari" to args.tipus,
                            "nom" to textNom.text.toString(),
                            "cognoms" to textCognom.text.toString(),
                            "dniUsuariProdis" to textCorreu.text.toString()
                        )
                    )
                } else {
                    val toast = Toast.makeText(requireContext(), "DNI no valid", Toast.LENGTH_LONG)
                    toast.show()
                }
            } else {
                val toast = Toast.makeText(requireContext(), "Algun camp esta buit", Toast.LENGTH_LONG)
                toast.show()
            }
        }
        return binding.root
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