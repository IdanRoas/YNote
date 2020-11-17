package com.ir.ynote.ui.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ir.ynote.R
import kotlinx.android.synthetic.main.note_item.view.*

class HomeNotesAdapter(private val dataSet: ArrayList<String>) : RecyclerView.Adapter<HomeNotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.note_item, viewGroup, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
     holder.title.text= dataSet[position]
    }

    override fun getItemCount(): Int {
       return  dataSet.size
    }


    class  ViewHolder(view:View): RecyclerView.ViewHolder(view) {
        var title:TextView = view.home_note_title
    }

}
