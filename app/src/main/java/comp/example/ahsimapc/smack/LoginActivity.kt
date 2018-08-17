package comp.example.ahsimapc.smack

import android.app.Application
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        spinnerState(false)
    }
    fun signupHere(view:View)
    {


        val intent= Intent(this,SignupActivity::class.java)
        startActivity(intent)
    }


    fun loginHere(view: View)
    { spinnerState(true)
        val email=login_email.text.toString()
        val password=login_password.text.toString()
        hideKeyboard()
        if(email.isNotEmpty()&&password.isNotEmpty()) {
            AuthService.loginUser( email, password) { loginSuccess ->
                if (loginSuccess) {
                    AuthService.findUserByEmail(this) { userfound ->

                        if (userfound) {
                            spinnerState(false)
                            finish()
                        } else {
                            errorToast()
                        }
                    }
                } else {
                    errorToast()
                }


            }


        }else{
            Toast.makeText(this,"Make sure that name,email and password are filled in.",Toast.LENGTH_LONG)
                    .show()
            spinnerState(false)
        }

    }
    fun spinnerState(enable:Boolean)
    {if(enable)
    {

        login_spinner.visibility=View.VISIBLE

    }else
    {
        login_spinner.visibility=View.INVISIBLE
    }
        login_login_button.isEnabled=!enable
        login_signup_button.isEnabled=!enable


    }
    fun errorToast()
    {
        Toast.makeText(this,"Something went wrong please try again later", Toast.LENGTH_LONG)
                .show()
        spinnerState(false)
    }

    fun hideKeyboard()
    {

           val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
             if(inputManager.isAcceptingText){
           inputManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)

       }
    }
}
