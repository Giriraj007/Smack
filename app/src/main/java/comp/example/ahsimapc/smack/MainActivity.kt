package comp.example.ahsimapc.smack

import android.content.*
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import comp.example.ahsimapc.smack.utilities.BROADCAST_USER
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(BROADCAST_USER))




    }




    val receiver =object:BroadcastReceiver()
    {
        override fun onReceive(context: Context?, intent: Intent?) {
            if(AuthService.isloggedin) {
                username_nav_header.text = UserDataService.user_name
                email_nav_header.text = UserDataService.user_email
                val imageiD = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                profilepic_nav_header.setImageResource(imageiD)
                profilepic_nav_header.setBackgroundColor(UserDataService.getAvatarColor(UserDataService.avatarColor))
                login_button_nav_screen.text = "Logout"
            }
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }



    }





    fun loginUserClick(view:View)
    {  if(AuthService.isloggedin)
    {  UserDataService.loggedOut()
        email_nav_header.text=""
        username_nav_header.text=""
        profilepic_nav_header.setImageResource(R.drawable.profiledefault)
        profilepic_nav_header.setBackgroundColor(Color.TRANSPARENT)
        login_button_nav_screen.text="Login"

    }else {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    }

    fun addChannelClick(view: View)
    {
        if(AuthService.isloggedin)
        {
            val builder=AlertDialog.Builder(this)
            val view=layoutInflater.inflate(R.layout.channel_layoutt,null)
            builder.setView(view)
            builder.setPositiveButton("Add"){dialog: DialogInterface?, which: Int ->

                //
            }
            builder.setNegativeButton("Cancel"){dialog: DialogInterface?, which: Int ->
            }
            builder.show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }





}
