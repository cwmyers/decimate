package com.chrisandjo.decimate.io

import scalaz._
import Scalaz._


object Main {
  def main(args: Array[String]) {


    val config: Config = Config(ffmpegLocations = List("/usr/local/bin/ffmpeg", "/usr/bin/ffmpeg"))
    Encoder.encode("/Users/chris/Development/skyvideo-encoderbot/test/resources/video/test-master.mov")(config).unsafePerformIO()
  }
}
