/**/
package com.ems.utilities.database

import cats.implicits._
import doobie._
import fs2.Stream
import shapeless._
import shapeless.ops.hlist._
import shapeless.ops.record._

object DBUtil {

  def void[A](a: A): Unit = (a, ())._2

  trait Dao[A] {
    type Key

    def insert(a: A, db:String = ""): ConnectionIO[Key]

    def find(k: Key ,db:String = ""): ConnectionIO[Option[A]]

    def findAll(db:String = ""): Stream[ConnectionIO, A]

    def findBy(k: Int, column:String,db:String=""): ConnectionIO[Option[A]]

    def findAllBy(k: Int, column:String,db:String=""): Stream[ConnectionIO, A]

    def update(k: Key, a: A ,db:String = ""): ConnectionIO[Int]

    def updateBy(k: Int,column:String, a: A ,db:String = ""): ConnectionIO[Int]

    def delete(k: Key,db:String = ""): ConnectionIO[Int]

  }

  object Dao {

    type Aux[A, K] = Dao[A] {type Key = K}

    def apply[A](implicit ev: Dao[A]): Aux[A, ev.Key] = ev

    def concatDot(db:String): String =
      if(db.nonEmpty)
        db.concat(".")
      else
        db

    object derive {
      def apply[A, K] = new Partial[A, K]

      class Partial[A, K] {
        def apply[R <: HList, S <: HList](table: String, keyCol: String)(
          implicit ev: LabelledGeneric.Aux[A, R],
          ra: Read[A],
          wa: Write[A],
          ks: Keys.Aux[R, S],
          tl: ToList[S, Symbol],
          rk: Read[K],
          wk: Write[K]
        ): Aux[A, K] =
          new Dao[A] {
            void(ev)
            type Key = K
            val cols = ks.apply.toList.map(_.name)
            def insert(a: A, db:String = ""): ConnectionIO[Key] =
              Update[A](
                s"""
                INSERT INTO ${concatDot(db)}$table (${cols.mkString(", ")})
                VALUES (${cols.as("?").mkString(", ")})
              """).withUniqueGeneratedKeys[Key](keyCol)(a)

            def find(key: Key, db:String = ""): ConnectionIO[Option[A]] =
              Query[Key, A](
                s"""
                SELECT ${cols.mkString(", ")}
                FROM ${concatDot(db)}$table
                WHERE $keyCol = ?
              """).option(key)

            def findAll(db:String = ""): Stream[ConnectionIO, A] =
              Query0[A](
                s"""
                SELECT ${cols.mkString(", ")}
                FROM ${concatDot(db)}$table
              """).stream

            def findBy(key: Int, column:String,db:String=""): ConnectionIO[Option[A]] =
              Query[Int, A](
                s"""
                SELECT ${cols.mkString(", ")}
                FROM ${concatDot(db)}$table
                WHERE $column = ?
              """).option(key)

            def findAllBy(key: Int, column:String,db:String=""): Stream[doobie.ConnectionIO, A] =
              Query0[A](
                s"""
                SELECT ${cols.mkString(", ")}
                FROM ${concatDot(db)}$table
                WHERE $column = $key
              """).stream

            def update(k: Key, a: A, db:String = ""): ConnectionIO[Int] =
              Update[(A, Key)](
                s"""
                UPDATE ${concatDot(db)}$table
                SET ${cols.map(_ + " = ?").mkString(", ")}
                WHERE $keyCol = ?
              """).run((a, k))

            def updateBy(k: Int, column:String, a: A, db:String = ""): ConnectionIO[Int] =
              Update[(A, Int)](
                s"""
                UPDATE ${concatDot(db)}$table
                SET ${cols.map(_ + " = ?").mkString(", ")}
                WHERE $column = ?
              """).run((a, k))

            def delete(k: Key, db:String = ""): ConnectionIO[Int] = {
              Update[Key](
                s"""
                DELETE FROM ${concatDot(db)}$table
                WHERE $keyCol = ?
              """).run(k)
            }
          }
      }
    }
  }
}
