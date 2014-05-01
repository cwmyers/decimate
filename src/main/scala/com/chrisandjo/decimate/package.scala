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

  type REIO[A] = ReaderT[EitherErrorIO, Config, A]

  type OptionIO[+A] = OptionT[IO, A]

  type EitherIO[+E,+A] = EitherT[IO, E, A]

  def EitherIO[E,A](i: IO[E\/A]):EitherIO[E,A] = EitherT[IO, E, A](i)

  type EitherErrorIO[+A]=EitherT[IO, String, A]

  def liftReaderEitherIO(a: IO[Stream[String]]): ReaderT[EitherErrorIO, Config, Stream[String]] =
    a.liftM[({type λ[α[+ _], b] = EitherT[α, String, b]})#λ]
      .liftReaderT[Config]

  sealed trait FilePath
  def FilePath[A](a: A): A @@ FilePath = Tag[A, FilePath](a)

  type FilePaths = List[String @@ FilePath]

}
