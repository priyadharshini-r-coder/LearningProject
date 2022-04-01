package com.example.learningproject.exoplayer

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.learningproject.R
import com.example.learningproject.databinding.ActivityExoplayerBinding
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory

class ExoplayerActivity : AppCompatActivity(), Player.EventListener {
    private lateinit var simpleExoplayer: SimpleExoPlayer
    private var playbackPosition: Long = 0
    private val mp4Url = "https://html5demos.com/assets/dizzy.mp4"
    private val dashUrl = "https://storage.googleapis.com/wvmedia/clear/vp9/tears/tears_uhd.mpd"
    private val urlList = listOf(mp4Url to "default", dashUrl to "dash")
    private lateinit var binding:ActivityExoplayerBinding
    private val dataSourceFactory: DataSource.Factory by lazy {
        DefaultDataSourceFactory(this,"exoplayer-sample")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      binding= ActivityExoplayerBinding.inflate(layoutInflater)
       val view=binding.root
       setContentView(view)

    }

    override fun onStart() {
        super.onStart()
        initialisePlayer()
    }



    override fun onStop() {
        super.onStop()
        releasePlayer()
    }
    private fun initialisePlayer() {
      simpleExoplayer=SimpleExoPlayer.Builder(this).build()
        val randomUrl=urlList.random()
        preparePlayer(randomUrl.first,randomUrl.second)
        simpleExoplayer.seekTo(playbackPosition)
        simpleExoplayer.playWhenReady=true
        simpleExoplayer.addListener(this)


    }




    private fun preparePlayer(videoUrl: String, type: String) {
        val uri= Uri.parse(videoUrl)
        val mediaSource=buildMediaSource(uri,type)
        simpleExoplayer.prepare(mediaSource)
    }
    private fun releasePlayer() {
    playbackPosition=simpleExoplayer.currentPosition
        simpleExoplayer.release()
    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
     return if(type=="dash")
     {
         DashMediaSource.Factory(dataSourceFactory)
             .createMediaSource(uri)
     }else
     {
         ProgressiveMediaSource.Factory(dataSourceFactory)
             .createMediaSource(uri)
     }
    }
    override fun onPlayerStateChanged(playWhenReady:Boolean,playbackState:Int)
    {
        if(playbackState==Player.STATE_BUFFERING)

        binding.progressBar.visibility= View.VISIBLE
        else if(playbackState==Player.STATE_READY || playbackState==Player.STATE_ENDED)
            binding.progressBar.visibility=View.VISIBLE
    }
}