package com.chrisandjo.decimate.io

import scalaz._
import Scalaz._
import scalaz.effect.IO
import java.io._
import scala.sys.process.Process

/**
 * Created by IntelliJ IDEA.
 * User: chris
 * Date: 16/05/2013
 * Time: 21:12
 * Copyright (c) Chris Myers 2013
 */
object Encoder {

  def findFfmpeg: OptionT[IO, String] = {
    OptionT[IO, String](IO {
      List("/usr/local/bin/ffmpeg", "/usr/bin/ffmpeg").find {
        new File(_).exists
      }
    }
    )
  }

  def getCommand(ffmpegBin: String, videoFile:String)= s"$ffmpegBin $videoFile  '-vcodec libx264 -b:v 2000k -refs 3 -keyint_min 29 -x264opts trellis=0 -acodec libfaac -ar 44100 -ab 160k -s 1024x576' /tmp/out.mp4"

  def callFfmpeg(command: String): IO[Stream[String]] = IO {
    Process(command).lines_!
  }

  def getStream(fileName: String): OptionT[IO,Stream[String]] = {
    val a:OptionT[IO,String] = findFfmpeg map {getCommand(_, fileName)}
    a map {Process(_).lines_!}
  }

  //  = for {
  //    ffmpegBin <- findFfmpeg
  //    stream <- callFfmpeg(fileName, ffmpegBin)
  //
  //  } yield stream


  def encode(fileName: String): IO[Unit] = {


    val b = getStream(fileName) map {
      a  =>
        a map {
          _.length
        } foreach (println)

    }

    b.getOrElse(Unit.box {println("nothing")})


  }
}
