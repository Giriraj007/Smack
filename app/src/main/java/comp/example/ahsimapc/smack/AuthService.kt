package comp.example.ahsimapc.smack

import android.content.Context
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import comp.example.ahsimapc.smack.utilities.ADD_USER
import comp.example.ahsimapc.smack.utilities.AUTH_LOGIN_USER
import comp.example.ahsimapc.smack.utilities.AUTH_REGISTER_USER
import org.json.JSONException
import org.json.JSONObject

object AuthService {
   var  Auth_email=""
    var Auth_password=""
    var Auth_token=""
    var isloggedin=false


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

             println(response.toString())
             complete(true)

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



    fun loginUser(context: Context,email:String,password:String,complete:(Boolean)->Unit)
    {var jsonstring=""

           try {
               val jsonbody = JSONObject()
               jsonbody.put("email", email)
               jsonbody.put("password", password)
               jsonstring  = jsonbody.toString()

           }catch (e:JSONException)
           {
               print("Error:"+e.localizedMessage)
           }

        val loginrequest=object :JsonObjectRequest(Method.POST, AUTH_LOGIN_USER,null,Response.Listener { response ->
         try {
             Auth_email=response.getString("user")
             Auth_token=response.getString("token")
             isloggedin=true
             complete(true)

         } catch (e:JSONException)
         {
             println("Error:"+e.localizedMessage)

         }

        }, Response.ErrorListener { error->
            println(error)
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return jsonstring.toByteArray()
            }


        }
        Volley.newRequestQueue(context).add(loginrequest)
    }

    fun addUser(context: Context,username:String,email:String,avatarname:String,avatarColor:String,complete:(Boolean)->Unit)
    {
        val jsonbody=JSONObject()
        jsonbody.put("name",username)
        jsonbody.put("email",email)
        jsonbody.put("avatarName",avatarname)
        jsonbody.put("avatarColor",avatarColor)
        val jsonString=jsonbody.toString()

        val addUserRequest=object:JsonObjectRequest(Method.POST, ADD_USER,null,Response.Listener { response->
            UserDataService.avatarColor=response.getString("avatarColor")
            UserDataService.user_email=response.getString("email")
            UserDataService.user_id=response.getString("_id")
            UserDataService.user_name=response.getString("name")
            println(response)
            complete(true)

        },
                Response.ErrorListener { error->
                    println("adduser"+error)
                    complete(false)
                }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return jsonString.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
               val headers=HashMap<String,String>()
                headers.put("Authorization","Bearer ${Auth_token}")
                return headers
            }

        }

    Volley.newRequestQueue(context).add(addUserRequest)
    }




}