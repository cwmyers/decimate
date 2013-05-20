package com.chrisandjo.decimate.io

import scalaz.effect.IO
import java.io._
import scala.sys.process.Process

/**
 * Created by IntelliJ IDEA.
 * User: chris
 * Date: 16/05/2013
 * Time: 21:12
 * Copyright (c) Chris Myers 2010
 */
object Encoder {

  def findFfmpeg: IO[Option[String]] = IO {
    List("/usr/local/bin/ffmpeg", "/usr/bin/ffmpeg").find {
      new File(_).exists
    }
  }

  def callFfmpeg(videoFile: String, ffmpegBin: String): IO[Stream[String]] = IO {
    val command = s"$ffmpegBin $videoFile  '-vcodec libx264 -b:v 2000k -refs 3 -keyint_min 29 -x264opts trellis=0 -acodec libfaac -ar 44100 -ab 160k -s 1024x576' /tmp/out.mp4"
    Process(command).lines_!
  }

  def getStream(fileName: String):IO[Stream[String]] = for {
    ffmpegOption <- findFfmpeg
    ffmpegBin <- ffmpegOption
    stream <- callFfmpeg(fileName, ffmpegBin)

  } yield stream


  def encode(fileName: String): IO[Unit] = {


    getStream(fileName) map {
      a: Stream[String] =>
        a map {
          _.length
        } foreach (println)

    }


  }
}
