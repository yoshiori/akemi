package org.yoshiori.akemi

import org.specs2.mutable._

class AkemiMainSpec extends Specification {

  "引数にパスが渡されていた場合（-p /）" should {
    val param = AkemiMain.parse(Array("-p","/"))

    "パスは /" in {
      param.path must be_==("/")
    }

    "チェックモードはオフ" in {
      param.isCheck must beFalse
    }
  }

  "引数にパスとチェックモードが渡されていた場合(-p / -c)" should {
    val param = AkemiMain.parse(Array("-p","/","-c"))

    "パスは /" in {
      param.path must be_==("/")
    }

    "チェックモードはオン" in {
      param.isCheck must beTrue
    }
  }

  "引数にパスとチェックモードが渡されていた場合(-c -p / )" should {
    val param = AkemiMain.parse(Array("-c","-p","/"))

    "パスは /" in {
      param.path must be_==("/")
    }

    "チェックモードはオン" in {
      param.isCheck must beTrue
    }
  }

  "引数にパスとチェックモードが渡されていた場合(-p -c / )" should {
    val param = AkemiMain.parse(Array("-p","-c","/"))

    "パスは -c(error)" in {
      param.path must be_==("-c")
    }

    "チェックモードはオフ(error) "in {
      param.isCheck must beFalse
    }
  }

  "引数にパスとhelpが渡されていた場合" should {
    val param = AkemiMain.parse(Array("-p","/","--help"))

    "help モードになる" in {
      param.isHelp must beTrue
    }
  }

  "引数に何も渡されなかった場合" should {
    val param = AkemiMain.parse(Array())

    "help モードになる" in {
      param.isHelp must beTrue
    }
  }
  
  "正常な conf のチェック" should {
    val path = getClass.getResource("/AkemiConfigTest1.scala").getPath
    "success が返ってくる" in {
      AkemiMain.check(path) must be_==("success")
    }
  }

  "不正な conf のチェック" should {
    val path = getClass.getResource("/AkemiConfigTest_error1.scala").getPath

    "エラーメッセージが返ってくる" in {
      AkemiMain.check(path) must not be_==("success")
    }
  }
}
