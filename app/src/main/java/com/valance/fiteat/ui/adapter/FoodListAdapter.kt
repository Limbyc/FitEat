package com.valance.fiteat.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.valance.fiteat.R
import com.valance.fiteat.db.entity.Meal
import java.util.Locale

class FoodListAdapter(private var meals: MutableList<Meal> = mutableListOf(),
                      private val onItemSelectedListener: (Int) -> Unit) :
    RecyclerView.Adapter<FoodListAdapter.MealViewHolder>() {

    private var filteredList: MutableList<Meal> = meals.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.food_list_elements, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = filteredList[position]
        holder.bind(meal)

        holder.itemView.setOnClickListener {
            onItemSelectedListener(meal.id)
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredResults = FilterResults()
                val query = constraint.toString().toLowerCase(Locale.ROOT).trim()

                filteredList = if (query.isEmpty()) {
                    meals.toMutableList()
                } else {
                    meals.filter { it.name.toLowerCase(Locale.ROOT).contains(query) }
                        .toMutableList()
                }

                filteredResults.values = filteredList
                return filteredResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.values is List<*>) {
                    @Suppress("UNCHECKED_CAST")
                    filteredList = results.values as MutableList<Meal>
                    notifyDataSetChanged()
                }
            }
        }
    }

    fun updateData(newMeals: List<Meal>) {
        meals.clear()
        meals.addAll(newMeals)
        filteredList.clear()
        filteredList.addAll(newMeals)
        notifyDataSetChanged()
    }

    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val mealNameTextView: TextView = itemView.findViewById(R.id.TVNameFood)
        private val mealCaloriesTextView: TextView = itemView.findViewById(R.id.Kalori)
        private val mealImageView: ImageView = itemView.findViewById(R.id.FitPhoto)
        private val plusEat: View = itemView.findViewById(R.id.plusEat)
        fun bind(meal: Meal) {
            mealNameTextView.text = meal.name
            mealCaloriesTextView.text = meal.calories.toString()
            if (meal.fit == 0) {
                mealImageView.setImageResource(R.drawable.apple)
            } else if (meal.fit == 1) {
                mealImageView.setImageResource(R.drawable.donat)
            }

        }
    }
}

