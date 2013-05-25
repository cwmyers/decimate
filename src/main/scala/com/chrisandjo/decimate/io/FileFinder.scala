package com.chrisandjo.decimate.io

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
  def findFile(path:List[String]): IO[Option[String]] = IO{path.find(new File(_).exists)}

  def findFfmpeg = findFile(List("/usr/local/bin/ffmpeg", "/usr/bin/ffmpeg"))

  def findFfmpegWrapper = findFile(List("/Users/grailsuser/decimate/src/main/scripts/ffmpeg_wrapper.sh","/Users/chris/Development/decimate/src/main/scripts/ffmpeg_wrapper.sh"))
}
