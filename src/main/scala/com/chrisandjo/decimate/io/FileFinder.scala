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

  def findFfmpeg:Reader[Config, IO[Option[String]]] = Reader {c=>findFile(c.ffmpegLocations).run}

  def findFfmpegWrapper:Reader[Config, IO[Option[String]]] = Reader {c=>findFile(c.ffmpegWrapperLocations).run}
}
