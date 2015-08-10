package com.thomaschoo

import com.thomaschoo.services._

object Runner extends App {
  val keyReader = new KeyReader()
  val files = keyReader.listFiles()

  val keys = files.foldLeft(Map[String, String]()) {
    case (accum, file) => accum + (keyReader.getFilename(file) -> keyReader.retrieveContents(file))
  }

  GitBucketDao.insertKeys(keys)
}
