package comp.example.ahsimapc.smack

import android.content.Context
import com.android.volley.toolbox.Volley

class SharedPrefs(context: Context) {
    private val FILE_NAME="prefs"
    val prefs=context.getSharedPreferences(FILE_NAME,0)

    val IS_LOGED_IN="islogedin"
    val AUTH_EMAIL="authemail"
    val AUTH_TOKEN="authtoken"


    var isloggedin:Boolean
    get() = prefs.getBoolean(IS_LOGED_IN,false)
    set(value) = prefs.edit().putBoolean(IS_LOGED_IN,value).apply()

    var  Auth_email:String
    get() = prefs.getString(AUTH_EMAIL,"")
    set(value) =prefs.edit().putString(AUTH_EMAIL,value).apply()

    var Auth_token:String
    get() = prefs.getString(AUTH_TOKEN,"")
    set(value)=prefs.edit().putString(AUTH_TOKEN,value).apply()

    val requestqueue=Volley.newRequestQueue(context)
}