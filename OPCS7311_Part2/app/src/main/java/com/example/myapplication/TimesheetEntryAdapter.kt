package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimesheetEntryAdapter(private val entries: List<TimesheetEntry>) :
    RecyclerView.Adapter<TimesheetEntryAdapter.EntryViewHolder>() {



    class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_all_work, parent, false)
        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = entries[position]


        val dateTextView = holder.itemView.findViewById<TextView>(R.id.dateTextView)
        val startTimeTextView = holder.itemView.findViewById<TextView>(R.id.startTimeTextView)
        val endTimeTextView = holder.itemView.findViewById<TextView>(R.id.endTimeTextView)
        val descriptionTextView = holder.itemView.findViewById<TextView>(R.id.descriptionTextView)
        val categoryTextView = holder.itemView.findViewById<TextView>(R.id.categoryTextView)

      //  dateTextView.text = "Date: ${entry.date}"
       // startTimeTextView.text = "Start Time: ${entry.startTime}"
     //   endTimeTextView.text = "End Time: ${entry.endTime}"
       // descriptionTextView.text = "Description: ${entry.description}"




    }

    override fun getItemCount() = entries.size
}

