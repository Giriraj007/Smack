package comp.example.ahsimapc.smack

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_signup.*
import java.util.*

class SignupActivity : AppCompatActivity() {


    var useravatar="profiledefault"
    var avatarcolor="[0.5,0.5,0.5,1]"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
            signup_createuser.setOnClickListener(){

                val email=signup_email.text.toString()
                val password=signup_password.text.toString()
                AuthService.registerUser(this,email,password){registerSuccess->
                    if(registerSuccess)
                    {
                        //login
                    }

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
        avatarcolor="[$savered,$savegreen,$saveblue]"
    }
}
