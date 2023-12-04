package com.valance.fiteat.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.valance.fiteat.R

class UserComponentsAdapter(private val dataList: List<UserComponentsData>)
    : RecyclerView.Adapter<UserComponentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_nutrition, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data1 = dataList[position]

        holder.line1TextView.text = data1.items
        holder.squirrelsTextView.text = data1.values
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val line1TextView: TextView = itemView.findViewById(R.id.line1)
        val squirrelsTextView: TextView = itemView.findViewById(R.id.Squirrels)
    }
}