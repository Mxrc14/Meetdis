package cat.copernic.meetdis


import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import cat.copernic.meetdis.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var visibleBottom: Boolean = false
    var visibleOptions: Boolean = false

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration


    val user = FirebaseAuth.getInstance().currentUser

    val uid = user?.email


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        setTheme(R.style.AppTheme)
        @Suppress("UNUSED_VARIABLE")

        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment

        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomMenu, navController)

        val args = Bundle()

        binding.bottomMenu.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.iniciFragment, R.id.buscarFragment, R.id.notificacioFragment,
                R.id.xatsFragment, R.id.perfilUsuariFragment
            )
        )

        sesion()

    }

    fun sesion() {

        if (user != null) {

            var dni: String = uid.toString().substring(0, uid.toString().length - 11)

            Log.i("MainActivityxd", "${dni.uppercase()}")

            this.navController.navigate(
                LogInFragmentDirections.actionLogInFragmentToIniciFragment(
                )
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /*return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item)*/

        return when (item.itemId) {
            R.id.logInFragment -> {
                FirebaseAuth.getInstance().signOut()
                NavigationUI.onNavDestinationSelected(item, navController)
                true
            }
            else -> NavigationUI.onNavDestinationSelected(item, navController)
                    || super.onOptionsItemSelected(item)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {

        val navController = Navigation.findNavController(this, R.id.myNavHostFragment)
        NavigationUI.setupWithNavController(bottomMenu, navController)
        return navController.navigateUp(appBarConfiguration)

    }




//    @RequiresApi(Build.VERSION_CODES.P)
//    fun cambiarColor(color: Int) {
//
//        var colorD: String = Color.parseColor(color.toString())
//
//        window.navigationBarDividerColor = color
//
//        window.navigationBarColor = color
//        supportActionBar?.setBackgroundDrawable(Color.parseColor(colorD))
//
//        this.window
//
//
//    }


}
