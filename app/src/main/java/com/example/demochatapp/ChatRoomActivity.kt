package com.example.demochatapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.chat_room_dialog.view.*
import java.util.*

class ChatRoomActivity : AppCompatActivity() {

    lateinit var lvMessage : ListView;

    private var chatArray: ArrayList<String>? = null
    private var chatAdapter: ArrayAdapter<String>? = null

    lateinit var prefs : SharedPreferences;
    lateinit var editor : SharedPreferences.Editor;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        chatArray = ArrayList<String>();
        prefs = getSharedPreferences("UserPref", Context.MODE_PRIVATE);

        chatAdapter = ArrayAdapter<String>(applicationContext, android.R.layout.simple_list_item_1, chatArray);
        lvMessage = findViewById<ListView>(R.id.lvMessage);
        lvMessage.setAdapter(chatAdapter)

        val mFab = findViewById<FloatingActionButton>(R.id.fabAddChat)
        mFab.setOnClickListener {
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.chat_room_dialog, null)
            val mBuilder = AlertDialog.Builder(this).setView(mDialogView).setTitle("CHAT TO")
            val mAlertDialog = mBuilder.show()

            mDialogView.dialogChatBtn.setOnClickListener {
                mAlertDialog.dismiss()

                val loginName = prefs.getString("username", "");
                val nameTxt = mDialogView.chatTo.text.toString()

                editor = prefs.edit();
                editor.putString("chatRoom", loginName+"_"+nameTxt);
                editor.apply();

                val intent = Intent(this, MessagesActivity::class.java);
                startActivity(intent);

//                Toast.makeText(applicationContext, loginName+"_"+nameTxt, Toast.LENGTH_LONG).show();
            }
            mDialogView.dialogCloseBtn.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }
}
