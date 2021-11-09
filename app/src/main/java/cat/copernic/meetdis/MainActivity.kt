package cat.copernic.meetdis


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import cat.copernic.meetdis.R
import cat.copernic.meetdis.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    var visibleBottom: Boolean = false
    var visibleOptions: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       Thread.sleep(3000)
        setTheme(R.style.AppTheme)
        @Suppress("UNUSED_VARIABLE")

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        drawerLayout = binding.drawerLayout
    }


    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return onCreateOptionsMenu(menu)
    }*/

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.mainActivityFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

    /*override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val findItem = menu?.findItem(R.id.bottom)
        findItem?.isVisible = visibleBottom

        val findItem2 = menu?.findItem(R.id.bottom)

        findItem2?.isVisible = visibleOptions
        return true
    }

     */

}
