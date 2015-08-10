package com.thomaschoo

import java.io.File

import com.thomaschoo.helpers.GitoliteConfig

import scala.io.Source

class KeyReader {
  def listFiles(): List[File] = new File(GitoliteConfig.directory).listFiles.toList
  def retrieveContents(file: File): String = Source.fromFile(file).getLines().mkString
}
