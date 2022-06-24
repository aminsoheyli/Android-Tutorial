package com.aminsoheyli.androidtutorial.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.androidtutorial.R
import com.aminsoheyli.androidtutorial.data.AudioAdapter
import com.aminsoheyli.androidtutorial.data.AudioInfo


class MediaPlayerActivity : AppCompatActivity(), ItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var seekbar: SeekBar
    private lateinit var mediaPlayer: MediaPlayer
    private val audios = ArrayList<AudioInfo>()
    private var seekBarValue = 0

    init {
        // Online Media
        audios.add(
            AudioInfo(
                "https://download.Quranicaudio.com/Quran/abdullaah_basfar/001.mp3",
                "Fatihah", "Abdulbasit ", "Quran"
            )
        )
        audios.add(
            AudioInfo(
                "https://download.quranicaudio.com/quran/abdullaah_basfar/109.mp3",
                "Kafirun", "Abdullah Basfar", "Quran"
            )
        )
        audios.add(
            AudioInfo(
                "https://download.quranicaudio.com/quran/abdullaah_basfar/110.mp3",
                "Nasr", "Abdullah Basfar", "Quran"
            )
        )
        audios.add(
            AudioInfo(
                "https://download.quranicaudio.com/quran/abdullaah_basfar/111.mp3",
                "Masad", "Abdullah Basfar", "Quran"
            )
        )

        audios.add(
            AudioInfo(
                "https://download.quranicaudio.com/quran/abdullaah_basfar/112.mp3",
                "Ikhlas'", "Abdullah Basfar", "Quran"
            )
        )
        audios.add(
            AudioInfo(
                "https://download.Quranicaudio.com/Quran/abdullaah_basfar/113.mp3",
                "Falagh", "Abdullah Basfar", "Quran"
            )
        )
        audios.add(
            AudioInfo(
                "https://download.quranicaudio.com/quran/abdullaah_basfar/114.mp3",
                "An-Nas", "Abdullah Basfar", "Quran"
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)

        initUi()
    }

    private fun initUi() {
        mediaPlayer = MediaPlayer()
        findViewById<Button>(R.id.button_play).setOnClickListener {
            mediaPlayer.start()
        }

        findViewById<Button>(R.id.button_pause).setOnClickListener {
            mediaPlayer.pause()
        }

        findViewById<Button>(R.id.button_stop).setOnClickListener {
            mediaPlayer.stop()
        }

        seekbar = findViewById(R.id.seekBar_audio)
        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                seekBarValue = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                mediaPlayer.seekTo(seekBarValue)
            }

        })

        recyclerView = findViewById(R.id.recyclerView_audios)
        recyclerView.adapter = AudioAdapter(audios, this)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    override fun onItemClicked(audioInfo: AudioInfo) {
        mediaPlayer.stop()
        mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(audioInfo.path)
        mediaPlayer.prepare()
        mediaPlayer.start()
        seekbar.max = mediaPlayer.duration
        val progressThread = ProgressThread()
        progressThread.start()
    }

    private inner class ProgressThread : Thread() {
        override fun run() {
            super.run()
            while (true) {
                try {
                    sleep(1000)
                } catch (e: Exception) {
                }
                runOnUiThread {
                    if (mediaPlayer != null)
                        seekbar.progress = mediaPlayer.currentPosition;
                }
            }
        }
    }
}

interface ItemClickListener {
    fun onItemClicked(audioInfo: AudioInfo)
}