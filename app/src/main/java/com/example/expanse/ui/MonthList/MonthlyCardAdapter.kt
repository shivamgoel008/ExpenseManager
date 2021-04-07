package com.example.expanse.ui

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.expanse.R
import com.example.expanse.data.MonthlyTransactions
import com.example.expanse.ui.MonthList.MonthAdapter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.fragment_monthly_list.*

class MonthlyCardAdapter(private val listener: (Long) -> Unit, val context: Context) :
    ListAdapter<MonthlyTransactions, MonthlyCardAdapter.ViewHolder>(
        DiffCallback2()
    ) {

    private var viewPool: RecyclerView.RecycledViewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val itemLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_monthly_list, parent, false)

        return ViewHolder(itemLayout, context)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override val containerView: View, val context: Context) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        init {
            itemView.setOnClickListener {
                listener.invoke(getItem(adapterPosition).monthYear)
            }

        }


        @SuppressLint("WrongConstant")
        fun bind(monthlyTransactions: MonthlyTransactions) {
//            val sharedPreferences: SharedPreferences =
//                this.context.getSharedPreferences("Preference", Context.MODE_PRIVATE)
//            var monthlyBudget = sharedPreferences.getFloat("Budget", 0f)
            with(monthlyTransactions) {
                month_name.text = selectMonth(monthlyTransactions.month)
                year_name.text = " " + monthlyTransactions.year.toString()
//                Log.d("MonthlyCard", "aug: " + monthlyTransactions.sum + " " + monthlyBudget)
//                if ((monthlyTransactions.sum * (-1)) > monthlyBudget) {
////                    budget_exceeded.text = "Budget Exceeded"
////                    budget_exceeded.error = "Budget Exceeded"
//                } else {
////                    budget_exceeded.text = ""
////                    budget_exceeded.error = null
//                }

                val childLayoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
                childLayoutManager.initialPrefetchItemCount = 4
                recyclerView2.apply {
                    layoutManager = childLayoutManager
                    adapter = MonthAdapter(monthlyTransactions.children)
                    setRecycledViewPool(viewPool)
                }

            }


        }

        private fun selectMonth(month: Int): String {
            when (month) {
                1 -> return "January"
                2 -> return "February"
                3 -> return "March"
                4 -> return "April"
                5 -> return "May"
                6 -> return "June"
                7 -> return "July"
                8 -> return "August"
                9 -> return "September"
                10 -> return "October"
                11 -> return "November"
                12 -> return "December"
                else -> return "Invalid"
            }
        }
    }


}


class DiffCallback2 : DiffUtil.ItemCallback<MonthlyTransactions>() {
    override fun areItemsTheSame(
        oldItem: MonthlyTransactions,
        newItem: MonthlyTransactions
    ): Boolean {
        return oldItem.monthYear == newItem.monthYear
    }

    override fun areContentsTheSame(
        oldItem: MonthlyTransactions,
        newItem: MonthlyTransactions
    ): Boolean {
        return oldItem == newItem
    }
}
