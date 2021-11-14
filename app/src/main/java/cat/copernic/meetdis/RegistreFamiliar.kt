package cat.copernic.meetdis

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.google.firebase.firestore.FirebaseFirestore
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

            Log.i("Registre Familiar", "Estem al listener boto Finalitzar")
            view.findNavController()
                .navigate(RegistreFamiliarDirections.actionLogInFragmentToIniciFragment())

            db.collection("users").document(args.dni).set(
                hashMapOf(
                    "dni" to args.dni,
                    "contrasenya" to args.contrasenya,
                    "tipus d´usuari" to args.tipus,
                    "nom" to textNom.text.toString(),
                    "cognoms" to textCognom.text.toString(),
                    "dniUsuariProdis" to textCorreu.text.toString()
                )
            )

        }

        return binding.root
    }


}