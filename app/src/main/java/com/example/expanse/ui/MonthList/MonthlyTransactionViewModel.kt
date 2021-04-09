package com.example.expanse.ui.MonthList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.expanse.data.MonthlyTransactions
import com.example.expanse.data.Transaction
import com.example.expanse.data.TransactionListRepository

class MonthlyTransactionViewModel(application: Application): AndroidViewModel(application) {

    private val repo: TransactionListRepository = TransactionListRepository(application)

    val month: LiveData<List<MonthlyTransactions>>
        get() = repo.getTransactionMonth()

}