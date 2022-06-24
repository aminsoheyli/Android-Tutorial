package com.aminsoheyli.androidtutorial.ui

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.media.MediaPlayer
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aminsoheyli.androidtutorial.R
import com.aminsoheyli.androidtutorial.data.AudioAdapter
import com.aminsoheyli.androidtutorial.data.AudioInfo
import com.aminsoheyli.androidtutorial.utilities.Utility

private const val REQUEST_CODE_ASK_PERMISSIONS_READ_EXTERNAL_STORAGE = 1

@SuppressLint("Range")
class MediaPlayerActivity : AppCompatActivity(), ItemClickListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var seekbar: SeekBar
    private lateinit var mediaPlayer: MediaPlayer
    private val audios = ArrayList<AudioInfo>()
    private var seekBarValue = 0

    /*// Online Media
    init {
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
    }*/

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

        loadAudios()
        recyclerView = findViewById(R.id.recyclerView_audios)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun loadAudios() {
        if (checkSelfPermission(READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED) {
            val audiosUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
            val cursor = contentResolver.query(audiosUri, null, selection, null, null)
            if (cursor?.moveToFirst() == true) {
                do {
                    val audioName =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    val fullPath =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val albumName =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                    val artistName =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    audios.add(AudioInfo(fullPath, audioName, albumName, artistName))
                } while (cursor.moveToNext())
                recyclerView.adapter = AudioAdapter(audios, this)
            }
            cursor?.close()
        } else if (!shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE))
            requestPermissions(
                arrayOf(READ_EXTERNAL_STORAGE),
                REQUEST_CODE_ASK_PERMISSIONS_READ_EXTERNAL_STORAGE
            )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS_READ_EXTERNAL_STORAGE ->
                if (grantResults[0] == PERMISSION_GRANTED)
                    loadAudios()
                else
                    Utility.showSnackBar(recyclerView, "You denied the external storage access")
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
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