package com.example.demochatapp

import android.app.AlertDialog
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        chatArray = ArrayList<String>();

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

                val name = mDialogView.chatTo.text.toString()
                Toast.makeText(applicationContext, name, Toast.LENGTH_LONG).show();
            }
            mDialogView.dialogCloseBtn.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }
    }
}
