package com.example.demochatapp.ui.body

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.demochatapp.R

class BodyFragment : Fragment() {

    private lateinit var notificationsViewModel: BodyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(BodyViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_body, container, false)
        val textView: TextView = root.findViewById(R.id.text_body)
        notificationsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
