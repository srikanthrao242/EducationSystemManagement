/**/
package org.ems.em.database


import cats.implicits._
import doobie._
import fs2.Stream
import shapeless._
import shapeless.ops.record._
import shapeless.ops.hlist._

object DBUtil {

  def void[A](a: A): Unit = (a, ())._2

  trait Dao[A] {
    type Key

    def insert(db:String, a: A): ConnectionIO[Key]

    def find(db:String,k: Key): ConnectionIO[Option[A]]

    def findBy(db:String,k: Int, column:String): ConnectionIO[Option[A]]

    def findAll(db:String): Stream[ConnectionIO, A]

    def update(db:String,k: Key, a: A): ConnectionIO[Int]

    def delete(db:String,k: Key): ConnectionIO[Int]
  }

  object Dao {

    type Aux[A, K] = Dao[A] {type Key = K}

    def apply[A](implicit ev: Dao[A]): Aux[A, ev.Key] = ev

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
            def insert(db:String,a: A): ConnectionIO[Key] =
              Update[A](
                s"""
                INSERT INTO $db.$table (${cols.mkString(", ")})
                VALUES (${cols.as("?").mkString(", ")})
              """).withUniqueGeneratedKeys[Key](keyCol)(a)

            def find(db:String,key: Key): ConnectionIO[Option[A]] =
              Query[Key, A](
                s"""
                SELECT ${cols.mkString(", ")}
                FROM $db.$table
                WHERE $keyCol = ?
              """).option(key)

            def findBy(db:String,key: Int, column:String): ConnectionIO[Option[A]] =
              Query[Int, A](
                s"""
                SELECT ${cols.mkString(", ")}
                FROM $db.$table
                WHERE $column = ?
              """).option(key)

            def findAll(db:String): Stream[ConnectionIO, A] =
              Query0[A](
                s"""
                SELECT ${cols.mkString(", ")}
                FROM $db.$table
              """).stream

            def update(db:String,k: Key, a: A): ConnectionIO[Int] =
              Update[(A, Key)](
                s"""
                UPDATE $db.$table
                SET ${cols.map(_ + " = ?").mkString(", ")}
                WHERE $keyCol = ?
              """).run((a, k))

            def delete(db:String,k: Key): ConnectionIO[Int] = {
              Update[Key](
                s"""
                DELETE FROM $db.$table
                WHERE $keyCol = ?
              """).run(k)
            }
          }
      }
    }
  }
}
