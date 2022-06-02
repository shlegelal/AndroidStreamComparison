package com.pedro.rtspserver

/**
 * RTSP client listener.
 * @author pedro
 */
interface ClientListener {
  /**
   * Disconnection callback.
   * @param client the client that disconnected.
   */
  fun onDisconnected(client: ServerClient)
}