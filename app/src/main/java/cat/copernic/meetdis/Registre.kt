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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import cat.copernic.meetdis.databinding.ActivityMainBinding
import cat.copernic.meetdis.databinding.FragmentRegistreBinding
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


       //val adapter: ArrayAdapter<String> = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, R.array.tipus_Usuaris)

        ArrayAdapter.createFromResource(requireContext(), R.array.tipus_Usuaris,

            R.layout.spinner_item).also{ adapter->
            //R.layout.spinner_item
            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            binding.spinnerUsuaris.adapter = adapter
            //TODO documentar-lo en la memoria

        }


        binding.bContinuar.setOnClickListener { view: View ->

            if (textContrasenya.text.toString() == textRepeteixContrasenya.text.toString()) {
                Log.i("Registre", "Estem al listener boto Continuar: $opcion")
                val dni : String = textDNI.text.toString();
                val contrasenya: String = textContrasenya.text.toString()+"prodis";
                val tipus : String = opcion.toString();



                when (opcion) {

                    "Usuari" -> view.findNavController()
                        .navigate(RegistreDirections.actionRegistreFragmentToRegistreUsuariFragment(dni, contrasenya, tipus))

                    "Monitor" -> view.findNavController()
                        .navigate(RegistreDirections.actionRegistreFragmentToRegistreMonitorFragment(dni, contrasenya, tipus))

                    else -> view.findNavController()
                        .navigate(RegistreDirections.actionRegistreFragmentToRegistreFamiliarFragment(dni, contrasenya, tipus))
                }



                //Log.i("Registre", "dni:$dni textdni:${textDNI.text.toString()}")


            }
            else{
                 val toast = Toast.makeText(requireContext(), "Has introduit contrasenyas diferents", Toast.LENGTH_LONG)
                toast.show()
            }
        }




         binding.spinnerUsuaris.onItemSelectedListener = this
            return binding.root
    }






    // override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
     //   var countrySelected = llistat.getItem(position)
     //   mBinding.tvSelected.text = countrySelected

   // }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        opcion = parent?.getItemAtPosition(position).toString()
        Log.i("Registre", "Opció: $opcion")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}