package com.thomaschoo

import org.specs2.mutable.Specification

class KeyReaderSpec extends Specification {
  "KeyReader" >> {
    val keyReader = new KeyReader()

    "file listing should not be empty" >> {
      keyReader.listFiles() must not be empty
    }

    "files listed should" >> {
      val files = keyReader.listFiles()

      "contain text" >> {
        files forall (keyReader.retrieveContents(_).length > 0) must beTrue
      }

      "start with 'ssh-rsa'" >> {
        files forall (keyReader.retrieveContents(_).startsWith("ssh-rsa")) must beTrue
      }

      "not contain '.' in their name" >> {
        files forall (keyReader.getFilename(_).contains(".")) must beFalse
      }
    }
  }
}