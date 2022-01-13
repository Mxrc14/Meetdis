package cat.copernic.meetdis

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import cat.copernic.meetdis.databinding.FragmentPersonalitzacioBinding
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import java.util.*
import android.content.Intent
import android.content.res.Configuration


class Personalitzacio : Fragment(), AdapterView.OnItemSelectedListener {

    private var opcion: String? = null
    lateinit var dni: String

    private var locale: Locale? = null
    private val config: Configuration = Configuration()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentPersonalitzacioBinding>(
            inflater,
            R.layout.fragment_personalitzacio, container, false
        )
        ArrayAdapter.createFromResource(
            requireContext(), R.array.idiomas,

            R.layout.spinner_item
        ).also { adapter ->

            adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
            binding.spinnerIdioma.adapter = adapter
            //TODO documentar-lo en la memoria

        }

        setFragmentResultListener("dniKey") { dniKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported

            dni = bundle.getString("DNIKey").toString()
        }

        binding.bColor1.setOnClickListener { view: View ->

            MaterialColorPickerDialog
                .Builder(requireContext())            // Pass Activity Instance
                .setColors(                            // Pass Predefined Hex Color
                    arrayListOf(
                        "#f6e58d", "#ffbe76", "#ff7979", "#badc58", "#dff9fb",
                        "#7ed6df", "#e056fd", "#686de0", "#30336b", "#95afc0"
                    )
                )
                .setColorListener { color, colorHex ->
                    binding.bColor1.setBackgroundColor(color)
                }
                .show()
        }


        binding.bColor2.setOnClickListener { view: View ->

            MaterialColorPickerDialog
                .Builder(requireContext())            // Pass Activity Instance
                .setColors(                            // Pass Predefined Hex Color
                    arrayListOf(
                        "#f6e58d", "#ffbe76", "#ff7979", "#badc58", "#dff9fb",
                        "#7ed6df", "#e056fd", "#686de0", "#30336b", "#95afc0"
                    )
                )
                .setColorListener { color, colorHex ->
                    binding.bColor2.setBackgroundColor(color)
                }
                .show()

        }

        binding.btnColorSorpresa.setOnClickListener { view: View ->


        }

        binding.btnConfirmarPersonalitzacio.setOnClickListener { view: View ->


            when (opcion) {

                "Català", "Catalan", "Catalán"-> {

                    locale = Locale("ca");
                    config.locale = locale;

                }
                "Castellà", "Castellano", "Spanish" -> {

                    locale = Locale("es");
                    config.locale = locale;

                }

                else -> {

                    locale = Locale("en");
                    config.locale = locale;

                }
            }

            resources.updateConfiguration(config, null)
            val refresh = Intent(this.activity, MainActivity::class.java)
            startActivity(refresh)


            view.findNavController()
                .navigate(
                    PersonalitzacioDirections.actionPersonalitzacioFragmentToIniciFragment()
                )

        }

        binding.spinnerIdioma.onItemSelectedListener = this

        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        opcion = parent?.getItemAtPosition(position).toString()
        Log.i("Registre", "Opció: $opcion")
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}
