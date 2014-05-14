package com.chrisandjo.decimate.model

import scalaz.@@
import com.chrisandjo.decimate.io.StreamProcessor.Percentage

sealed trait Actions

case object CleanUp extends Actions

case class ReportStatus(percentageComplete: String) extends Actions

case class ReportError(message: String) extends Actions
