package com.aminsoheyli.mvvmbasics.ui.quotes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.aminsoheyli.mvvmbasics.R
import com.aminsoheyli.mvvmbasics.data.Quote
import com.aminsoheyli.mvvmbasics.ui.TutorialActivity
import com.aminsoheyli.mvvmbasics.utilities.InjectorUtils


class QuotesActivity : AppCompatActivity() {
    private lateinit var textViewQuotes: TextView
    private lateinit var editTextQuote: EditText
    private lateinit var editTextAuthor: EditText
    private lateinit var buttonAddQuote: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quotes)
        textViewQuotes = findViewById(R.id.textView_quotes)
        buttonAddQuote = findViewById(R.id.button_add_quote)
        editTextQuote = findViewById(R.id.editText_quote)
        editTextAuthor = findViewById(R.id.editText_author)
        initializeUi()
        tutorialActivity()
    }

    private fun tutorialActivity() {
        val buttonActivity = findViewById<Button>(R.id.button_tutorial)
        buttonActivity.setOnClickListener {
            val intent = Intent(this, TutorialActivity::class.java)
            this.startActivity(intent)
        }
    }


    private fun initializeUi() {
        val factory = InjectorUtils.provideQuotesViewModelFactory()
        val viewModel = ViewModelProvider(this, factory)
            .get(QuotesViewModel::class.java)

        viewModel.getQuotes().observe(this, Observer { quotes ->
            val stringBuilder = StringBuilder()
            quotes.forEach { quote -> stringBuilder.append("$quote\n\n") }
            textViewQuotes.text = stringBuilder.toString()
        })
        buttonAddQuote.setOnClickListener {
            val quote = editTextQuote.text
            val author = editTextAuthor.text
            if (quote.isNotEmpty() && author.isNotEmpty()) {
                val quoteString = Quote(quote.toString(), editTextAuthor.text.toString())
                viewModel.addQuote(quoteString)
                editTextQuote.setText("")
                editTextAuthor.setText("")
            }
        }
    }

}