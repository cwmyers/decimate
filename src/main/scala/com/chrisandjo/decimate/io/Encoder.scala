package com.chrisandjo.decimate.io

import scalaz._
import Scalaz._
import scalaz.effect.IO
import scala.sys.process.Process
import java.io._

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

  def getCommand(ffmpegBin: String,
                 videoFile: String) = s"$ffmpegBin $videoFile  '-vcodec libx264 -s 1024x576' /tmp/out.mp4"

  def callFfmpeg(command: String): IO[Stream[String]] = IO {
    Process(command).lines_!
  }

  def getStream(fileName: String): OptionT[IO, Stream[String]] = {
    val optionalCommand: OptionT[IO, String] = findFfmpeg map {
      getCommand(_, fileName)
    }
    optionalCommand >>= {
      command => callFfmpeg(command).liftM[OptionT]
    }

//    for {
//      command <- optionalCommand
//    } yield callFfmpeg(command).liftM[OptionT]
  }


  def encode(fileName: String): IO[Unit] = {
//  IO {println ("hi") }
    getStream(fileName) map {
      a: Stream[String] =>
        a map {
          _.length
        } foreach (println)

    } getOrElse (Unit.box {
      println("nothing")
    })
  }
}
