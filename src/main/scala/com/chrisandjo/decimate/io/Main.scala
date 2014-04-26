package com.chrisandjo.decimate.io

import scalaz.effect.SafeApp


object Main extends SafeApp {
  override def runl(args: List[String])= {
    val config: Config = Config(
      ffmpegLocations = List("/usr/local/bin/ffmpeg", "/usr/bin/ffmpeg"),
      ffmpegWrapperLocations = List(
        "/Users/grailsuser/decimate/src/main/scripts/ffmpeg_wrapper.sh",
        "/Users/chris/Development/decimate/src/main/scripts/ffmpeg_wrapper.sh")
    )
    def printStream(s: Stream[String]) =
      s.foreach(println)


    val fileName = "/Users/chris/Development/skyvideo-encoderbot/test/resources/video/test-master.mov"
    val streamOrError = Encoder.encode(fileName)(config)
    streamOrError.fold(println(_), printStream)
  }
}
