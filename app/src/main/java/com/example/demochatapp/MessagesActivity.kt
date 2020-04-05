package com.example.demochatapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException
import java.util.ArrayList


class MessagesActivity : AppCompatActivity() {

    lateinit var mSocket: Socket;
    var ROOT_URL = "http://192.168.39.148:3000";

    private var chatArray: ArrayList<String>? = null

    lateinit var prefs : SharedPreferences;
    private var chatMsg: EditText? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        chatMsg = findViewById<EditText>(R.id.message);

        chatArray = ArrayList<String>();
        prefs = getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        val chatName = prefs.getString("chatRoom", "");
        val loginName = prefs.getString("username", "");

        // Connect to socket.io
        try {
            mSocket = IO.socket(ROOT_URL);
            mSocket.connect();
        } catch (e: Exception){
            Log.e("SOCKET", "Socket init")
        }

        val send = findViewById<Button>(R.id.send)
        send.setOnClickListener {
            val txtChatMsg = chatMsg?.text;

            var obj : JSONObject = JSONObject();
            obj.put("chatroom", chatName)
            obj.put("username", loginName)
            obj.put("message", txtChatMsg)
            mSocket.emit("message", obj);

            Toast.makeText(this, "send msg", Toast.LENGTH_LONG).show();
        }
    }

}
