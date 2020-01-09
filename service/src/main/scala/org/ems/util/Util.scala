/**/
package org.ems.util

import java.nio.file.{Path, Paths}
import java.time.LocalDateTime

import akka.http.scaladsl.server.directives.FileInfo

object Util {

  def getDestinationFile: String =
    ("""\d+""".r findAllIn LocalDateTime
      .now()
      .toString).mkString("")

  def getDestPath(dest: String, fileInfo: FileInfo, dir:String): Path =
    Paths.get(
      System
        .getProperty("user.dir") + s"/Images/$dir/"
    ) resolve dest + fileInfo.fileName

}
