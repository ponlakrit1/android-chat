package com.example.demochatapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demochatapp.adapter.RecyclerAdapter
import com.example.demochatapp.adapter.RecyclerListener
import com.example.demochatapp.model.MessageModel
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MessagesActivity : AppCompatActivity(), RecyclerListener {

    lateinit var mSocket: Socket;
    var ROOT_URL = "http://192.168.39.148:3000";

    lateinit var prefs : SharedPreferences;
    lateinit var chatMsg: EditText;

    lateinit var messageList : ArrayList<MessageModel>;
    lateinit var memberAdapter : RecyclerAdapter;
    lateinit var recyclerView: RecyclerView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        chatMsg = findViewById<EditText>(R.id.message);

        // PREFS
        prefs = getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        val chatName = prefs.getString("chatRoom", "");
        val loginName = prefs.getString("username", "");

        // init adapter
        messageList = ArrayList<MessageModel>();
        memberAdapter = RecyclerAdapter(messageList, this)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = false
            adapter = memberAdapter
            onFlingListener = null
        }
        memberAdapter.notifyDataSetChanged()

        // Connect to socket.io
        try {
            mSocket = IO.socket(ROOT_URL);
            mSocket.connect();

            mSocket.on(chatName, Emitter.Listener { args ->

                val data = args[0] as JSONObject
                var gravityStatus : Boolean = false;
                try {
                    gravityStatus = data.getString("username") == loginName

                    messageList.add(MessageModel(data.getString("chatroom"), data.getString("username"), data.getString("message"), gravityStatus));
                    runOnUiThread {
                        memberAdapter.notifyDataSetChanged();
                    }
                } catch (e: JSONException) {
                    Log.e("SOCKET", "Socket message error")
                }

            });
        } catch (e: Exception){
            Log.e("SOCKET", "Socket init")
        }

        // Button send
        val send = findViewById<Button>(R.id.send)
        send.setOnClickListener {
            val txtChatMsg = chatMsg.text;
            System.out.println(txtChatMsg)

            var obj : JSONObject = JSONObject();
            obj.put("chatroom", chatName)
            obj.put("username", loginName)
            obj.put("message", txtChatMsg)
            mSocket.emit("message", obj);

//            chatMsg.text.clear();
        }
    }

    override fun onItemClick() {

    }
}
