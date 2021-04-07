package com.example.expanse.ui.MonthList

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.expanse.R
import com.example.expanse.data.Expense
import kotlinx.android.synthetic.main.monthly_list_item.view.*


class MonthAdapter(private val children: List<Expense>) :
    RecyclerView.Adapter<MonthAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.monthly_list_item, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return children.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val child = children[position]
        holder.itemName.text = child.name
        if (child.amount < 0) {
            holder.itemAmount.text = child.amount.toString()
            holder.itemAmount.setTextColor(Color.RED)
        } else {
            holder.itemAmount.text = "+" + child.amount.toString()
            holder.itemAmount.setTextColor(Color.GREEN)
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemName: TextView = itemView.item_name
        val itemAmount: TextView = itemView.item_price
    }
}

