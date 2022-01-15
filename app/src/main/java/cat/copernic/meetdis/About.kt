package cat.copernic.meetdis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import cat.copernic.meetdis.databinding.FragmentAboutBinding
import com.github.dhaval2404.colorpicker.util.setVisibility
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class About : AppCompatActivity() {
    
    private lateinit var binding: FragmentAboutBinding

    private val aboutViewModel: aboutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)


        aboutViewModel.aboutModel.observe(this , Observer {
            binding.textView3.text = it.text1
            binding.textView5.text = it.text2
            binding.textView4.text = it.text3
        })
        aboutViewModel.getText()


    }


}

