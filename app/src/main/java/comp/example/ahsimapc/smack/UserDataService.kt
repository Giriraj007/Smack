package comp.example.ahsimapc.smack

import android.graphics.Color
import java.util.*

object UserDataService {


    var avatarColor=""
    var user_email=""
    var user_name=""
    var user_id=""
    var avatarName=""


    fun loggedOut()
    { avatarColor=""
         user_email=""
         user_name=""
         user_id=""
         avatarName=""
        App.prefs.Auth_email=""
        //AuthService.Auth_password=""
        App.prefs.Auth_token=""
        App.prefs.isloggedin=false
        MessageService.array.clear()


    }


    fun getAvatarColor(component:String):Int
    {
        val strippedstring=component.replace("["," ")
                .replace("]"," ")
                .replace(","," ")
        var r=0
        var g=0
        var b=0



        val scanner=Scanner(strippedstring)
         if(scanner.hasNext())
         {
             r=(scanner.nextDouble()*255).toInt()
             g=(scanner.nextDouble()*255).toInt()
             b=(scanner.nextDouble()*255).toInt()
         }

      return Color.rgb(r,g,b)
    }


}