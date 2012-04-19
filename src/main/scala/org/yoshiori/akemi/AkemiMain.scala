package org.yoshiori.akemi

import java.io.FileNotFoundException

import com.twitter.util.Eval
import com.twitter.util.Eval._
import grizzled.slf4j.Logger


import org.yoshiori.akemi.conf.AkemiConfig

case class InitParam(path: String, isCheck:Boolean, isHelp:Boolean)

object AkemiMain {

  val logger = Logger("AkemiMain")
  val helpText = """
Usage:
       -c     conf check mode

       -p     conf file path

       --help display this help and exit
"""

  def main(args: Array[String]) {
    logger.info("start Akemi")
    parse(args) match {
      case InitParam( _, _, true ) => println(helpText)
      case InitParam(path, true, _ ) => println(check(path))
      case InitParam(path, _, _ ) => {
        val eval = new Eval
        val file = new java.io.File(path)
        logger.info("config file path => " + file.getAbsoluteFile())
        val conf = eval[AkemiConfig](file)
        val akemi = Akemi(conf)
        akemi.run
      }
    }
  }

  def check(path: String): String = {
    try {
      val eval = new Eval
      eval.check(new java.io.File(path))
      "success"
    } catch {
      case e: CompilerException => e.getMessage
      case e: FileNotFoundException => e.getMessage
    }
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
