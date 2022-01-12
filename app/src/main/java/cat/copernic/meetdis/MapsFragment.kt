package cat.copernic.meetdis


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import cat.copernic.meetdis.databinding.FragmentMaps2Binding
import com.github.dhaval2404.colorpicker.util.setVisibility
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment() {

    private lateinit var binding: FragmentMaps2Binding
    var dni: String? = null
    var lat: Double? = 0.0
    var lon: Double? = 0.0

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setFragmentResultListener("dniKey") { dniKey, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            dni = bundle.getString("DNIKey").toString()
        }

        binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_maps2, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)


    }


    val callback = OnMapReadyCallback { googleMap ->
        val terrassa = LatLng(41.56316375007589, 2.008833765210524)
        googleMap.addMarker(MarkerOptions().position(terrassa).title("Marker in Terrassa"))

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(terrassa, 15f), 2000, null)

        googleMap.setOnMapClickListener { latLng -> // Creating a marker
            val markerOptions = MarkerOptions()
            binding.OKButton.setVisibility(true)

            // Establint la posicio al marker.
            markerOptions.position(latLng)

            // Realitzacio del marker amb la latitut i longitud
            markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)

            // Eliminacio del marker anterior
            googleMap.clear()

            // Animacio a la posicio seleccionada
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

            // Realitzant un marker en la nova localitzacio
            googleMap.addMarker(markerOptions)

           lat = latLng .latitude
           lon = latLng.longitude

        }

        binding.OKButton.setOnClickListener { latLng -> // Creating a marker

            // Pasar les variables de latitud i longitud
            setFragmentResult("latKey", bundleOf("bundleKey1" to lat))

            setFragmentResult("lonKey", bundleOf("bundleKey2" to lon))

            Log.i("MapsFragment", "$lat  $lon")

            Log.i("MapsFragment", "$dni")


            view?.findNavController()
                    ?.navigate(MapsFragmentDirections.actionMapsFragmentToCrearOfertaFragment(dni!!))

        }

    }
}