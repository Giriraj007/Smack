package comp.example.ahsimapc.smack

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import comp.example.ahsimapc.smack.utilities.*
import org.json.JSONException
import org.json.JSONObject

object AuthService {
//   var  Auth_email=""
//    var Auth_password=""
//    var Auth_token=""
//    var isloggedin=false


    fun registerUser( email:String, password:String,complete:(Boolean)->Unit)
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
         App.prefs.requestqueue.add(registerrequest)

    }



    fun loginUser(email:String,password:String,complete:(Boolean)->Unit)
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
            App.prefs.Auth_email=response.getString("user")
             App.prefs.Auth_token=response.getString("token")
             App.prefs. isloggedin=true
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
        App.prefs.requestqueue.add(loginrequest)
    }

    fun addUser(username:String,email:String,avatarname:String,avatarColor:String,complete:(Boolean)->Unit)
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
            UserDataService.avatarName=response.getString("avatarName")
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
                headers.put("Authorization","Bearer ${App.prefs.Auth_token}")
                return headers
            }

        }

        App.prefs.requestqueue.add(addUserRequest)
    }


    fun findUserByEmail(context: Context,complete:(Boolean)->Unit)
    {

        val finduser=object :JsonObjectRequest(Method.GET, "${FIND_USER_BY_EMAIL}${App.prefs.Auth_email}",null,
                Response.Listener {response->
                    UserDataService.user_id=response.getString("_id")
                    UserDataService.avatarName=response.getString("avatarName")
                    UserDataService.avatarColor=response.getString("avatarColor")
                    UserDataService.user_email=response.getString("email")
                    UserDataService.user_name=response.getString("name")
                    val intent= Intent(BROADCAST_USER)
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
                    complete(true)
                    try {

                    }catch (e:JSONException)
                    {
                        println(e.localizedMessage)
                        complete(false)
                    }
                },Response.ErrorListener {error ->
            println("find User Error:"+error.toString())

        })
        {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }



            override fun getHeaders(): MutableMap<String, String> {
                val headers=HashMap<String,String>()
                headers.put("Authorization","Bearer ${App.prefs.Auth_token}")
                return headers
            }


        }
        App.prefs.requestqueue.add(finduser)




    }




}