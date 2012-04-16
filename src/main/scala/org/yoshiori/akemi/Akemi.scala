package org.yoshiori.akemi

import com.twitter.conversions.time._
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.service.Backoff
import com.twitter.finagle.kestrel.{ReadHandle, Client => KestrelClient}
import com.twitter.finagle.redis.{Client => RedisClient}
import com.twitter.util.{JavaTimer, Eval}



import org.jboss.netty.util.CharsetUtil

import java.util.concurrent.atomic.AtomicBoolean

import grizzled.slf4j.Logger

object MainApp {
  val deleteKey = "delete_key"
  val logger = Logger("hoge")
  def main(args: Array[String]) {
    logger.info("start Akemi")
    val eval = new Eval
    val conf = eval[AkemiConfig](new java.io.File("config/AkemiConfigSample.scala"))

    logger.info("kestrel server is %s".format(conf.kestrelServer))
    logger.info("redis server is %s".format(conf.redisServer))

    val kestrelClient = KestrelClient(conf.kestrelServer)
    val redisClient = RedisClient(conf.redisServer)
    println("start")
    val readHandles: Seq[ReadHandle] = {
      val queueName = deleteKey
      val timer = new JavaTimer(isDaemon = true)
      val retryBackoffs = Backoff.const(10.milliseconds)
      Seq(kestrelClient.readReliably(queueName, timer, retryBackoffs))
    }
    val readHandle: ReadHandle = ReadHandle.merged(readHandles)
    readHandle.messages foreach { msg =>
      try {
        val key = msg.bytes.toString(CharsetUtil.UTF_8) 
        println(key)
        redisClient.del(Seq(key))
      } finally {
        msg.ack.sync()
      }
    }
  }
}
