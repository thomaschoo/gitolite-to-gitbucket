package com.thomaschoo

import java.io.File

import scala.io.Source

import com.thomaschoo.helpers.GitoliteConfig

class KeyReader {
  def listFiles(): List[File] = new File(GitoliteConfig.directory).listFiles.toList
  def retrieveContents(file: File): String = Source.fromFile(file).getLines().mkString
}
