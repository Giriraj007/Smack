package comp.example.ahsimapc.smack

import android.content.Context
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import comp.example.ahsimapc.smack.MessageService.array
import comp.example.ahsimapc.smack.utilities.BASE_URL
import comp.example.ahsimapc.smack.utilities.FIND_CHANNEL
import comp.example.ahsimapc.smack.utilities.FIND_MESSAGE
import org.json.JSONException
import org.json.JSONObject


object MessageService {

    val array = ArrayList<Channel>()
    val MessageArray=ArrayList<Message>()


    fun findChannel(context: Context, complete: (Boolean) -> Unit) {
        val findchannelRequest = object : JsonArrayRequest(Method.GET, FIND_CHANNEL, null, Response.Listener { response ->


            try {

                for (x in 0 until response.length()) {
                    val channel = response.getJSONObject(x)
                    val name = channel.getString("name")
                    val chandes = channel.getString("description")
                    val id = channel.getString("_id")
                    val newChannel = Channel(name, chandes, id)
                    array.add(newChannel)
                    complete(true)
                }
            } catch (e: JSONException) {
                println("Error findchannel:" + e.localizedMessage)
                complete(false)
            }
        },
                Response.ErrorListener { error ->
                    println(error.toString())
                    complete(false)
                }) {
            override fun getHeaders(): MutableMap<String, String> {

                val map = HashMap<String, String>()
                map.put("Authorization", "Bearer ${App.prefs.Auth_token}")
                return map
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }

  App.prefs.requestqueue.add(findchannelRequest)
    }



    fun findMessageForChannel(id:String,complete:(Boolean)->Unit) {
        clearMessages()
        val url = "$FIND_MESSAGE$id"

        val findMesaageRequest = object : JsonArrayRequest(Method.GET, url, null, Response.Listener {response->
            try {
                for(x in 0 until response.length())
                {  val message=response.getJSONObject(x)
                    val id=message.getString("_id")
                    val messagebody=message.getString("messageBody")
                    val channelId=message.getString("channelId")
                    val username=message.getString("userName")
                    val useravatar=message.getString("userAvatar")
                    val userColor=message.getString("userAvatar")
                    val timestamp=message.getString("timeStamp")
                    val msg=Message(messagebody,username,channelId,useravatar,userColor,id,timestamp)
                   this.MessageArray.add(msg)
                    complete(true)

                }


            } catch (e: JSONException) {
                println("Error in find Message" + e.localizedMessage)
                complete(false)
            }
        },

                Response.ErrorListener { error ->
                    println(error.toString())
                    complete(false)
                }){

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
            override fun getHeaders(): MutableMap<String, String> {

                val map = HashMap<String, String>()
                map.put("Authorization", "Bearer ${App.prefs.Auth_token}")
                return map
            }



        }
        App.prefs.requestqueue.add(findMesaageRequest)

    }


    fun clearChannel()
    {
        array.clear()
    }


    fun clearMessages()
    {

        MessageArray.clear()
    }
}