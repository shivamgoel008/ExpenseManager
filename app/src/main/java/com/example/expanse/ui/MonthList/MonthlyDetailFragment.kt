package com.example.expanse.ui.MonthList

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expanse.R
import com.example.expanse.ui.TransactionAdapter
import com.example.expanse.ui.UpcomingTransactions.TransactionListViewModel
import kotlinx.android.synthetic.main.fragment_monthly_detail.*
import kotlinx.android.synthetic.main.fragment_transaction_list.*


class MonthlyDetailFragment : Fragment() {

    private lateinit var viewModel: MonthlyDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MonthlyDetailViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_monthly_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val monthYear = MonthlyDetailFragmentArgs.fromBundle(requireArguments()).dateMonth
        viewModel.setMonthYear(monthYear)

        setMonthlyBalance()

        with(recycler){
            layoutManager = LinearLayoutManager(activity)
            adapter = TransactionAdapter {
                findNavController().navigate(MonthlyDetailFragmentDirections.actionMonthlyDetailFragmentToTransactionDetailFragment(it))
            }
        }

        viewModel.transactionMonth.observe(viewLifecycleOwner, Observer{
            (recycler.adapter as TransactionAdapter).submitList(it)
        })
    }


    private fun setMonthlyBalance(){
        val sharedPreferences : SharedPreferences = this.requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor =  sharedPreferences.edit()

        viewModel.sumByMonth.observe(viewLifecycleOwner, Observer {
            var monthBalance = sharedPreferences.getString("Budget","0")?.toFloat()
            if(it!=null) {
                monthBalance = monthBalance?.plus(it)
                amount_remaining_monthly.text = monthBalance.toString()
            }
        })
    }
}