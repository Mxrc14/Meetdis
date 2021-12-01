package cat.copernic.meetdis

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.core.text.isDigitsOnly
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import cat.copernic.meetdis.databinding.ActivityMainBinding
import cat.copernic.meetdis.databinding.FragmentRegistreBinding
import com.github.dhaval2404.colorpicker.util.setVisibility
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_registre.*


private lateinit var mBinding: ActivityMainBinding
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Registre.newInstance] factory method to
 * create an instance of this fragment.
 */
class Registre : Fragment(), AdapterView.OnItemSelectedListener{

    private var spinner: Spinner? = null
    private var opcion: String? = null

     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentRegistreBinding>(inflater,
        R.layout.fragment_registre,container,false)


        ArrayAdapter.createFromResource(requireContext(), R.array.tipus_Usuaris,

            R.layout.spinner_item).also{ adapter->

            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            binding.spinnerUsuaris.adapter = adapter
            //TODO documentar-lo en la memoria

        }


        binding.bContinuar.setOnClickListener { view: View ->

            //TODO Falta comprobar si el DNI ja esta registrat d´aquesta manera no li permetra continuar amb el registre

            if (textDNI.text.isNotEmpty() && textContrasenya.text.isNotEmpty() && textRepeteixContrasenya.text.isNotEmpty()) {

                var letra: Char = textDNI.text.toString().substring(textDNI.length() - 1, textDNI.length()).get(0)

                var numeros: String = textDNI.text.toString().substring(0, textDNI.length() - 1)

                if (letra.isLetter() && numeros.isDigitsOnly() && letra.isUpperCase() && textDNI.text.length == 9) {


                    if (textContrasenya.text.toString() == textRepeteixContrasenya.text.toString()) {

                        if (textContrasenya.length() == 4 && textRepeteixContrasenya.length() == 4) {


                            val dni: String = textDNI.text.toString()
                            val contrasenya: String = textContrasenya.text.toString() + "prodis"
                            val tipus: String = opcion.toString();


                            when (opcion) {

                                "Usuari" -> view.findNavController()
                                    .navigate(
                                        RegistreDirections.actionRegistreFragmentToRegistreUsuariFragment(
                                            dni,
                                            tipus,
                                            contrasenya
                                        )
                                    )

                                "Monitor" -> view.findNavController()
                                    .navigate(
                                        RegistreDirections.actionRegistreFragmentToRegistreMonitorFragment(
                                            dni,
                                            tipus,
                                            contrasenya
                                        )
                                    )

                                else -> view.findNavController()
                                    .navigate(
                                        RegistreDirections.actionRegistreFragmentToRegistreFamiliarFragment(
                                            dni,
                                            tipus,
                                            contrasenya
                                        )
                                    )
                            }

                        } else {
                            val toast = Toast.makeText(
                                requireContext(),
                                "La contrasenya te que constar de 4 numeros.",
                                Toast.LENGTH_LONG
                            )
                            toast.show()
                        }

                    } else {
                        val toast = Toast.makeText(
                            requireContext(),
                            "Has introduit contrasenyas diferents",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                } else {
                    val toast = Toast.makeText(requireContext(), "DNI no valid", Toast.LENGTH_LONG)
                    toast.show()
                }
            }else{
                val toast = Toast.makeText(requireContext(), "Algun camp esta buit", Toast.LENGTH_LONG)
                toast.show()
            }
        }
         binding.spinnerUsuaris.onItemSelectedListener = this
            return binding.root
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        opcion = parent?.getItemAtPosition(position).toString()
        Log.i("Registre", "Opció: $opcion")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
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