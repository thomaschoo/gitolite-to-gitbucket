package com.thomaschoo

object Runner extends App {
  val migration = new Migration()
  val keyReader = new KeyReader()

  val files = keyReader.listFiles()

  files foreach { f =>
    val content = keyReader.retrieveContents(f)
    migration.insertKey(content, keyReader.getFilenameWithoutExtension(f))
  }
}
