package com.example.demochatapp

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.google.gson.GsonBuilder
import org.json.JSONArray
import org.json.JSONObject
import java.lang.String.format


class MainActivity : AppCompatActivity() {

    var ROOT_URL = "http://192.168.39.148:3000";

    private var username: EditText? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        username = findViewById<EditText>(R.id.username);
    }

    fun onLogin(view: View?) {
        val txtUsername = username?.text;
        var txtTest : JSONObject? = null;

        if (txtUsername.toString() !== "") {
            val jsonObjectRequest = JsonArrayRequest(Request.Method.GET, ROOT_URL + "/user/"+txtUsername, null,
                Response.Listener<JSONArray> { response ->
                    Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT).show();
                },
                Response.ErrorListener { error ->
                    Toast.makeText(applicationContext, "$error", Toast.LENGTH_LONG).show();
                }
            )

            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        }
    }
}
