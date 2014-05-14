package com.chrisandjo.decimate.io

import scalaz._, Scalaz._
import com.chrisandjo.decimate.time.Time._
import scalaz.effect.IO
import com.chrisandjo.decimate.model.{Actions, CleanUp, ReportStatus}

/**
 * Copyright (c) Chris Myers 2014
 */
object StreamProcessor {

  trait Percentage

  def Percentage[A](a: A): A @@ Percentage = Tag[A, Percentage](a)

  case class Streams[A, B](progressStream: Stream[A], completedStream: Stream[B])


  def processStreamNotLazy(duration: Double @@ Seconds, s: Stream[String]): Streams[String, IO[Unit]] = {
    val streams: (Stream[String], Stream[String]) = s.partition(_.startsWith("frame"))
    Streams(streams._1.
      collect(Function.unlift(extractTime)).
      map(createPercentage(duration) _ andThen convertToString),
      streams._2.filter(_.contains("~DONE~")).map(_ => IO {
        println("Done")
      }))
  }


  def processStream(duration: Double @@ Seconds, s: Stream[String]): Stream[Actions] =
    s.collect(Function.unlift {
      l =>
        if (l.startsWith("frame"))
          extractTime(l).map(createPercentage(duration) _ andThen convertToString).map(ReportStatus)
        else if (l.contains("~DONE~"))
          Some(CleanUp)
        else
          None
    })


  def processStream2(duration: Double @@ Seconds, s: Stream[String]): Streams[String, IO[Unit]] = {
    Streams(s.filter(_.startsWith("frame")).
      collect(Function.unlift(extractTime)).
      map(createPercentage(duration) _ andThen convertToString),
      Stream(IO.ioUnit))
  }


  def createPercentage(duration: Double @@ Seconds)(time: Double @@ Seconds): Int @@ Percentage =
    Percentage((time / duration * 100).toInt)

  def convertToString(p: Int @@ Percentage) = s"$p%"
}
