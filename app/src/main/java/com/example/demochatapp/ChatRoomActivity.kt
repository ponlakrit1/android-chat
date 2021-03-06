package com.example.demochatapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.example.demochatapp.adapter.ListViewAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.chat_room_dialog.view.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

class ChatRoomActivity : AppCompatActivity() {

    var ROOT_URL = "http://192.168.39.148:3000";

    lateinit var lvMessage : ListView;

    lateinit var chatArray: ArrayList<String>;
    lateinit var recentArray: ArrayList<String>;
    lateinit var chatAdapter: ArrayAdapter<String>;

    lateinit var prefs : SharedPreferences;
    lateinit var editor : SharedPreferences.Editor;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        chatArray = ArrayList<String>();
        recentArray = ArrayList<String>();
        prefs = getSharedPreferences("UserPref", Context.MODE_PRIVATE);
        val loginName = prefs.getString("username", "");

        // get data from API
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, "$ROOT_URL/chatroom/$loginName", null,
            Response.Listener<JSONArray> { response ->
                for (i in 0 until response.length()) {
                    val item : JSONObject  = response.getJSONObject(i);

                    if(item.getString("chat_from") == loginName){
                        chatArray.add(item.getString("chat_to"));
                    } else {
                        chatArray.add(item.getString("chat_from"));
                    }

                    recentArray.add(item.getString("message_recent"));
                }

                chatAdapter.notifyDataSetChanged();
            },
            Response.ErrorListener { error ->
                Log.e("chatRoom", "$error");
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        // Listview
//        chatAdapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1, chatArray);
        chatAdapter = ListViewAdapter(this, chatArray, recentArray)
        lvMessage = findViewById<ListView>(R.id.lvMessage);
        lvMessage.adapter = chatAdapter
        lvMessage.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val itemValue = lvMessage.getItemAtPosition(position) as String

            editor = prefs.edit();
            editor.putString("chatRoom", loginName+"_"+itemValue);
            editor.putString("chatTo", itemValue);
            editor.apply();

            getChatRoomFromWebService(loginName, itemValue);
        }

        // FAB button
        val mFab = findViewById<FloatingActionButton>(R.id.fabAddChat)
        mFab.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.chat_room_dialog, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle("CHAT TO")
            val mAlertDialog = mBuilder.show()

            mDialogView.dialogChatBtn.setOnClickListener {
                mAlertDialog.dismiss()
                val nameTxt = mDialogView.chatTo.text.toString()

                editor = prefs.edit();
                editor.putString("chatRoom", loginName+"_"+nameTxt);
                editor.putString("chatTo", nameTxt);
                editor.apply();

                getChatRoomFromWebService(loginName, nameTxt);
            }
            mDialogView.dialogCloseBtn.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }

    private fun getChatRoomFromWebService(loginName : String, nameTxt : String){
        // get data from API
        val jsonObjectRequest = JsonArrayRequest(
            Request.Method.GET, "$ROOT_URL/chatroom/$loginName/$nameTxt", null,
            Response.Listener<JSONArray> { response ->

                if(response.length() != 0){
                    var obj : JSONObject = response.getJSONObject(0);

                    editor = prefs.edit();
                    editor.putString("chatNo", obj.getString("chat_no"));
                    editor.putString("messageNo", obj.getString("message_no"));
                    editor.apply();
                }

                val intent = Intent(this, MessagesActivity::class.java);
                startActivity(intent);
            },
            Response.ErrorListener { error ->
                Log.e("chatRoom", "$error");
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}
