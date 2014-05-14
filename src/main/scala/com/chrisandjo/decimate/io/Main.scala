package com.chrisandjo
package decimate
package io

import scalaz.effect._
import scalaz.effect.IO._
import scalaz._, Scalaz._
import time.Time._
import StreamProcessor._
import scala.language.higherKinds
import com.chrisandjo.decimate.model.{ReportStatus, CleanUp, Actions, ReportError}


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


    def handleStream2(s: Streams[String, IO[Unit]]): IO[Unit] = {
      val progressEffects: IO[Unit] = printStream(s.progressStream)
      s.completedStream.foldLeft(progressEffects)(_ mappend _)
    }

    def handleStream1(s: Stream[Actions]): IO[Unit] = {
      val ioActions: Stream[IO[Unit]] = s.map {
        case CleanUp => IO {
          println("Cleaning up")
        }
        case ReportError(e) => IO {
          println(s"There was an error $e")
        }
        case ReportStatus(status) => IO {
          println(status)
        }
      }
      ioActions.foldRight(ioUnit)(_ mappend _)
    }

    def handleStream(s: Stream[Actions]): IO[Unit] = IO {
      s.foreach {
        case CleanUp => println("Cleaning up")
        case ReportError(e) => println(s"There was an error $e")
        case ReportStatus(status) => println(status)
      }
    }

    val metaData = Encoder.getMetadata(fileName)

    val encodingStream = Encoder.encode(fileName)

    val processedStream = Apply[REIO].apply2(metaData, encodingStream)(processStream)

    processedStream(config).valueOr(error => Stream(ReportError(error))) flatMap handleStream

  }

}



