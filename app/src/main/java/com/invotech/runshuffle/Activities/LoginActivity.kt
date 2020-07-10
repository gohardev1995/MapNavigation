package com.invotech.runshuffle.Activities

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.invotech.runshuffle.Interfaces.LoginAPI
import com.invotech.runshuffle.Models.LoginUser
import com.invotech.runshuffle.Object.APIClient
import com.invotech.runshuffle.Object.SaveSharedPreference
import com.invotech.runshuffle.R
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {


    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedEditor: SharedPreferences.Editor
    private var PREFS_NAME = "PrefsFile"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedEditor.putString("userName",edt_email.text.toString())
        sharedEditor.putString("password",edt_password.text.toString())



        if (SaveSharedPreference.getLoggedStatus(applicationContext)) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        } else {
            loginForm.setVisibility(View.VISIBLE)
        }

        //----------------------Adding Shared Preference to save Credentials----------------------//

       /* sharedPreferences = getSharedPreferences(fileName, MODE_PRIVATE)
        val loginPrefsEditor = loginPreferences.edit()
        val saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            edt_email.setText(loginPreferences.getString("username", ""))
            edt_password.setText(loginPreferences.getString("password", ""))
            chk_remember.setChecked(true)
        }*/
        //--------------------</Adding Shared Preference to save Credentials/>--------------------//
        //----------------------Adding OnClick Listerns to Buttons--------------------------------//
        txt_create_account.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        })
        txt_forgot_password.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        })


        //--------------------</Adding OnClick Listerns to Buttons/>------------------------------//
        //-----------------------Calling LogIn POST RETROFIT API----------------------------------//
        btn_signin.setOnClickListener(View.OnClickListener {
            val LoginAPI = APIClient.client.create(LoginAPI::class.java)
            val callLoginApi = LoginAPI.createUser(
                edt_email.text.toString(),
                edt_password.text.toString()
            )
            callLoginApi.enqueue(object : Callback<LoginUser> {
                override fun onFailure(call: Call<LoginUser>, t: Throwable) {

                }

                override fun onResponse(call: Call<LoginUser>, response: Response<LoginUser>) {
                    Log.d("Gohar", response.code().toString())

                    if (response.code() == 200 && chk_remember.isChecked) {

                        val isChecked = chk_remember.isChecked
                        sharedEditor = sharedPreferences.edit()
                        sharedEditor.putString("pref_name",edt_email.text.toString())
                        sharedEditor.putString("pref_pass",edt_password.text.toString())
                        sharedEditor.putBoolean("pref_check",isChecked)
                        sharedEditor.apply()

                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
                        Toast.makeText(applicationContext, "Succesfully Logged In ", Toast.LENGTH_SHORT).show()

                        startActivity(intent)
                        finish()

                        SaveSharedPreference.setLoggedIn(
                            applicationContext,
                            true,
                            edt_email.text.toString(),
                            edt_password.text.toString()
                        )
                    } else
                        Toast.makeText(
                            applicationContext,
                            "Invalid Credentials Or Confirm the CheckBox",
                            Toast.LENGTH_SHORT
                        ).show()

                }

            })



            /*doSomethingElse()*/
        })

    }

    private fun doSomethingElse() {

        startActivity(Intent(this@LoginActivity, MainActivity::class.java))

    }

    //---------------------</Calling LogIn POST RETROFIT API/>------------------------------------//
    //----------------------------------- Function to Hide Keyboard-------------------------------//
    fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun getclearText(view: View) {
        closeKeyboard()
    }
    //-----------------------------------</Function to Hide Keyboard/>----------------------------//

}