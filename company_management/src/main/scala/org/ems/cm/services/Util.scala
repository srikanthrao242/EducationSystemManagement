/**/
package org.ems.cm.services

import java.awt.image.{BufferedImage, DataBufferByte}
import java.io.{ByteArrayOutputStream, File}

import akka.http.scaladsl.model.HttpEntity.{ChunkStreamPart, Chunked}
import akka.http.scaladsl.model.{ContentType, ContentTypes, HttpResponse, MediaTypes}
import akka.stream.scaladsl.Source
import javax.imageio.ImageIO

object Util {

  def contentType(fileName: String): ContentType =
    (fileName.split("\\.").last) match {
      case "jpg"  => ContentType(MediaTypes.`image/jpeg`)
      case "png"  => ContentType(MediaTypes.`image/png`)
      case "html" => ContentTypes.`text/html(UTF-8)`
    }

  val fileContentsSource: (String, String) => Source[ChunkStreamPart, _] =
    (fileName, fType) =>
      Source
        .single(ImageIO.read(new File(fileName)))
      .map(v=>{
        val baos = new ByteArrayOutputStream()
        ImageIO.write(v,fType,baos)
        ChunkStreamPart.apply(baos.toByteArray)
      })

  val fileEntityResponse: (String) => HttpResponse = { (fileName) =>
    val fType = contentType(fileName)
    HttpResponse(
      entity =
        Chunked(fType, fileContentsSource(fileName, fType.mediaType.toString()))
    )
  }

}
