package com.chrisandjo.decimate.io

import scalaz._
import Scalaz._
import scalaz.effect.IO
import scala.sys.process.Process
import FileFinder._

/**
 * Created by IntelliJ IDEA.
 * User: chris
 * Date: 16/05/2013
 * Time: 21:12
 * Copyright (c) Chris Myers 2013
 */
object Encoder {

  def getCommand(ffmpegWrapper: String, ffmpegBin: String,
                 videoFile: String) = s"$ffmpegWrapper $ffmpegBin $videoFile  '-vcodec libx264 -s 1024x576' /tmp/out.mp4"

  def callFfmpeg(command: String): IO[Stream[String]] = IO {
    Process(command).lines_!
  }

  def getStream(fileName: String, ffmpegBin: OptionT[IO,String], ffmpegWrapper: OptionT[IO,String]): OptionT[IO, Stream[String]] = for {
    w <- ffmpegWrapper
    b <- ffmpegBin
    stream <- callFfmpeg(getCommand(w, b, fileName)).liftM[OptionT]
  } yield stream



  def encode(fileName:String): Reader[Config,IO[Unit]] = for {
    ffmpegWrapper <- findFfmpegWrapper >=> Reader {findFile(_)}
    ffmpegBin <- findFfmpeg >=> Reader { findFile(_) }
  } yield (getStream(fileName, ffmpegBin, ffmpegWrapper) map ( _ foreach (println)) getOrElse (Unit.box {}))

}
