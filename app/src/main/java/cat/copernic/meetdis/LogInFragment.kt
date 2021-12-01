package cat.copernic.meetdis

import android.R.attr
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import cat.copernic.meetdis.databinding.FragmentLogInBinding
import android.text.method.HideReturnsTransformationMethod

import android.R.attr.password

import android.text.method.PasswordTransformationMethod

import android.R.attr.visible
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.android.synthetic.main.fragment_registre.*


class LogInFragment : Fragment() {


    private lateinit var drawerLayout: DrawerLayout

    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = DataBindingUtil.inflate<FragmentLogInBinding>(
            inflater,
            R.layout.fragment_log_in, container, false
        )

        binding.bRegistre.setOnClickListener { view: View ->

            Log.i("login Fragment", "Estem al listener boto registre")
            view.findNavController()
                .navigate(LogInFragmentDirections.actionLogInFragmentToRegistreFragment())


        }

        binding.bEntra.setOnClickListener { view: View ->

            if (textNom1.text.isNotEmpty() && contra.text.isNotEmpty()) {

                var letra: Char =
                    textNom1.text.toString().substring(textNom1.length() - 1, textNom1.length())
                        .get(0)

                var numeros: String = textNom1.text.toString().substring(0, textNom1.length() - 1)

                if (letra.isLetter() && numeros.isDigitsOnly() && letra.isUpperCase()) {


                    if (contra.length() == 4) {

                        val dni: String = textNom1.text.toString();
                        val contrasenya: String = contra.text.toString() + "prodis"

                        Log.d("LogInFragment", "dni:$dni")
                        db.collection("users").document(dni)
                            .get()
                            .addOnSuccessListener { result ->

                                if (result.exists()) {
                                    FirebaseAuth.getInstance()
                                        .signInWithEmailAndPassword(
                                            result.data?.get("dni") as String, //correu electronic
                                            contrasenya

                                        ).addOnCompleteListener() {
                                            if (it.isSuccessful) {
                                                view.findNavController()
                                                    .navigate(
                                                        LogInFragmentDirections.actionLogInFragmentToIniciFragment(
                                                            dni
                                                        )

                                                    )
                                            }
                                        }

                                } else {
                                    showAlert()
                                }
                            }


                    } else {
                        val toast = Toast.makeText(
                            requireContext(),
                            resources.getText(R.string.error_4digits),
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                } else {
                    val toast = Toast.makeText(requireContext(), "DNI no valid", Toast.LENGTH_LONG)
                    toast.show()
                }
            } else {
                val toast =
                    Toast.makeText(requireContext(), "Algun camp esta buit", Toast.LENGTH_LONG)
                toast.show()
            }

        }

        binding.oblidar.setOnClickListener { view: View ->
            Log.i("login Fragment", "Estem al listener boto oblidar")
            view.findNavController()
                .navigate(LogInFragmentDirections.actionLogInFragmentToOblidatContrasenyaFragment())
        }

        var esVisible = false


        return binding.root
    }

    private fun showAlert() {

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("Se ha produit un error al autenticar l´usuari")
        builder.setPositiveButton("Acceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    

}
