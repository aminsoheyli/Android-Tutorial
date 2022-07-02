package com.aminsoheyli.androidtutorial.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.androidtutorial.R
import com.aminsoheyli.androidtutorial.ui.ItemClickListener


class AudioAdapter(
    private val dataSet: ArrayList<AudioInfo>,
    private val itemClickedInterface: ItemClickListener
) :
    RecyclerView.Adapter<AudioAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewTitle: TextView
        val textViewDescription: TextView

        init {
            textViewTitle = view.findViewById(R.id.textView_audio_title)
            textViewDescription = view.findViewById(R.id.textView_audio_description)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.card_audio, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val audioInfo = dataSet[position]
        viewHolder.textViewTitle.text = audioInfo.audioName
        viewHolder.textViewDescription.text = audioInfo.artistName
        viewHolder.itemView.setOnClickListener {
            itemClickedInterface.onItemClicked(audioInfo)
        }
    }

    override fun getItemCount() = dataSet.size
}