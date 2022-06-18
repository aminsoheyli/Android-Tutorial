package com.aminsoheyli.mvvmbasics.ui.quotes

import androidx.lifecycle.ViewModel
import com.aminsoheyli.mvvmbasics.data.Quote
import com.aminsoheyli.mvvmbasics.data.QuoteRepository

class QuotesViewModel(private val quoteRepository: QuoteRepository) : ViewModel() {
    fun getQuotes() = quoteRepository.getQuotes()
    fun addQuote(quote: Quote) = quoteRepository.addQuote(quote)
}