package com.invotech.runshuffle.Activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.MarkerOptions
import com.invotech.runshuffle.Interfaces.LoginAPI
import com.invotech.runshuffle.Models.LoginUser
import com.invotech.runshuffle.Object.APIClient
import com.invotech.runshuffle.Object.SaveSharedPreference
import com.invotech.runshuffle.R
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var SharedEditor : SharedPreferences.Editor
    private var myPreference = "myPrefs"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        sharedPreferences = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        getPreferencesData()

        if (SaveSharedPreference.getLoggedStatus(applicationContext)) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        } else {
            loginForm.setVisibility(View.VISIBLE)
        }

        //----------------------Adding Shared Preference to save Credentials----------------------//

        /*val loginPreferences = getSharedPreferences("prefsName", MODE_PRIVATE)
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

                @SuppressLint("CommitPrefEdits")
                override fun onResponse(call: Call<LoginUser>, response: Response<LoginUser>) {
                    Log.d("Gohar", response.code().toString())
                    Log.e("Gohar",response.code().toString())
                    if (response.code() == 200) {
                        if(chk_remember.isChecked)
                        {

                            val boolisChecked = chk_remember.isChecked
                            SharedEditor = sharedPreferences.edit()
                            SharedEditor.putString("pref_email",edt_email.text.toString())
                            SharedEditor.putString("pref_pass",edt_password.text.toString())
                            SharedEditor.putBoolean("pref_check",boolisChecked)
                            SaveSharedPreference.setLoggedIn(
                                applicationContext,
                                true
                            )
                            SharedEditor.apply()


                        }
                        else
                            sharedPreferences.edit().clear().apply()

                        val dialog =
                            ProgressDialog.show(
                                this@LoginActivity, "",
                                "Loading. Please wait...", true
                            )
                        val buttonTimer = Timer()
                        val progressDialog =
                            ProgressDialog(this@LoginActivity)

                        buttonTimer.schedule(object : TimerTask() {
                            override fun run() {
                                runOnUiThread {


                                    /*progressDialog.setTitle("ProgressDialog");
                                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                                    progressDialog.window
                                    progressDialog.max = 100;
                                    progressDialog.max;
                                    progressDialog.show()*/
                                    val intent = Intent(applicationContext,MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
                                    Toast.makeText(applicationContext, "Succesfully Logged In ", Toast.LENGTH_SHORT).show()

                                    startActivity(intent)
                                    finish()




                                }
                                dialog.dismiss();
                            }
                        }, 4000)
                        /*intent.putExtra("email",edt_email.text.toString())
                        intent.putExtra("password",edt_password.text.toString())

                        val username = intent.getStringExtra("email")
                        val password = intent.getStringExtra("password")

                        edt_email.setText(username)
                        edt_password.setText(password)
                        Log.d("gohar",username)
                        Log.d("gohar",password)*/




                    } else
                        Toast.makeText(
                            applicationContext,
                            "Invalid Credentials",
                            Toast.LENGTH_SHORT
                        ).show()

                }

            })



            /*doSomethingElse()*/
        })

    }

    private fun getPreferencesData() {
        if (sharedPreferences.contains("pref_email") && sharedPreferences.contains("pref_pass") && sharedPreferences.contains("pref_check")) {
            val useremail = sharedPreferences.getString("pref_email", "not found")
            edt_email.setText(useremail)
            val userpass = sharedPreferences.getString("pref_pass", "not found")
            edt_password.setText(userpass)
            val b = sharedPreferences.getBoolean("pref_check", false)
            chk_remember.isChecked = b

        }




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