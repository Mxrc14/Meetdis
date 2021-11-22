package cat.copernic.meetdis


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import cat.copernic.meetdis.R
import cat.copernic.meetdis.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    var visibleBottom: Boolean = false
    var visibleOptions: Boolean = false

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       Thread.sleep(3000)
        setTheme(R.style.AppTheme)
        @Suppress("UNUSED_VARIABLE")

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        //(true)

        //val menu_options: MaterialToolbar = binding.optionsMenu
        //navView= binding.bottomMenu


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment

        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomMenu,navController)

        setVisible(false)


        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.iniciFragment, R.id.buscarFragment, R.id.notificacioFragment,
                R.id.xatsFragment, R.id.perfilUsuariFragment
            )
        )


        appBarConfiguration.isVisible(flase)
        navController.isVisible(flase)

        //val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        //bottomNavigationView.setupWithNavController(navController)


        //navView.setupWithNavController(navController)
        // navView.setupActionBarWithNavController(navController, appBarConfiguration)




        //setupActionBarWithNavController(navController, appBarConfiguration)


    }



//    fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        super.onCreateOptionsMenu(menu)
//        inflater.inflate(R.menu.options_menu,menu)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,navController)
                ||super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.itemId){
//
//            R.id.personalitzacioFragment -> LogInFragmentDirections.actionLogInFragmentToRegistreFragment()
//            R.id.aboutFragment -> LogInFragmentDirections.actionLogInFragmentToRegistreFragment()
//            R.id.validacioUsuarisFragment -> LogInFragmentDirections.actionLogInFragmentToRegistreFragment()
//            R.id.->
//        }


//
//        return super.onOptionsItemSelected(item)
//    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }



}
