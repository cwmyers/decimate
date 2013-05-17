package com.chrisandjo.decimate.io

import scalaz.effect.IO
import scala.sys.process.Process

/**
 * Created by IntelliJ IDEA.
 * User: chris
 * Date: 16/05/2013
 * Time: 21:12
 * Copyright (c) Chris Myers 2010
 */
object Encoder {
      def encode(fileName:String):IO[Stream[String]] = {
        val ffmpegIO  = IO {
          Process("/Users/grailsuser/decimate/ffmpeg.sh").lines_!
        }


        val filteredOutput = ffmpegIO map { a : Stream[String] =>
          a filter {_.startsWith("frame=")}

        }

        filteredOutput


      }
}
