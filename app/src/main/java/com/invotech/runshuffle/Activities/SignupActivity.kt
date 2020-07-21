package com.invotech.runshuffle.Activities

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.MarkerOptions
import com.invotech.runshuffle.Interfaces.RegisterAPI
import com.invotech.runshuffle.Models.RegisterUser
import com.invotech.runshuffle.Object.APIClient
import com.invotech.runshuffle.R
import kotlinx.android.synthetic.main.activity_signup.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


@Suppress("DEPRECATION")
class SignupActivity : AppCompatActivity() {
     private lateinit var sharedPreferences : SharedPreferences
    private lateinit var SharedEditor :SharedPreferences.Editor
    private var myPreference = "myPrefs"
    private var email = "email"
    private var password = "password"
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
            if (  TextUtils.isEmpty(edt_name.text.toString()) || TextUtils.isEmpty(edt_email.text.toString()) || TextUtils.isEmpty(edt_password.text.toString()) || TextUtils.isEmpty(edt_c_password.text.toString()))
            {
                if(TextUtils.isEmpty(edt_name.text.toString()))
                {
                    Toast.makeText(applicationContext,"Please Enter Your Name",Toast.LENGTH_SHORT).show()
                }
                else if(TextUtils.isEmpty(edt_email.text.toString()))
                {
                    Toast.makeText(applicationContext,"Please Enter Your Email",Toast.LENGTH_SHORT).show()
                }
                else if(TextUtils.isEmpty(edt_password.text.toString()))
                {
                    Toast.makeText(applicationContext,"Please Enter Your Password",Toast.LENGTH_SHORT).show()
                }
                else if(TextUtils.isEmpty(edt_c_password.text.toString()))
                {
                    Toast.makeText(applicationContext,"Please Re-Enter Your Password",Toast.LENGTH_SHORT).show()
                }
                else if(!chk_conscent.isChecked)
                {
                    Toast.makeText(applicationContext,"Please Confirm the CheckBox",Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(applicationContext,"Please Enter All Credentials ",Toast.LENGTH_SHORT).show()



            }
            else
            {
                val RegisterApi = APIClient.client.create(RegisterAPI::class.java)
                val callRegisterApi = RegisterApi.RegisterUser(
                    edt_name.text.toString(),
                    edt_email.text.toString(),
                    edt_password.text.toString()
                )
                callRegisterApi.enqueue(object : Callback<RegisterUser> {
                    override fun onFailure(call: Call<RegisterUser>, t: Throwable) {
                        Toast.makeText(applicationContext,"This Email Has been Registered Already",Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(call: Call<RegisterUser>, response: Response<RegisterUser>) {
                        Log.d("Gohar",response.code().toString())
                        if (response.code() == 401 )
                        {
                            Toast.makeText(applicationContext,"This email is Already Registered",Toast.LENGTH_SHORT).show()
                        }
                        else
                        {
                            val dialog =
                                ProgressDialog.show(
                                    this@SignupActivity, "",
                                    "Loading. Please wait...", true
                                )
                            val buttonTimer = Timer()
                            val progressDialog =
                                ProgressDialog(this@SignupActivity)
                            buttonTimer.schedule(object : TimerTask() {
                                override fun run() {
                                    runOnUiThread {


                                       
                                        Toast.makeText(applicationContext,"Succesffuly Registered",Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this@SignupActivity,MainActivity::class.java))




                                    }
                                    dialog.dismiss();
                                }
                            }, 4000)


                        }

                    }

                })
            }


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