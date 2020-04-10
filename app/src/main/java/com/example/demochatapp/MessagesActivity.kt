package com.example.demochatapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.example.demochatapp.adapter.RecyclerAdapter
import com.example.demochatapp.adapter.RecyclerListener
import com.example.demochatapp.model.MessageModel
import com.github.nkzawa.emitter.Emitter
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class MessagesActivity : AppCompatActivity(), RecyclerListener {

    lateinit var mSocket: Socket;
    var ROOT_URL = "http://192.168.39.148:3000";

    lateinit var prefs : SharedPreferences;
    lateinit var chatMsg: EditText;

    lateinit var messageList : ArrayList<MessageModel>;
    lateinit var messageAdapter : RecyclerAdapter;
    lateinit var recyclerView: RecyclerView;

    lateinit var messageNo : String;
    lateinit var chatNo : String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        // PREFS
        prefs = getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        val chatName = prefs.getString("chatRoom", "");
        val loginName = prefs.getString("username", "");
        val chatTo = prefs.getString("chatTo", "");
        messageNo = prefs.getString("messageNo", "");
        chatNo = prefs.getString("chatNo", "");

        title = chatTo;
        chatMsg = findViewById<EditText>(R.id.message);

        // init adapter
        messageList = ArrayList<MessageModel>();
        this.loadMessageData();
        messageAdapter = RecyclerAdapter(messageList, this)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView).apply {
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = false
            adapter = messageAdapter
            onFlingListener = null
        }
        messageAdapter.notifyDataSetChanged()
        recyclerView.scrollToPosition(messageAdapter.itemCount - 1);

        // Connect to socket.io
        try {
            mSocket = IO.socket(ROOT_URL);
            mSocket.connect();

            mSocket.on(chatName, Emitter.Listener { args ->

                val data = args[0] as JSONObject
                var gravityStatus : Boolean = false;
                try {
                    gravityStatus = data.getString("username") == loginName

                    keepMessageData(MessageModel(data.getString("chatroom"), data.getString("username"), data.getString("message"), gravityStatus));
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

            chatMsg.setText("");
        }
    }

    override fun onItemClick() {

    }

    fun loadMessageData(){
        val gson = Gson();

        // get data from API
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, "$ROOT_URL/chatroom/msg/$chatNo/$messageNo", null,
            Response.Listener<JSONArray> { response ->

                if(response.length() != 0){
                    val obj : JSONObject = response.getJSONObject(0);
                    val messageData = obj.getString("message_data");

                    if(messageData != ""){
                        val completedMsg = messageData.substring(1, messageData.length - 1);
                        val datarList: List<MessageModel> = gson.fromJson(completedMsg , Array<MessageModel>::class.java).toList();
                        for (i in datarList.indices) {
                            messageList.add(datarList[i]);
                        }
                    }
                }

                System.out.println("msg size = "+messageList.size)

                messageAdapter.notifyDataSetChanged()
                recyclerView.scrollToPosition(messageAdapter.itemCount - 1);

            },
            Response.ErrorListener { error ->
                Log.e("chatRoom", "$error");
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }

    private fun keepMessageData(data : MessageModel){
        runOnUiThread {
            messageList.add(data);
            messageAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(messageAdapter.itemCount - 1);

            val params = HashMap<String,String>()
            val gson = Gson();
            params["message_no"] = messageNo
            params["chat_no"] = chatNo
            params["message_recent"] = data.message
            params["message_data"] = gson.toJson(messageList)
            val jsonObject = JSONObject(params)

            // send data to API
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.POST, "$ROOT_URL/chatroom/msg", jsonObject,
                Response.Listener { response ->
                    Log.d("message", "send completed");
                },
                Response.ErrorListener { error ->
                    Log.e("message", "$error");
                }
            )
            MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }
}
