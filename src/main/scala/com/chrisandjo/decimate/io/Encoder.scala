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
      def encode(fileName:String):IO[Unit] = {
        val ffmpegIO  = IO {
          Process("/usr/local/bin/ffmpeg -y -i "+fileName+" -vcodec libx264 -b:v 2000k -refs 3 -keyint_min 29 -x264opts trellis=0 -acodec libfaac -ar 44100 -ab 160k -s 1024x576 /tmp/out.mp4").lines_!
        }



        ffmpegIO map { a : Stream[String] =>
          a map {_.length} foreach(println)

        }


      }
}
