package com.example.demochatapp.ui.body

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BodyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is body Fragment"
    }
    val text: LiveData<String> = _text
}