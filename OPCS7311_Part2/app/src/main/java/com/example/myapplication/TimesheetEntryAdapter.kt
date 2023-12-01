package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TimesheetEntryAdapter(private val entries: List<TimesheetEntry>) :
    RecyclerView.Adapter<TimesheetEntryAdapter.EntryViewHolder>() {

    class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define your views inside the ViewHolder if needed
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val startTimeTextView: TextView = itemView.findViewById(R.id.startTimeTextView)
        val endTimeTextView: TextView = itemView.findViewById(R.id.endTimeTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_all_work, parent, false)
        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = entries[position]

        // Bind the data to the views inside the item's layout
        holder.categoryTextView.text = "Category: ${entry.categoryName}"
        holder.dateTextView.text = "Date: ${entry.date}"
        holder.startTimeTextView.text = "Start Time: ${entry.startTime}"
        holder.endTimeTextView.text = "End Time: ${entry.endTime}"
        holder.descriptionTextView.text = "Description: ${entry.description}"
    }

    override fun getItemCount() = entries.size
}


