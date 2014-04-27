package com.chrisandjo
package decimate
package io

import scalaz.effect._
import IO._
import scalaz._, Scalaz._
import time.Time._


object Main extends SafeApp {

  override def runl(args: List[String]) = {

    val config = Config(
      ffmpegLocations = List("/usr/local/bin/ffmpeg", "/usr/bin/ffmpeg").map(FilePath),
      ffmpegWrapperLocations = List(
        "/Users/grailsuser/decimate/src/main/scripts/ffmpeg_wrapper.sh",
        "/Users/chris/Development/decimate/src/main/scripts/ffmpeg_wrapper.sh",
        "/Users/chris.myers/projects/decimate/src/main/scripts/ffmpeg_wrapper.sh").map(FilePath),
      ffprobeLocations = List("/usr/local/bin/ffprobe", "/usr/bin/ffprobe").map(FilePath)
    )

    val fileName = FilePath("/Users/chris.myers/src/ruby-2.1.0/sample/trick2013/mame/music-box.mp4")

    def processStream(s: Stream[String]) =
      IO {
        s.filter(_.startsWith("frame")).
          collect(Function.unlift(extractTime)).
          foreach(println)
      }


    def processError = putStrLn _

    for {
      duration <- Encoder.getMetadata(fileName)
    } yield duration

    for {
      streamOrError <- Encoder.encode(fileName)(config).run
      _ <- streamOrError.fold(processError, processStream)
    } yield ()

  }

}

