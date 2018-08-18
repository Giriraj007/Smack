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
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import comp.example.ahsimapc.smack.utilities.BROADCAST_USER
import comp.example.ahsimapc.smack.utilities.SOCKEt_URL
import io.socket.client.IO
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(){
    var selectedchannel:Channel?=null
    val socket= IO.socket(SOCKEt_URL)
    lateinit var channel_Adapter:ArrayAdapter<Channel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        socket.connect()
        socket.on("channelCreated",emmiter)
        socket.on("messageCreated",mesaageEmitter)
        channel_list.setOnItemClickListener { parent, view, position, id ->

            selectedchannel = MessageService.array[position]
            drawer_layout.closeDrawer(GravityCompat.START)
            updateWithChannel()
        }
            send_button.setOnClickListener {
                if(App.prefs.isloggedin &&message_textview.text.isNotEmpty() && selectedchannel!=null)
                {

                    val channelid=selectedchannel!!.id
                    val userid=UserDataService.user_id
                    socket.emit("newMessage",message_textview.text.toString(),userid,channelid,UserDataService.user_name,
                            UserDataService.avatarName,UserDataService.avatarColor)
                    hideKeyboard()
                    message_textview.text.clear()

                }
            }




        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(BROADCAST_USER))
        setAdapter()
        if(App.prefs.isloggedin)
        {
            AuthService.findUserByEmail(this){}
        }







    }
    fun setAdapter()
    {
        channel_Adapter=ArrayAdapter(this,android.R.layout.simple_list_item_1,MessageService.array)
        channel_list.adapter=channel_Adapter
    }
    val  emmiter=Emitter.Listener { arg->
     if(App.prefs.isloggedin) {
         runOnUiThread {
             val channel_name = arg[0] as String
             val channel_description = arg[1] as String
             val channel_id = arg[2] as String
             val channel = Channel(channel_name, channel_description, channel_id)
             MessageService.array.add(channel)
             channel_Adapter.notifyDataSetChanged()
         }
     }
    }
    val mesaageEmitter=Emitter.Listener { args ->
        if(App.prefs.isloggedin) {
            runOnUiThread {
                val channelId = args[2] as String
                if(channelId==selectedchannel!!.id) {
                    val messageBody = args[0] as String
                    val username = args[3] as String
                    val avatarName = args[4] as String
                    val avatarColor = args[5] as String
                    val id = args[6] as String
                    val timeSpan = args[7] as String
                    val message = Message(messageBody, username, channelId, avatarName, avatarColor, id, timeSpan)
                    MessageService.MessageArray.add(message)
                }


            }
        }
    }








    val receiver =object:BroadcastReceiver()
    {
        override fun onReceive(context: Context, intent: Intent?) {
            if(App.prefs.isloggedin) {
                username_nav_header.text = UserDataService.user_name
                email_nav_header.text = UserDataService.user_email
                val imageiD = resources.getIdentifier(UserDataService.avatarName, "drawable", packageName)
                profilepic_nav_header.setImageResource(imageiD)
                profilepic_nav_header.setBackgroundColor(UserDataService.getAvatarColor(UserDataService.avatarColor))
                login_button_nav_screen.text = "Logout"
                MessageService.findChannel(context){complete->
                    if(complete)
                    { if(MessageService.array.count()>0)
                    {  selectedchannel=MessageService.array[0]

                        channel_Adapter.notifyDataSetChanged()
                        updateWithChannel()
                    }


                    }


                }


            }
        }
    }

    fun updateWithChannel()
    {
        channel_name.text=selectedchannel?.name
        if(selectedchannel!=null)
        {
        MessageService.findMessageForChannel(selectedchannel!!.id) { complete ->
            if (complete) {
                for (message in MessageService.MessageArray) {
                    println(message.message)

                }
            }
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
    {  if(App.prefs.isloggedin)
    {  UserDataService.loggedOut()
        email_nav_header.text=""
        username_nav_header.text=""
        profilepic_nav_header.setImageResource(R.drawable.profiledefault)
        profilepic_nav_header.setBackgroundColor(Color.TRANSPARENT)
        login_button_nav_screen.text="Login"
        channel_Adapter.notifyDataSetChanged()

    }else {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    }

    override fun onResume() {
        super.onResume()
       // LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(BROADCAST_USER))


    }

    fun addChannelClick(view: View)
    {
        if(App.prefs.isloggedin)
        {
            val builder=AlertDialog.Builder(this)
            val view=layoutInflater.inflate(R.layout.channel_layoutt,null)
            builder.setView(view)
            builder.setPositiveButton("Add"){dialog: DialogInterface?, which: Int ->
                val channel_name=view.findViewById<EditText>(R.id.dialogue_channelname)
                val channel_description=view.findViewById<EditText>(R.id.dialog_channeldescription)
                socket.emit("newChannel",channel_name.text,channel_description.text)


            }
            builder.setNegativeButton("Cancel"){dialog: DialogInterface?, which: Int ->
            }
            builder.show()

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
        socket.disconnect()
    }
    fun hideKeyboard()
    {

        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if(inputManager.isAcceptingText){
            inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)

        }
    }
}






