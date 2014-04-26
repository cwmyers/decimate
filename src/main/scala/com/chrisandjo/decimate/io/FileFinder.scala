package com.chrisandjo
package decimate
package io

import scalaz._
import scalaz.effect.IO
import java.io.File

object FileFinder {
  def findFile(path: List[String]): EitherT[IO, String, String] =
    EitherT[IO, String, String](IO {
      path.find(new File(_).exists).toEither(s"File not found in $path")
    })

  def findFfmpeg: ReaderT[EitherErrorIO, Config, String] =
    Kleisli[EitherErrorIO, Config, String](c => findFile(c.ffmpegLocations))

  def findFfmpegWrapper: ReaderT[EitherErrorIO, Config, String] =
    Kleisli[EitherErrorIO, Config, String](c => findFile(c.ffmpegWrapperLocations))


}
