package com.chrisandjo.decimate.io

import scalaz._
import com.chrisandjo.decimate.time.Time._

/**
 * Copyright (c) Chris Myers 2014
 */
object StreamProcessor {

  trait Percentage

  def Percentage[A](a: A): A @@ Percentage = Tag[A, Percentage](a)


  def processStream(duration: Double @@ Seconds, s: Stream[String]): Stream[String] =
    s.filter(_.startsWith("frame")).
      collect(Function.unlift(extractTime)).
      map(createPercentage(duration) _ andThen convertToString)


  def createPercentage(duration: Double @@ Seconds)(time: Double @@ Seconds): Int @@ Percentage =
    Percentage((time / duration * 100).toInt)

  def convertToString(p: Int @@ Percentage) = s"$p%"
}
