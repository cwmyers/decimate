package com.chrisandjo.decimate.time

import scalaz._, Scalaz._

/**
 * Copyright (c) Chris Myers 2014
 */
object Time {
  sealed trait Seconds
  def Seconds[A](a: A): A @@ Seconds = Tag[A, Seconds](a)

  def extractTime(s:String):Option[Double @@ Seconds] = {
    val timeRegex = """(\d\d):(\d\d):(\d\d\.\d+)""".r
    for {
      timeRegex(h,m,s) <- timeRegex findFirstIn s
      hours <- h.parseInt.toOption
      minutes <- m.parseInt.toOption
      seconds <- s.parseDouble.toOption
    } yield Seconds(hours * 3600 + minutes * 60 + seconds)
  }


}
