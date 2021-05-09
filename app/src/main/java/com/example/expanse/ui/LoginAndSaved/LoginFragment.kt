package com.example.expanse.ui.LoginAndSaved

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.expanse.R
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        sharedPreferences =
            this.requireActivity().getSharedPreferences("login", Context.MODE_PRIVATE)

        val boarded: Boolean = sharedPreferences.getBoolean("isLogin", false)

        if (boarded) {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToTransactionListFragment()
            )
        } else {
            sharedPreferences.edit().putBoolean("isLogin", true)
        }
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        val name=view.findViewById<TextInputLayout>(R.id.PersonName).editText.toString()
//        val budget=view.findViewById<TextInputLayout>(R.id.PersonBudget).toString()
//        val income=view.findViewById<TextInputLayout>(R.id.PersonIncome).toString()
//        var islogin=sharedPreferences.getBoolean("isLogin", false)
//
//        if(islogin==true){
//            findNavController().navigate(
//                LoginFragmentDirections.actionLoginFragmentToTransactionListFragment()
//            )
//        }


//        if(name!=null && budget!=null && income!=null){
//            saveCredentials(name, budget, income)
//            println("name =$name budget= $budget ")
//
//
//        continueButton.setOnClickListener {
//
//
//            findNavController().navigate(
//                LoginFragmentDirections.actionLoginFragmentToTransactionListFragment()
//            )
//        }
//            }


        // one time login using text Watcher with this method we can enable and disable button
        PersonName.editText?.addTextChangedListener(boardingTextWatcher)
        PersonBudget.editText?.addTextChangedListener(boardingTextWatcher)
        PersonIncome.editText?.addTextChangedListener(boardingTextWatcher)

        continueButton.setOnClickListener {
            val name = PersonName.editText?.text.toString()
            val budget = PersonBudget.editText?.text.toString()
            val income = PersonIncome.editText?.text.toString()

            saveCredentials(name, budget, income)

            println("name = $name budget = $budget")
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToTransactionListFragment()
            )
        }

    }

    private val boardingTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }

        override fun afterTextChanged(s: Editable?) {
            val personName = PersonName.editText?.text.toString()
            val personBudget = PersonBudget.editText?.text.toString()


            if (personName.isEmpty()) {
                continueButton.isEnabled = false
                PersonName.error = "This field cannot be empty"
            }

            if (personBudget.isEmpty()) {
                continueButton.isEnabled = false
                PersonBudget.error = "This field cannot be empty"
            } else {
                continueButton.isEnabled = true
                PersonBudget.error = null
                PersonName.error = null
            }
        }

    }

    private fun saveCredentials(name: String, budget: String, income: String) {
        sharedPreferences.edit().putBoolean("isLogin", true).apply()
        sharedPreferences.edit().putString("Name", name).apply()
        sharedPreferences.edit().putString("Budget", budget).apply()
        sharedPreferences.edit().putString("Income", income).apply()

    }
}
