package com.invotech.runshuffle.Activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.invotech.runshuffle.Interfaces.RegisterAPI
import com.invotech.runshuffle.Models.RegisterUser
import com.invotech.runshuffle.Object.APIClient
import com.invotech.runshuffle.R
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //---------------------------Adding OnClick Listerners to Buttons-------------------------//
        txt_login.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )
        })


        txt_forgot_password.setOnClickListener(View.OnClickListener {
            startActivity(
                Intent(
                    this,
                    ForgetPasswordActivity::class.java
                )
            )
        })
        //-------------------------</Adding OnClick Listerners to Buttons/>-----------------------//

        //---------------------------------Calling POST API RETROFIT------------------------------//
        btn_signup.setOnClickListener(View.OnClickListener {

            val RegisterApi = APIClient.client.create(RegisterAPI::class.java)
            val callRegisterApi = RegisterApi.RegisterUser(
                edt_name.text.toString(),
                edt_email.text.toString(),
                edt_password.text.toString()
            )
            callRegisterApi.enqueue(object : Callback<RegisterUser> {
                override fun onFailure(call: Call<RegisterUser>, t: Throwable) {
                    Toast.makeText(applicationContext,"Registration Failed",Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<RegisterUser>, response: Response<RegisterUser>) {
                    startActivity(Intent(this@SignupActivity,LoginActivity::class.java))
                    Toast.makeText(applicationContext,"Succesfully Registered",Toast.LENGTH_LONG).show()

                }

            })
        })
        //-------------------------------</Calling POST API RETROFIT/>----------------------------//

    }

    //----------------------------------- Function to Hide Keyboard-------------------------------//
    fun getclearText(view: View) {
        closeKeyboard()
    }

    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
    //-----------------------------------</Function to Hide Keyboard/>------------------------------//

}