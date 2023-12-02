package com.example.myapplication
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.TimesheetEntry

class TimesheetEntryAdapter :
    ListAdapter<TimesheetEntry, TimesheetEntryAdapter.EntryViewHolder>(TimesheetEntryDiffCallback()) {

    class EntryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define your views inside the ViewHolder if needed
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val startTimeTextView: TextView = itemView.findViewById(R.id.startTimeTextView)
        val endTimeTextView: TextView = itemView.findViewById(R.id.endTimeTextView)
        val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
        val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_timesheet_entry, parent, false)
        return EntryViewHolder(view)
    }

    override fun onBindViewHolder(holder: EntryViewHolder, position: Int) {
        val entry = getItem(position)

        // Bind the data to the views inside the item's layout
        holder.categoryTextView.text = "Category: ${entry.categoryName}"
        holder.dateTextView.text = "Date: ${entry.date}"
        holder.startTimeTextView.text = "Start Time: ${entry.startTime}"
        holder.endTimeTextView.text = "End Time: ${entry.endTime}"
        holder.descriptionTextView.text = "Description: ${entry.description}"
    }
}

class TimesheetEntryDiffCallback : DiffUtil.ItemCallback<TimesheetEntry>() {
    override fun areItemsTheSame(oldItem: TimesheetEntry, newItem: TimesheetEntry): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TimesheetEntry, newItem: TimesheetEntry): Boolean {
        return oldItem == newItem
    }
}
