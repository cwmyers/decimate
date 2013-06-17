package com.chrisandjo.decimate.types

import scalaz.{OptionT, Leibniz, UnapplyCo}
import scalaz.effect.IO

/**
 * Created by IntelliJ IDEA.
 * User: chris
 * Date: 12/06/2013
 * Time: 20:25
 * Copyright (c) Chris Myers 2010
 */
object Types {

  implicit def unapplyMFA1[TC[_[_]], F[+ _], M0[F[+ _], + _], A0](implicit
                                                                  TC0: TC[({type L[x] = M0[F, x]})#L]): UnapplyCo[TC, M0[F, A0]] {
    type M[+X] = M0[F, X]
    type A = A0
  } = new UnapplyCo[TC, M0[F, A0]] {
    type M[+X] = M0[F, X]
    type A = A0

    def TC = TC0

    def leibniz = Leibniz.refl
  }

  type OptionIO[+A] = OptionT[IO, A]


}
