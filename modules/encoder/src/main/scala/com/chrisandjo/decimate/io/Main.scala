package com.chrisandjo.decimate.io


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


    Encoder.encode("/Users/chris/Development/skyvideo-encoderbot/test/resources/video/test-master.mov").unsafePerformIO
  }
}
