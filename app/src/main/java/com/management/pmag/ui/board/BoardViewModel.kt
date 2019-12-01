package com.management.pmag.ui.board

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BoardViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Agile Board"
    }
    val text: LiveData<String> = _text
}