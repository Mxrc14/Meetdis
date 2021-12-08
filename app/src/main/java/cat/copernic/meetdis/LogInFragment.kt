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
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.github.dhaval2404.colorpicker.util.setVisibility
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlinx.android.synthetic.main.fragment_registre.*
import kotlinx.coroutines.*


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

        var tasca1: Job? = null


        binding.bEntra.setOnClickListener { view: View ->

            tasca1 = crearCorrutina(
                5, //Temps de durada de la corrutina 1
                binding.bEntra, //Botó per activar la corrutina 1
                binding.progressBarUn, //Progrés de la corrutina 1
            )


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







    private fun crearCorrutina(durada: Int, inici: Button,progres: ProgressBar) = GlobalScope.launch(

        Dispatchers.Main) {

        progres.setVisibility(true)
        progres.progress = 0 //Situem el ProgressBar a l'inici del procés

        /* Tasca principal:
         * Trasalladem l'execucció de la corrutina a un procés diferent mitjançant el mètode withContext. En aquest cas a un procés d'entrada i sortida que bloquejarà
         * el procés principla fins que rebi una resposta, fent que la nostra funció sigui segura i habiliti l'IU segons sigui necessari.
         * En el nostre cas activarem el progressBar fins arribar al final.
         */
        withContext(Dispatchers.IO) {
            var comptador = 0
            //Inciem el progrés de la barra de progrés
            while (comptador < durada) {
                //Retardem el progrés de la barra
                if(suspensio((durada * 50).toLong())) {
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


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
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
