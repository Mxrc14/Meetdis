package cat.copernic.meetdis

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import cat.copernic.meetdis.databinding.FragmentContingutOfertaBinding

class ValidacioUsuaris : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentContingutOfertaBinding>(
            inflater,
            R.layout.fragment_contingut_oferta, container, false
        )

        return binding.root
    }


}