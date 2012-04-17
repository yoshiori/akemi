package org.yoshiori.akemi.conf

import org.specs2.mutable._
import com.twitter.util.Eval

class AkemiConfigSpec extends Specification{
  "普通に設定した場合" should {
    val eval = new Eval
    val conf = eval[AkemiConfig](new java.io.File(getClass.getResource("/AkemiConfigTest1.scala").toURI()))

    "kestrel server が設定されている" in {
      conf.kestrelServer must_== "localhost:22133"
    }

    "redis server が設定されている" in {
      conf.redisServer must_== "localhost:6379"
    }

  }
}
