package org.yoshiori.akemi

import com.twitter.util.Eval

import grizzled.slf4j.Logger

import org.yoshiori.akemi.conf.AkemiConfig

object AkemiMain {

  val logger = Logger("AkemiMain")

  def main(args: Array[String]) {
    logger.info("start Akemi")
    val eval = new Eval
    val conf = eval[AkemiConfig](new java.io.File("config/AkemiConfigSample.scala"))

    logger.info("kestrel server is %s".format(conf.kestrelServer))
    logger.info("redis server is %s".format(conf.redisServer))
    val akemi = Akemi(conf)
    akemi.run
  }
}
