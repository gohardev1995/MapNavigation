package com.invotech.runshuffle.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.invotech.runshuffle.Interfaces.ForgetPasswordAPI
import com.invotech.runshuffle.Models.ForgetUser
import com.invotech.runshuffle.Object.APIClient
import com.invotech.runshuffle.R
import kotlinx.android.synthetic.main.activity_forget_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        btn_send_email.setOnClickListener(View.OnClickListener {
            val ForgetPasswordAPI = APIClient.client.create(ForgetPasswordAPI::class.java)
            val callForgetPasswordApi = ForgetPasswordAPI.ForgetUser(edt_email.text.toString())
            callForgetPasswordApi.enqueue(object : Callback<ForgetUser>{
                override fun onFailure(call: Call<ForgetUser>, t: Throwable) {

                }

                override fun onResponse(call: Call<ForgetUser>, response: Response<ForgetUser>) {
                    Log.d("Gohar",response.code().toString())
                    if (response.code() == 200)
                    {
                        Toast.makeText(applicationContext,"Password Reset Confirmation is Sent to Your Email, Please Check your Email",Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@ForgetPasswordActivity,LoginActivity::class.java))

                    }
                    else
                        Toast.makeText(applicationContext,"Invalid Email",Toast.LENGTH_SHORT).show()
                }

            })
        })

    }
    private fun closeKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    fun closeKeyboard(view: View) {
        closeKeyboard()
    }
}