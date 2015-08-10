package com.thomaschoo

import java.io.File

import com.thomaschoo.helpers.GitoliteConfig

import scala.io.Source

class KeyReader {
  def listFiles(): List[File] = new File(GitoliteConfig.directory).listFiles.toList
  def retrieveContents(file: File): String = Source.fromFile(file).getLines().mkString
  def getFilename(file: File): String = file.getName
  def getFilenameWithoutExtension(file: File): String = getFilename(file).replaceAll("\\.[^.]*$", "")
}
