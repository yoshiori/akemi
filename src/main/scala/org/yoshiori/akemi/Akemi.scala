package org.yoshiori.akemi

import com.twitter.conversions.time._
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.service.Backoff
import com.twitter.finagle.kestrel.{ReadHandle, Client => KestrelClient}
import com.twitter.finagle.redis.{Client => RedisClient}
import com.twitter.util.JavaTimer

import org.jboss.netty.util.CharsetUtil

import grizzled.slf4j.Logging

import org.yoshiori.akemi.conf.AkemiConfig


object Akemi {
  def apply(conf:AkemiConfig) = {
    new Akemi(conf)
  }
}

class Akemi(conf:AkemiConfig) extends Logging {

  val DELETE_KEY = "delete_key"

  def run(){
    info("run")
    val kestrelClient = KestrelClient(conf.kestrelServer)
    val redisClient = RedisClient(conf.redisServer)
    val readHandles: Seq[ReadHandle] = {
      val timer = new JavaTimer(isDaemon = true)
      val retryBackoffs = Backoff.const(10.milliseconds)
      Seq(kestrelClient.readReliably(DELETE_KEY, timer, retryBackoffs))
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
