package com.nenasa.Walkthrough

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.nenasa.R

class Walkthrough1 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.walkthrough1, container, false)

//        view.walkthrough1_btn.setOnClickListener {
//            (activity as Walkthrough?)?.openLogin()
//        }

        // Inflate the layout for this fragment
        return view
    }
}