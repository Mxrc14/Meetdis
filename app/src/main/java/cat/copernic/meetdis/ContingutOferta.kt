package cat.copernic.meetdis


import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import cat.copernic.meetdis.databinding.FragmentContingutOfertaBinding
import android.content.Intent
import cat.copernic.meetdis.MainActivity as MainActivity1


class ContingutOferta : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentContingutOfertaBinding>(
            inflater,
            R.layout.fragment_contingut_oferta, container, false
        )

        //val intent: Intent = requireActivity().getIntent()
        //val bundle = intent.extras

//        var titol = bundle!!.getSerializable("ofertaTitol")
//        var descripcio = bundle!!.getSerializable("ofertaDesc")
//        var data = bundle!!.getSerializable("ofertaData")
//        var dni = bundle!!.getSerializable("ofertaDNI")
//        var img = bundle!!.getSerializable("ofertaImg")
//        var lat = bundle!!.getSerializable("ofertaLat")
//        var lon = bundle!!.getSerializable("ofertaLon")





        //val args = BuscarArgs.fromBundle(requireArguments())


        return binding.root
    }

}