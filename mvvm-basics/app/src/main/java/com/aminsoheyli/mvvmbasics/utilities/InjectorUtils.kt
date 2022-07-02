package com.aminsoheyli.mvvmbasics.utilities

import com.aminsoheyli.mvvmbasics.data.FakeDatabase
import com.aminsoheyli.mvvmbasics.data.QuoteRepository
import com.aminsoheyli.mvvmbasics.ui.quotes.QuotesViewModelFactory

object InjectorUtils {
    fun provideQuotesViewModelFactory(): QuotesViewModelFactory {
        val quoteRepository = QuoteRepository.getInstance(FakeDatabase.getInstance().quoteDao)
        return QuotesViewModelFactory(quoteRepository)
    }
}