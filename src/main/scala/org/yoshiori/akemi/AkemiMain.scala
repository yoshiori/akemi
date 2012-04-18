package org.yoshiori.akemi

import com.twitter.util.Eval

import grizzled.slf4j.Logger

import org.yoshiori.akemi.conf.AkemiConfig

case class InitParam(path: String, isCheck:Boolean, isHelp:Boolean)

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

  def parse(args: Array[String]): InitParam = {

    // 空だったらヘルプモードに
    if (args.isEmpty) {
      return InitParam("",false,true)
    }

    val _args = args.toList
    var path = ""
    var isCheck = false
    var isHelp = false
    
    def _parse(rest: List[String]): InitParam = {
      rest match {
        case "-p" :: _path :: _rest => {
          path = _path
          _parse(_rest)
        }
        case "-c" :: _rest => {
          isCheck = true
          _parse(_rest)
        }
        case "--help" :: _rest => {
          isHelp = true
          _parse(_rest)
        }
        case _ => {
          InitParam(path,isCheck,isHelp)
        }
      }
    }
    _parse(_args)
  }
}
