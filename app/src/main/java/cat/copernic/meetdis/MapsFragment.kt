package cat.copernic.meetdis

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import cat.copernic.meetdis.databinding.FragmentCrearOfertaBinding
import cat.copernic.meetdis.databinding.FragmentMaps2Binding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_maps2.*
import com.google.android.gms.maps.GoogleMap.OnMapClickListener
import kotlinx.android.synthetic.main.fragment_contacte.*


class MapsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

    }


     val callback = OnMapReadyCallback { googleMap ->
         /**
          * Manipulates the map once available.
          * This callback is triggered when the map is ready to be used.
          * This is where we can add markers or lines, add listeners or move the camera.
          * In this case, we just add a marker near Sydney, Australia.
          * If Google Play services is not installed on the device, the user will be prompted to
          * install it inside the SupportMapFragment. This method will only be triggered once the
          * user has installed Google Play services and returned to the app.
          */
         val terrassa = LatLng(41.56316375007589, 2.008833765210524)
         googleMap.addMarker(MarkerOptions().position(terrassa).title("Marker in Terrassa"))

         googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(terrassa, 15f))
         // Zoom in, animating the camera.
         googleMap.animateCamera(CameraUpdateFactory.zoomIn())
         // Zoom out to zoom level 10, animating with a duration of 2 seconds.
         googleMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)

         googleMap.setOnMapClickListener { latLng -> // Creating a marker
             val markerOptions = MarkerOptions()

             // Setting the position for the marker
             markerOptions.position(latLng)

             // Setting the title for the marker.
             // This will be displayed on taping the marker
             markerOptions.title(latLng.latitude.toString() + " : " + latLng.longitude)

             // Clears the previously touched position
             googleMap.clear()

             // Animating to the touched position
             googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))

             // Placing a marker on the touched position
             googleMap.addMarker(markerOptions)

         }


     }
}