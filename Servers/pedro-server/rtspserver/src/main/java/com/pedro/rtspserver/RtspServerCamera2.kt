package com.pedro.rtspserver

import android.media.MediaCodec
import android.os.Build
import androidx.annotation.RequiresApi
import com.pedro.encoder.utils.CodecUtil
import com.pedro.rtplibrary.base.Camera2Base
import com.pedro.rtplibrary.view.OpenGlView
import com.pedro.rtsp.rtsp.VideoCodec
import com.pedro.rtsp.utils.ConnectCheckerRtsp
import java.nio.ByteBuffer

/**
 * Get audio data from microphone in PCM buffer and from camera API2 rendering a MediaCodec
 * interface. This builder can be executed in background mode if you use a context in the
 * constructor instead of a surfaceview.
 * @author pedro
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
open class RtspServerCamera2(
  openGlView: OpenGlView,
  connectCheckerRtsp: ConnectCheckerRtsp,
  port: Int
) : Camera2Base(
  openGlView
) {

  private val rtspServer: RtspServer = RtspServer(connectCheckerRtsp, port)

  /**
   * Sets the transmission type to video only.
   * @param onlyVideo if enabled, only video in the stream.
   */
  fun setOnlyVideo(onlyVideo: Boolean) {
    rtspServer.setOnlyVideo(onlyVideo)
  }

  /**
   * Sets the videocodec.
   * @param videoCodec [VideoCodec.H264] and [VideoCodec.H265] are supported.
   */
  fun setVideoCodec(videoCodec: VideoCodec) {
    videoEncoder.type =
      if (videoCodec == VideoCodec.H265) CodecUtil.H265_MIME else CodecUtil.H264_MIME
  }

  /**
   * Get endpoint connection.
   */
  fun getEndPointConnection(): String = "rtsp://${rtspServer.serverIp}:${rtspServer.port}/"

  override fun setAuthorization(user: String, password: String) {}

  /**
   * Need be called after [prepareVideo] or/and [prepareAudio]. This method override resolution of
   * [startPreview] to resolution seated in [prepareVideo]. If you never [startPreview] this method
   * [startPreview] for you to resolution seated in [prepareVideo].
   */
  fun startStream() {
    super.startStream("")
    rtspServer.startServer()
  }

  override fun prepareAudioRtp(isStereo: Boolean, sampleRate: Int) {
    rtspServer.isStereo = isStereo
    rtspServer.sampleRate = sampleRate
  }

  /**
   * Unused functions
   */
  override fun startStreamRtp(url: String) {}

  override fun stopStreamRtp() {
    rtspServer.stopServer()
  }

  override fun getAacDataRtp(aacBuffer: ByteBuffer, info: MediaCodec.BufferInfo) {}

  override fun onSpsPpsVpsRtp(sps: ByteBuffer, pps: ByteBuffer, vps: ByteBuffer?) {
    val newSps = sps.duplicate()
    val newPps = pps.duplicate()
    val newVps = vps?.duplicate()
    rtspServer.setVideoInfo(newSps, newPps, newVps)
  }

  override fun getH264DataRtp(h264Buffer: ByteBuffer, info: MediaCodec.BufferInfo) {
    rtspServer.sendVideo(h264Buffer, info)
  }

  override fun setLogs(enable: Boolean) {}

  override fun setCheckServerAlive(enable: Boolean) {}

  /**
   * Unused functions
   */
  @Throws(RuntimeException::class)
  override fun resizeCache(newSize: Int) {
  }

  override fun shouldRetry(reason: String?): Boolean = false

  override fun hasCongestion(): Boolean = true

  override fun setReTries(reTries: Int) {}

  override fun reConnect(delay: Long, backupUrl: String?) {}

  override fun getCacheSize(): Int = 0

  override fun getSentAudioFrames(): Long = 0

  override fun getSentVideoFrames(): Long = 0

  override fun getDroppedAudioFrames(): Long = 0

  override fun getDroppedVideoFrames(): Long = 0

  override fun resetSentAudioFrames() {}

  override fun resetSentVideoFrames() {}

  override fun resetDroppedAudioFrames() {}

  override fun resetDroppedVideoFrames() {}
}