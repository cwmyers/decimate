package com.chrisandjo.decimate.io

import scalaz._
import Scalaz._
import scalaz.effect.IO
import scala.sys.process.Process
import FileFinder._

import com.chrisandjo.decimate.types.Types._

/**
 * Created by IntelliJ IDEA.
 * User: chris
 * Date: 16/05/2013
 * Time: 21:12
 * Copyright (c) Chris Myers 2013
 */
object Encoder {

  def getCommand(ffmpegWrapper: String, ffmpegBin: String,
                 videoFile: String) = s"$ffmpegWrapper $ffmpegBin $videoFile   /tmp/out.mp4"

  def callFfmpeg(command: String): IO[Stream[String]] = IO {
    Process(command).lines_!
  }

  def encode(fileName: String): ReaderT[OptionIO, Config, Unit] = (for {
    ffmpegWrapper <- findFfmpegWrapper
    ffmpegBin <- findFfmpeg
    stream <- callFfmpeg(getCommand(ffmpegWrapper, ffmpegBin, fileName)).liftM[OptionT].liftReaderT[Config]
  } yield stream).map(_ foreach println)
}
