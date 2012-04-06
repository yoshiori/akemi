package org.yoshiori

import com.twitter.conversions.time._
import com.twitter.finagle.builder.ClientBuilder
import com.twitter.finagle.kestrel.{ReadHandle, Client => KestrelClient}
import com.twitter.finagle.redis.{Client => RedisClient}
import com.twitter.util.JavaTimer
import com.twitter.finagle.service.Backoff
import org.jboss.netty.util.CharsetUtil
import java.util.concurrent.atomic.AtomicBoolean

object MainApp {
  val deleteKey = "delete_key"

  def main(args: Array[String]) {
    val kestrelClient = KestrelClient("localhost:22133")
    val redisClient = RedisClient("localhost:6379")
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
