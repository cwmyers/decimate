package com.chrisandjo.decimate.io

import scalaz._
import scalaz.effect.IO
import java.io.File

/**
 * Created by IntelliJ IDEA.
 * User: chris
 * Date: 21/05/2013
 * Time: 20:59
 * Copyright (c) Chris Myers 2010
 */
object FileFinder {
  def findFile(path:List[String]): OptionT[IO,String] = OptionT[IO,String](IO{path.find(new File(_).exists)})

  def findFfmpeg:Reader[Config, List[String]] = Reader {c=>c.ffmpegLocations}

  def findFfmpegWrapper = findFile(List("/Users/grailsuser/decimate/src/main/scripts/ffmpeg_wrapper.sh","/Users/chris/Development/decimate/src/main/scripts/ffmpeg_wrapper.sh"))
}
