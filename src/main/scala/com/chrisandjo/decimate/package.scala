package com.chrisandjo

import scalaz._, Scalaz._
import scalaz.effect.IO
import scala.language.higherKinds
import com.chrisandjo.decimate.io.Config

package object decimate {
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

  type EitherIO[+E,+A] = EitherT[IO, E, A]

  type EitherErrorIO[+A]=EitherT[IO, String, A]

  implicit class OptionOps[A](opt: Option[A]) {
    def toEither[E](error: E): \/[E, A] = \/.fromEither(opt.toRight(error))
  }

  def liftReaderEitherIO(a: IO[Stream[String]]): ReaderT[EitherErrorIO, Config, Stream[String]] =
    a.liftM[({type λ[α[+ _], b] = EitherT[α, String, b]})#λ]
      .liftReaderT[Config]


}
