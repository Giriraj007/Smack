package comp.example.ahsimapc.smack

import android.content.Context
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import comp.example.ahsimapc.smack.MessageService.array
import comp.example.ahsimapc.smack.utilities.FIND_CHANNEL
import org.json.JSONException
import org.json.JSONObject


object MessageService {

    val array = ArrayList<Channel>()


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
                map.put("Authorization", "Bearer ${AuthService.Auth_token}")
                return map
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }
        }

  Volley.newRequestQueue(context).add(findchannelRequest)
    }
}