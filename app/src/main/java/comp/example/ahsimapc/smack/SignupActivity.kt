package comp.example.ahsimapc.smack

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Toast
import comp.example.ahsimapc.smack.utilities.BROADCAST_USER
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

class SignupActivity : AppCompatActivity() {


    var useravatar="profiledefault"
    var avatarcolor="[0.5,0.5,0.5,1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        spinner.visibility=View.INVISIBLE
            signup_createuser.setOnClickListener() {
                spinnerState(true)
                val email = signup_email.text.toString()
                val password = signup_password.text.toString()
                val user_name = signupusername.text.toString()
                if (email.isNotEmpty() && password.isNotEmpty() && user_name.isNotEmpty()) {
                    AuthService.registerUser( email, password) { registerSuccess ->
                        if (registerSuccess) {
                            AuthService.loginUser( email, password) { loginSuccess ->
                                if (loginSuccess) {
                                    AuthService.addUser( user_name, email, useravatar, avatarcolor) { addUserSuccess ->
                                        if (addUserSuccess) {
                                            spinnerState(false)
                                            val intentt = Intent(BROADCAST_USER)
                                            val broadcast = LocalBroadcastManager.getInstance(this)
                                                    .sendBroadcast(intentt)
                                            finish()
                                        } else {
                                            errorToast()
                                        }


                                    }
                                } else {
                                    errorToast()
                                }

                            }
                        } else {
                            errorToast()
                        }

                    }

                }else
                {
                    Toast.makeText(this,"Make sure that name,email and password are filled in.",Toast.LENGTH_LONG)
                            .show()
                    spinnerState(false)
                }
            }
    }

    fun changeImage(view: View)
     { val random=Random()
         val imageChoice=random.nextInt(2)
         val backgroundChoice=random.nextInt(28)
         if(imageChoice==0)
         { useravatar="light$backgroundChoice"

         }else
         {
             useravatar="dark$backgroundChoice"
         }
         val imageid=resources.getIdentifier(useravatar,"drawable",packageName)
         signup_userimage.setImageResource(imageid)

     }



    fun generatebackgroundcolor(view: View)
    {
      val random=Random()
        val red=random.nextInt(255)
        val green =random.nextInt(255)
        val blue=random.nextInt(255)
        signup_userimage.setBackgroundColor(Color.rgb(red,green,blue))


        val savered=red.toDouble()/255
        val savegreen=green.toDouble()/255
        val saveblue=blue.toDouble()/255
        avatarcolor="[$savered,$savegreen,$saveblue,1]"
    }
    fun spinnerState(enable:Boolean)
     {if(enable)
     {

         spinner.visibility=View.VISIBLE

     }else
     {
         spinner.visibility=View.INVISIBLE
     }
         signup_createuser.isEnabled=!enable
         signup_userimage.isEnabled=!enable
         signup_generate_button.isEnabled=!enable

    }
    fun errorToast()
    {
        Toast.makeText(this,"Something went wrong please try again later",Toast.LENGTH_LONG)
                .show()
        spinnerState(false)
    }
}
