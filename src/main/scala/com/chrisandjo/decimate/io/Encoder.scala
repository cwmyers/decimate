package com.chrisandjo
package decimate
package io

import scalaz._
import scalaz.effect.IO
import scala.sys.process.Process
import FileFinder._
import scala.language.higherKinds


object Encoder {

  def getCommand(ffmpegWrapper: String, ffmpegBin: String,
                 videoFile: String) = s"$ffmpegWrapper $ffmpegBin $videoFile   /tmp/out.mp4"

  def callFfmpeg(command: String): IO[Stream[String]] = IO {
    Process(command).lines_!
  }

  def encode(fileName: String): ReaderT[EitherErrorIO, Config, Stream[String]] = for {
    ffmpegWrapper <- findFfmpegWrapper
    ffmpegBin <- findFfmpeg
    stream <- liftReaderEitherIO(callFfmpeg(getCommand(ffmpegWrapper, ffmpegBin, fileName)))

  } yield stream


}


