package cat.copernic.meetdis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.setFragmentResultListener
import cat.copernic.meetdis.databinding.FragmentCrearOfertaBinding
import cat.copernic.meetdis.databinding.FragmentOfertaBinding
import com.github.dhaval2404.colorpicker.util.setVisibility


class ContingutOferta() : Fragment() {
    lateinit var binding: FragmentOfertaBinding
    lateinit var dni: String
    lateinit var dniC: String

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<FragmentOfertaBinding>(
                inflater,
                R.layout.fragment_crear_oferta, container, false
        )

        var lat: Double? = null
        var lon: Double? = null

        if (lat == 0.0 && lon == 0.0) {
            binding.telematicText.setVisibility(true)
        } else {
            binding.mapView.setVisibility(true)
        }
        setFragmentResultListener("dniKey") { dniKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            dni = bundle.getString("DNIKey").toString()
        }

        if (dniC == dni) {
            binding.bModificar.setVisibility(true)
            binding.bEliminar.setVisibility(true)
        } else {
            binding.bInscriuMe.setVisibility(true)
        }


        return binding.root
    }


}