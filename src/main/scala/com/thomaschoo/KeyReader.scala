package com.thomaschoo

import java.io.File

import com.thomaschoo.helpers.Config

import scala.io.Source

class KeyReader {
  def listFiles(): List[File] = new File(Config.directory).listFiles.toList
  def retrieveContents(file: File): String = Source.fromFile(file).getLines().mkString
  def getFilename(file: File): String = file.getName.take(file.getName.lastIndexOf("."))
}
