package com.chrisandjo
package decimate
package io

import scalaz._,Scalaz._
import scalaz.effect.IO
import java.io.File

object FileFinder {
  def findFile(path: List[String @@ FilePath]) =
    EitherIO[String, String @@ FilePath](IO {
      path.find(new File(_).exists) \/> s"File not found in $path"
    })

  def findFfmpeg = Kleisli[EitherErrorIO, Config, String @@ FilePath](c => findFile(c.ffmpegLocations))

  def findFfmpegWrapper = Kleisli[EitherErrorIO, Config, String @@ FilePath](c => findFile(c.ffmpegWrapperLocations))

  def findFfprobe = Kleisli[EitherErrorIO, Config, String @@ FilePath](c => findFile(c.ffprobeLocations))

  def findFfprobeWrapper = Kleisli[EitherErrorIO, Config, String @@ FilePath](c => findFile(c.ffprobeWrapperLocations))


}
