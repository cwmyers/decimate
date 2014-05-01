package com.chrisandjo
package decimate
package io

import scalaz._, Scalaz._
import scalaz.effect.IO
import scala.sys.process.Process
import FileFinder._
import scala.language.higherKinds
import com.chrisandjo.decimate.time.Time
import com.chrisandjo.decimate.time.Time.Seconds


object Encoder {

  def getCommand(ffmpegWrapper: String @@ FilePath,
                 ffmpegBin: String @@ FilePath,
                 videoFile: String @@ FilePath) =
    Seq(ffmpegWrapper, ffmpegBin, videoFile.replaceAll(" ", "\\ "), "/tmp/out.mp4")

  def executeCommand(command: Seq[String]): IO[Stream[String]] = IO {
    println(s"Running command $command")
    Process(command).lines_!
  }


  def encode(fileName: String @@ FilePath): ReaderT[EitherErrorIO, Config, Stream[String]] = for {
    ffmpegWrapper <- findFfmpegWrapper
    ffmpegBin <- findFfmpeg
    stream <- liftReaderEitherIO(executeCommand(getCommand(ffmpegWrapper, ffmpegBin, fileName)))
  } yield stream

  def getMetadata(fileName: String @@ FilePath): ReaderT[EitherErrorIO, Config, Double @@ Seconds] = for {
    ffprobeBin <- findFfprobe
    ffprobeWrapper <- findFfprobeWrapper
    duration <- EitherIO(
      executeCommand(Seq(ffprobeWrapper, ffprobeBin, fileName))
        map getDuration).
      liftReaderT[Config]
  } yield duration


  def getDuration(stream: Stream[String]): String \/ (Double @@ Seconds) = {
    stream.filter(_.contains("Duration")).headOption.flatMap(Time.extractTime) \/> "Couldn't determine duration"
  }

}


