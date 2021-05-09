package com.example.expanse.ui.UpcomingTransactions

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.expanse.R
import com.example.expanse.R.id.*
import com.example.expanse.ui.TransactionAdapter
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.fragment_transaction_list.*


class TransactionListFragment : Fragment() {

    private lateinit var viewModel: TransactionListViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        viewModel = ViewModelProviders.of(this).get(TransactionListViewModel::class.java)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_transaction_list, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        with(recyclerViewDetail) {
            layoutManager = LinearLayoutManager(activity)
/* when user clicks the transaction in recycler view then it will navigate to fragment with details of the transaction */
            adapter = TransactionAdapter(
                {
                    findNavController().navigate(
                        TransactionListFragmentDirections.actionTransactionListFragmentToTransactionDetailFragment(
                            it,
                        )
                    )
                },
            )
        }


/* to update to net balace remaining after every addition in the recycler view  and always use this
 this method to update the shared preference rather then the method used in login fragment */
        val sharedPreferences: SharedPreferences =
            this.requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        setNetBalance(sharedPreferences, editor)

//        to set the name in the recycler view fragment
        name_text.text = sharedPreferences.getString("Name", "illuminati").toString()


/* for tool bar on click listener */
        addAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {

                profile -> {
                    findNavController().navigate(TransactionListFragmentDirections.actionTransactionListFragmentToSavedProfileFragment())
                    true
                }

                calender_view -> {
                    findNavController().navigate(TransactionListFragmentDirections.actionTransactionListFragmentToDayTransactionFragment())
                    true
                }
                else -> false
            }
        }

        bottomNavigationView.setOnNavigationItemSelectedListener { it ->
            when (it.itemId) {

                daily -> {
                    true
                }

                add_expanse -> {
                    findNavController().navigate(
                        TransactionListFragmentDirections.actionTransactionListFragmentToTransactionDetailFragment(
                            0
                        )
                    )
                    true
                }

                monthly -> {
                    findNavController().navigate(TransactionListFragmentDirections.actionTransactionListFragmentToMonthlyTransactionsFragment())
                    true
                }
                else -> false
            }
        }

        viewModel.transactions.observe(viewLifecycleOwner, Observer {
            (recyclerViewDetail.adapter as TransactionAdapter).submitList(it)
        })

//        for net amount

//        val sharedPreferencesp : SharedPreferences = this.requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE)
//        val netAmount = sharedPreferencesp.getString("Budget","0")
//
//        amount_remaining.text=netAmount


    }

    private fun setupPieChart() {

        // setup Pie entries
        val pieEntries = arrayListOf<PieEntry>()
        var first: Float = amount_remaining2.text.toString().toFloat()
        var second: Float = netCash.text.toString().toFloat()
        var third: Float = netCredit.text.toString().toFloat()
        var forth: Float = netDebit.text.toString().toFloat()

        pieEntries.add(PieEntry(first))
        pieEntries.add(PieEntry(second))
        pieEntries.add(PieEntry(third))
        pieEntries.add(PieEntry(forth))
// setup Pie chart animations
        pieChart.animateXY(1000, 1000)

        // setup PieChart Entries Colors
        val pieDataSet = PieDataSet(pieEntries, "This is Pie Chart Label")
        pieDataSet.setColors(
            resources.getColor(R.color.R),
            resources.getColor(R.color.Java),
            resources.getColor(R.color.CPP),
            resources.getColor(R.color.Python)
        )

        // setup pie data set in piedata
        val pieData = PieData(pieDataSet)

        // setip text in pieChart centre
        pieChart.centerText = "Expanses"
        pieChart.setCenterTextColor(resources.getColor(android.R.color.black))
        pieChart.setCenterTextSize(15f)

        // hide the piechart entries tags
        pieChart.legend.isEnabled = false

//        now hide the description of piechart
        pieChart.description.isEnabled = false
        pieChart.description.text = "Expanses"

        pieChart.holeRadius = 40f
        // this enabled the values on each pieEntry
        pieData.setDrawValues(true)

        pieChart.data = pieData
    }


    private fun setNetBalance(
        sharedPreferences: SharedPreferences,
        editor: SharedPreferences.Editor
    ) {

//        val sharedPreferences : SharedPreferences = this.requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE)
//        println("Name ${sharedPreferences.getString("Name","0")} Budget ${sharedPreferences.getString("Budget","0")}")
        var remainingAmount: Float = sharedPreferences.getString("Budget", "0")!!.toFloat()

        println("Net Amount $remainingAmount")
        amount_remaining2.text = remainingAmount.toString()
        viewModel.netAmount.observe(viewLifecycleOwner, Observer {


            if (it != null) {
                remainingAmount = sharedPreferences.getString("Budget", "0")!!.toFloat()
                remainingAmount += it

//                sharedPreferences.edit().putString("Budget", remainingAmount.toString()).apply()
//                remainingAmount.toString().also { amount_remaining.text = it }

                editor.putString("Budget", remainingAmount.toString())
                amount_remaining2.text = remainingAmount.toString()
                setupPieChart()
                if (remainingAmount >= 0)
                    amount_remaining2.setTextColor(Color.parseColor("#ADFF2F"))
                else if (remainingAmount < 0) {
                    amount_remaining2.setTextColor(Color.parseColor("#ff726f"))
                }
            }
        })

        viewModel.netAmountCash.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                netCash.text = it.toString()
                setupPieChart()
            }

        })

        viewModel.netAmountCredit.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                netCredit.text = it.toString()
                setupPieChart()
            }
        })

        viewModel.netAmountDebit.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                netDebit.text = it.toString()
                setupPieChart()
            }
        })

    }
}