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

  def getStream(fileName: String): OptionT[IO, Stream[String]] = for {
    ffmpegBin <- OptionT[IO, String](findFfmpeg)
    ffmpegWrapper <- OptionT[IO, String](findFfmpegWrapper)
    stream <- callFfmpeg(getCommand(ffmpegWrapper, ffmpegBin, fileName)).liftM[OptionT]
  } yield stream


  def encode(fileName: String): ReaderWriterState[Config, List[String], Int, IO[Unit]] = {
    ReaderWriterStateT {
      case (r, s) => ( List("dfs"),
        getStream(fileName) map {
          a: Stream[String] =>
            a map {
              _.length
            } foreach (println)

        } getOrElse (Unit.box {
          println("nothing")
        })
        , s)
    }
  }
}
