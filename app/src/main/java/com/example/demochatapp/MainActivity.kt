package com.example.demochatapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    var ROOT_URL = "http://192.168.39.148:3000";

    private var username: EditText? = null;

    lateinit var prefs : SharedPreferences;
    lateinit var editor : SharedPreferences.Editor;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = findViewById<EditText>(R.id.username);
    }

    fun onLogin(view: View?) {
        val txtUsername = username?.text;
        var txtTest : JSONObject? = null;

        if (txtUsername.toString() !== "") {

            // get data from API
            val jsonObjectRequest = JsonArrayRequest(Request.Method.GET,
                "$ROOT_URL/user/$txtUsername", null,
                Response.Listener<JSONArray> { response ->

                    prefs = getSharedPreferences("UserPref", Context.MODE_PRIVATE);

                    if(response.length() != 0){
                        var obj : JSONObject = response.getJSONObject(0);

                        editor = prefs.edit();
                        editor.putString("username", obj.getString("USERNAME"));
                        editor.apply();
                    }

//                    Toast.makeText(applicationContext, prefs.getString("username", ""), Toast.LENGTH_SHORT).show();

                    val intent = Intent(this, ChatRoomActivity::class.java);
                    startActivity(intent);
                },
                Response.ErrorListener { error ->
                    Toast.makeText(applicationContext, "$error", Toast.LENGTH_LONG).show();
                }
            )

            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        } else {
            Toast.makeText(applicationContext, "Error type a text pls", Toast.LENGTH_LONG).show();
        }
    }
}
