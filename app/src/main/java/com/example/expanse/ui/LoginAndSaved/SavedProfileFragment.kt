package com.example.expanse.ui.LoginAndSaved

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.expanse.R
import kotlinx.android.synthetic.main.fragment_saved_profile.*

class SavedProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved_profile, container, false)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


//         putting the values login in the saved details
        val sharedPreferences: SharedPreferences =
            this.requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE)

        val tempName = sharedPreferences.getString("Name", "Shivam")
        val tempBudget = sharedPreferences.getString("Budget", "1000")
        val tempIncome = sharedPreferences.getString("Income", "1000")

        name_saved.editText?.setText(tempName)
        budget_saved.editText?.setText(tempBudget)
        income_saved.editText?.setText(tempIncome)

//         disabling the edit text till the user clicks the pencil
        name_saved.isEnabled = false
        budget_saved.isEnabled = false
        income_saved.isEnabled = false


        addAppBar2.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {


                R.id.edit_pencil -> {
//                     when user clicks the pencil the edit text get enabled
                    name_saved.isEnabled = true
                    budget_saved.isEnabled = true
                    income_saved.isEnabled = true

//                    updating the values of sharedPreferences when user clicks the save button
                    saveButton.setOnClickListener {

                        sharedPreferences.edit()
                            .putString("Name", name_saved.editText?.text.toString()).apply()
                        sharedPreferences.edit()
                            .putString("Budget", budget_saved.editText?.text.toString()).apply()
                        sharedPreferences.edit()
                            .putString("Income", income_saved.editText?.text.toString()).apply()

                        findNavController().navigate(
                            SavedProfileFragmentDirections.actionSavedProfileFragmentToTransactionListFragment()
                        )
                    }
                    true
                }
                else -> false
            }
        }

    }
}