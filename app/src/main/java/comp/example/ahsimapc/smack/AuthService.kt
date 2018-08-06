package comp.example.ahsimapc.smack

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import comp.example.ahsimapc.smack.utilities.AUTH_REGISTER_USER
import org.json.JSONException
import org.json.JSONObject

object AuthService {
   var  Auth_email=""
    var Auth_password=""


    fun registerUser(context: Context, email:String, password:String,complete:(Boolean)->Unit)
     {  var stringjson=""

         try {


         val jsoonbody = JSONObject()
         jsoonbody.put("email", email)
         jsoonbody.put("password", password)
          stringjson = jsoonbody.toString()
     }catch (e:JSONException)
     {
         println("Error:"+e.localizedMessage)
     }


         val registerrequest=object : StringRequest(Method.POST, AUTH_REGISTER_USER, Response.Listener {response ->
             println(response)

         },
                 Response.ErrorListener { error->
                     println(error)
                     complete(false)
                 }){
             override fun getBodyContentType(): String {
                 return "application/json; charset=utf-8"
             }

             override fun getBody(): ByteArray {
                 return stringjson.toByteArray()
             }
         }
         Volley.newRequestQueue(context).add(registerrequest)

    }


}