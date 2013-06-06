package com.chrisandjo.decimate.io

import scalaz._
import Scalaz._


object Main {
  def main(args: Array[String]) {


    val config: Config = Config(ffmpegLocations = List("/usr/local/bin/ffmpeg", "/usr/bin/ffmpeg"),
    ffmpegWrapperLocations = List("/Users/grailsuser/decimate/src/main/scripts/ffmpeg_wrapper.sh","/Users/chris/Development/decimate/src/main/scripts/ffmpeg_wrapper.sh"))
    Encoder.encode("/Users/chris/Development/skyvideo-encoderbot/test/resources/video/test-master.mov")(config).unsafePerformIO()
  }
}
