package com.ir.ynote.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.ir.ynote.R
import com.ir.ynote.location.LocationViewModel
import com.ir.ynote.ui.main.home.HomeNotesAdapter
import com.ir.ynote.ui.main.map.MapsFragment
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment() {

    private var list= ArrayList<String>()

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var homeViewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //startLocationUpdate()
        public_notes_recyclerView.apply {
            adapter=HomeNotesAdapter(list)
        }

        add_new_note_button.setOnClickListener{
            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_container,MapsFragment.newInstance())?.commit()

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        // TODO: Use the ViewModel
    }

 /*private   private fun startLocationUpdate() {
        locationViewModel.getLocationData().observe(this, Observer {
          public_note_title.text=it.latitude.toString()+","+it.latitude
        })
    }*/



}