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
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import cat.copernic.meetdis.databinding.ActivityMainBinding
import cat.copernic.meetdis.databinding.FragmentIniciBinding
import cat.copernic.meetdis.databinding.FragmentRegistreBinding
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_registre.view.*
import java.util.*


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
class Registre : Fragment(), AdapterView.OnItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var spinner: Spinner? = null
    private lateinit var mBinding: ActivityMainBinding
    private lateinit var llistat: ArrayAdapter<String>





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(layoutInflater)

        //setContentView(R.layout.fragment_registre, )

        //llistat = ArrayAdapter<String>( this,
          //  android.R.layout.simple_spinner_dropdown_item)

        //llistat.addAll(Arrays.asList("Selecciona","Mexico", "Españita", "Perú", "Argentina"))


        //mBinding.drawerLayout.spinnerUsuaris.onItemSelectedListener = this
        //mBinding.drawerLayout.spinnerUsuaris.adapter = llistat


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val binding = DataBindingUtil.inflate<FragmentRegistreBinding>(inflater,
        R.layout.fragment_registre,container,false)



        /*binding.bRegistre.setOnClickListener { view: View ->

            Log.i("login Fragment", "Estem al listener boto registre")
            view.findNavController()
                .navigate(LogInFragmentDirections.actionLogInFragmentToRegistreFragment())



        }
*/

            return binding.root
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("Not yet implemented")
    }


    // override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
     //   var countrySelected = llistat.getItem(position)
     //   mBinding.tvSelected.text = countrySelected

   // }

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
         * @return A new instance of fragment Registre.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Registre().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}