package com.chrisandjo
package decimate
package io

import scalaz.effect._
import scalaz._, Scalaz._
import time.Time._
import StreamProcessor._
import scala.language.higherKinds


object Main extends SafeApp {
  type myProgram = IO[String \/ Reader[Config, (Double @@ Seconds, Stream[String])]]


  override def runl(args: List[String]) = {

    val config = Config(
      ffmpegLocations = List("/usr/local/bin/ffmpeg", "/usr/bin/ffmpeg").map(FilePath),
      ffmpegWrapperLocations = List(
        "/Users/grailsuser/decimate/src/main/scripts/ffmpeg_wrapper.sh",
        "/Users/chris/Development/decimate/src/main/scripts/ffmpeg_wrapper.sh",
        "/Users/chris.myers/projects/decimate/src/main/scripts/ffmpeg_wrapper.sh").map(FilePath),
      ffprobeLocations = List("/usr/local/bin/ffprobe", "/usr/bin/ffprobe").map(FilePath),
      ffprobeWrapperLocations = List("/Users/chris.myers/projects/decimate/src/main/scripts/ffprobe_wrapper.sh").map(FilePath)
    )

    val fileName = FilePath("/Users/chris.myers/src/ruby-2.1.0/sample/trick2013/mame/music-box.mp4")

    def printStream(s: Stream[String]) = IO {
      s.foreach(println)
    }

    val metaData = Encoder.getMetadata(fileName)

    val encode = Encoder.encode(fileName)

    val metaDataAndEncodeStream: ReaderT[EitherErrorIO, Config, (Double @@ Seconds, Stream[String])] = metaData tuple encode

    val processedEffects: ReaderT[EitherErrorIO, Config, Stream[String]] = metaDataAndEncodeStream map (processStream _).tupled

    processedEffects(config).valueOr(Stream(_)) flatMap printStream

  }

}



