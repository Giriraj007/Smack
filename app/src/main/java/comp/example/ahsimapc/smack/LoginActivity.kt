package comp.example.ahsimapc.smack

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
    fun signupHere(view:View)
    {


        val intent= Intent(this,SignupActivity::class.java)
        startActivity(intent)
    }
}
