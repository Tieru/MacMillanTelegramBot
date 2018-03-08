package tools

import java.io.FileNotFoundException

import scala.io.Source
import scala.util.Try

trait RawResourceLoader {

  def rawResource(resource: String): String =
    Try(Source.fromResource(resource).mkString)
      .getOrElse(throw new FileNotFoundException(resource))

}
