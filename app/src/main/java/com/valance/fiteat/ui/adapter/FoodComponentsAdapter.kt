package com.valance.fiteat.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.valance.fiteat.R

class FoodComponentsAdapter(private var dataList: List<FoodComponentsData>) : RecyclerView.Adapter<FoodComponentsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_nutrition, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]

        holder.line1TextView.text = data.items
        holder.squirrelsTextView.text = data.values
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(newData: List<FoodComponentsData>) {
        dataList = newData
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val line1TextView: TextView = itemView.findViewById(R.id.line1)
        val squirrelsTextView: TextView = itemView.findViewById(R.id.Squirrels)
    }

}