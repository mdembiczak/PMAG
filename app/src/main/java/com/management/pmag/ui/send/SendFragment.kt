package com.management.pmag.ui.send

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore
import com.management.pmag.R

class SendFragment : Fragment() {

    private lateinit var sendViewModel: SendViewModel
    lateinit var button: Button
    var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sendViewModel =
            ViewModelProviders.of(this).get(SendViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_send, container, false)
        val textView: TextView = root.findViewById(R.id.text_send)

        val projectName: TextView = root.findViewById(R.id.projectNameId)
        button = root.findViewById(R.id.button)

        button.setOnClickListener {
            val hashMapOf = hashMapOf<String, String>(
                "first" to projectName.text.toString()
            )
            firestore.collection("sendFragment")
                .add(hashMapOf)
                .addOnSuccessListener { Log.d(TAG, it.id) }
                .addOnFailureListener { Log.e(TAG, it.toString()) }
        }

        sendViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }


}