package com.chrisandjo.decimate.io

import scalaz._
import effect._
import scala.sys.process._

object Main {
  def main(args: Array[String]) {
    //    val dirInIO = io { new File(System.getProperty("user.dir")) }
    //
    //    def filesInIO = for (dir <- dirInIO) yield dir.listFiles()
    //
    //    def namesInIO = for (theFiles ← filesInIO) yield theFiles.map(_.getName())
    //
    //    def printNamesInIO = for (all ← namesInIO) yield all.map(println(_))
    //
    //    val result = printNamesInIO
    //
    //    println("Nothings happened yet, lets go!")
    //    result.unsafePerformIO


    val ffmpegIO: IO[ProcessIO] = IO {
      val pb = Process("ffmpeg")

      val pio = new ProcessIO(_ => (),
        stdout => scala.io.Source.fromInputStream(stdout)
                .getLines.foreach(println),
        _ => ())
      pb.run(pio)
      pio
    }

    ffmpegIO map()

    val result = ffmpegIO

    println("Nothings happened yet, lets go!")
    result.unsafePerformIO
  }
}
