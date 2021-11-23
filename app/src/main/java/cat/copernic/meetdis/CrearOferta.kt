package cat.copernic.meetdis

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.view.SupportActionModeWrapper
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import cat.copernic.meetdis.databinding.FragmentCrearOfertaBinding
import cat.copernic.meetdis.databinding.FragmentRegistreBinding
import com.bumptech.glide.manager.SupportRequestManagerFragment
import com.google.android.gms.dynamic.SupportFragmentWrapper
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_crear_oferta.*
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
 * Use the [CrearOferta.newInstance] factory method to
 * create an instance of this fragment.
 */
class CrearOferta : Fragment(), AdapterView.OnItemSelectedListener {
    private val db = FirebaseFirestore.getInstance()
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var spinner: Spinner? = null
    private var opcion: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentCrearOfertaBinding>(inflater,
            R.layout.fragment_crear_oferta,container,false)
        ArrayAdapter.createFromResource(requireContext(), R.array.tipus_Events
            ,

            R.layout.spinner_item).also { adapter ->

            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            binding.spinnerEvents.adapter = adapter

        }


        val args = CrearOfertaArgs.fromBundle(requireArguments())



        binding.bCrear.setOnClickListener { view: View ->

            if (textTitol.text.isNotEmpty() && descripcio.text.isNotEmpty()) {

                val dni: String = args.dni;


                view.findNavController()
                    .navigate(CrearOfertaDirections.actionCrearOfertaFragmentToIniciFragment(dni))

                db.collection("events").document(textTitol.text.toString()).set(
                    hashMapOf(
                        "dni" to args.dni,
                        "titol" to textTitol.text.toString(),
                        "descripcio" to descripcio.text.toString(),
                         "data" to textData.text.toString(),
                        "tipus" to opcion.toString()
                    )
                )
            }else{
                val toast = Toast.makeText(requireContext(), "Algun camp esta buit", Toast.LENGTH_LONG)
                toast.show()
            }

        }

      binding.textData.setOnClickListener{ showDatePickerDialog()}


        binding.spinnerEvents.onItemSelectedListener = this
        return binding.root


    }

    private fun showDatePickerDialog() {
        val datePicker = DatePickerFragment{day, month, year -> onDateSelected(day, month, year)}

        activity?.let { datePicker.show(it.supportFragmentManager, "Date Picker") }



    }

    fun onDateSelected(day:Int, month: Int, year:Int){
        textData.setText("$day/$month/$year")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        opcion = parent?.getItemAtPosition(position).toString()
        Log.i("Registre", "Opció: $opcion")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CrearOferta.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CrearOferta().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}