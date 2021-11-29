package cat.copernic.meetdis

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.ui.navigateUp
import cat.copernic.meetdis.databinding.FragmentPersonalitzacioBinding
import cat.copernic.meetdis.databinding.FragmentRegistreBinding
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Personalitzacio.newInstance] factory method to
 * create an instance of this fragment.
 */
class Personalitzacio : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var opcion: String? = null


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


        }

        binding.bColor1.setOnClickListener { view: View ->

            MaterialColorPickerDialog
                .Builder(requireContext())        			// Pass Activity Instance
                .setColors(							// Pass Predefined Hex Color
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
                .Builder(requireContext())        			// Pass Activity Instance
                .setColors(							// Pass Predefined Hex Color
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




        return binding.root
    }

        fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            opcion = parent?.getItemAtPosition(position).toString()
            Log.i("Registre", "Opció: $opcion")
        }

        fun onNothingSelected(p0: AdapterView<*>?) {
            TODO("Not yet implemented")
        }

    }
