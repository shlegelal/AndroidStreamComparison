package com.example.libvlc_test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.net.Uri
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

class MainActivity : AppCompatActivity()
{
    private var url: String = "rtsp://192.168.0.105:1554/"

    private lateinit var libVlc: LibVLC
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var videoLayout: VLCVideoLayout

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        libVlc = LibVLC(this)
        mediaPlayer = MediaPlayer(libVlc)
        videoLayout = findViewById(R.id.videoLayout)
    }

    override fun onStart()
    {
        super.onStart()

        mediaPlayer.attachViews(videoLayout, null, false, false)

        val media = Media(libVlc, Uri.parse(url))
        media.setHWDecoderEnabled(true, false)
        media.addOption(":network-caching=100")
        media.addOption(":clock-jitter=0")
        media.addOption(":clock-synchro=0")
        media.addOption(":rtsp-caching=0")
//        options.add("--rtsp-tcp");
//        options.add("--live-caching=0");
//        options.add("--file-caching=0")
//        options.add("--drop-late-frames")
//        options.add("--skip-frames")
//        options.add("--disc-caching=3000")
//        options.add("--sout-mux-caching=0")


        mediaPlayer.media = media
        media.release()
        mediaPlayer.play()
    }

    override fun onStop()
    {
        super.onStop()

        mediaPlayer.stop()
        mediaPlayer.detachViews()
    }

    override fun onDestroy()
    {
        super.onDestroy()

        mediaPlayer.release()
        libVlc.release()
    }
}