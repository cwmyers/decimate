package com.chrisandjo.decimate.io

import scalaz._
import scalaz.effect.IO
import com.chrisandjo.decimate.types.Types._
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

  def findFfmpeg: ReaderT[OptionIO, Config, String] =
    Kleisli[OptionIO, Config, String](c => findFile(c.ffmpegLocations))

  def findFfmpegWrapper: ReaderT[OptionIO, Config, String] =
    Kleisli[OptionIO, Config, String](c => findFile(c.ffmpegWrapperLocations))

}
