# AndroidStreamComparison

[![Codacy Badge](https://app.codacy.com/project/badge/Grade/03b7aa2dc5b049749bf907a5e3271b9c)](https://www.codacy.com/gh/shlegelal/AndroidStreamComparison/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=shlegelal/AndroidStreamComparison&amp;utm_campaign=Badge_Grade)

Comparison of streaming libraries for Android with low latency.

## Goal

Give an overview of existing protocols for live-streaming and libraries for android.
Design a system of two smartphones, one of which transmits live video from its camera and the other receives it.

## Overview

### Review protocols

The following protocols for stream transmission over the network are considered:

- Protocols work on TCP:
    - RTMP
    - HTTP and related HLS, DASH и MPEG-DASH

- Protocols work on UDP
    - Implementing TCP reliability at the application layer:
        - SRT
        - RTMFP

    - Protocol works on RTP:
        - RTSP

- Protocol works on SCTP:
    - WebRTC

- Protocols not included in the list:
    - FTL - the server-side implementation of Mixer’s FTL protocol is proprietary and closed-source as of this writing.

![Protocols](https://github.com/shlegelal/AndroidStreamComparison/blob/main/res/eng.jpg)

Secure and reliable protocols (RTMP, HTTP, SRT, RTMFP, WebRTC) will have inherently higher latency than UDP-based
protocols. Therefore, we will only consider RTSP.

### Review RTSP servers

The following libraries were found:

- [rtmp-rtsp-stream-client-java](https://github.com/pedroSG94/rtmp-rtsp-stream-client-java)

Excluded from consideration:

- [libstreaming](https://github.com/fyhertz/libstreaming) - no camera2 API support.

- [sms](https://github.com/pengliren/sms) - implements RTSP only over TCP.

- [RTSP.Server.Android](https://github.com/VideoExpertsGroup/RTSP.Server.Android) - failed to start.

- [Live555](https://github.com/papan01/Live555-server-android) - obsolete.
    - [LiveStreamer](https://github.com/papan01/LiveStreamer) - also not suitable.
    - [Live264Streamer](https://github.com/huzongyao/Live264Streamer)
    - [Live555-server-android](https://github.com/papan01/Live555-server-android)

- [https://github.com/spex66/RTSP-Camera-for-Android](https://github.com/spex66/RTSP-Camera-for-Android) - obsolete.

- [RTSPMultiCam](https://github.com/jiaxin-du/RTSPMultiCam) - inappropriate functionality.

### Review RTSP players

Players base on [ffmpeg](https://www.ffmpeg.org/):

- [Ijkplayer](https://github.com/Bilibili/ijkplayer)
    - [rtsp_player](https://github.com/bowen919446264/rtsp_player) - project is not supported, the player could not be
      started.
    - [GSYVideoPlayer](https://github.com/CarGuo/GSYVideoPlayer) - no working implementation is provided.

- [mobile-ffmpeg](https://github.com/tanersener/mobile-ffmpeg) - doesn't play the stream.

Players base on [libvlc](https://github.com/videolan/vlc-android#libvlc)

- [VLC-android](https://github.com/videolan/vlc-android)

Excluded from consideration:

- [VXG.Media.SDK.Android](https://github.com/VideoExpertsGroup/VXG.Media.SDK.Android) - closed source.
- [EasyPlayer-RTSP-Android](https://github.com/tsingsee/EasyPlayer) - closed source.
- [RTSP.Player.Android](https://github.com/VideoExpertsGroup/RTSP.Player.Android) - obsolete.
- [RTSPTest](https://github.com/rayryeng/RTSPTest) - obsolete.
- [Yuneec-RTSP-Player-Android](https://github.com/YUNEEC/Yuneec-RTSP-Player-Android) - not supported.

## Comparison method

### Devices

1. Monitor with centi seconds clock (i.e. [clock](https://htmlblog.ucoz.net/html_files/time.html))
2. Android phone with RTSP server installed (Server)
3. Android phone with RTSP player installed (Player)
4. Camera (for measurements)

### Processing video recordings

Run the [script](https://github.com/shlegelal/AndroidStreamComparison/blob/main/Scripts/take_frames.py) to take out the
frames. This script takes frames exponentially. Then we process the images and make graphs.

### Error calculation

For my experiments I took the following devices

- Monitor with a fps equal to 60
- Android Samsung Galaxy S10e as Server
- Android Galaxy S10 Lite as Player
- Samsung Galaxy Tab S6 Lite camera with fps equal to 30

![Stand](https://github.com/shlegelal/AndroidStreamComparison/blob/main/res/stand.jpg)

The error of the Server and of the Player with a fps of 30 will be up to 4 centi seconds, 8 centi seconds in total. And
given the fps of the camera, the latency counting error should not be more than 12 centi seconds.

## Results

The following [VLC commands](https://wiki.videolan.org/VLC_command-line_help/) affect the latency:

- network-caching
  ![network-caching](https://github.com/shlegelal/AndroidStreamComparison/blob/main/res/vlc-nc.svg)
- rtsp-caching
  ![rtsp-caching](https://github.com/shlegelal/AndroidStreamComparison/blob/main/res/vlc-rc.svg)
- clock-jitter
  ![clock-jitter](https://github.com/shlegelal/AndroidStreamComparison/blob/main/res/vlc-cj.svg)
- clock-synchro
  ![clock-synchro](https://github.com/shlegelal/AndroidStreamComparison/blob/main/res/vlc-cs.svg)

We managed to reach a latency of less than 0.4 second
![Result](https://github.com/shlegelal/AndroidStreamComparison/blob/main/res/vlc-long.svg)
