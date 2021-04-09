package com.example.expanse.ui.DayTransaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expanse.R
import kotlinx.android.synthetic.main.fragment_day_transaction.*

class DayTransactionFragment : Fragment() {

    private lateinit var viewModel: DayTransactionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        viewModel = ViewModelProviders.of(this).get(DayTransactionViewModel::class.java)
//        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_day_transaction, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        viewModel = ViewModelProviders.of(this).get(DayTransactionViewModel::class.java)

        val calendarView: CalendarView = requireView().findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener(CalendarView.OnDateChangeListener() { calendarView, i: Int, i1: Int, i2: Int ->
//            i1 += 1
            val month = i1 + 1

            val date: String

            if (month < 10) {
                date = "$i-0$month-$i2"
            } else {
                date = "$i-$month-$i2"
            }
            viewModel.setDate(date)
            val mon = month(month)
            `fun`.text = "Transactions On $i2 $mon, $i"
//            Toast.makeText(activity, "$date", Toast.LENGTH_SHORT).show()

            with(day_recycler_view) {
                layoutManager = LinearLayoutManager(activity)
                adapter = DayTransactionAdapter {
                }
            }
            viewModel.dayTransaction.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                (day_recycler_view.adapter as DayTransactionAdapter).submitList(it)
            })
        })
    }


    private fun month(m: Int): String {
        when {
            m == 1 -> return "Jan"
            m == 2 -> return "Feb"
            m == 3 -> return "March"
            m == 4 -> return "April"
            m == 5 -> return "May"
            m == 6 -> return "June"
            m == 7 -> return "July"
            m == 8 -> return "Aug"
            m == 9 -> return "Sep"
            m == 10 -> return "Oct"
            m == 11 -> return "Nov"
            else -> return "Dec"
        }
    }
}