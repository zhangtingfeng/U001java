import java.util.Date

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/*
idea创建Scala入门HelloWorld            https://blog.csdn.net/javaee_gao/article/details/82535171
* */


object HelloWorld {
  def main(args: Array[String]): Unit = {
    val arr01=Array(1,"2","ytret",4)
    arr01(1)=99
    //arr01.("dddd")
    println(arr01(1))
    arr01.foreach(println)

    val list01=List(11,22,33,44)
    list01.foreach(println)

    println("--------list02-----------")
    var list02=new ListBuffer[Int]()
    list02.+=(334)
    list02.foreach(println)

    println("--------set-----------")
    val set01:Set[Int]=Set(5643,8,9,9,5434,36553,45644,5)
    set01.foreach(println)
    println("hello world")

    println("--------scala.collection.mutable.Set-----------")
    import scala.collection.mutable.Set
    val set02:mutable.Set[Int]=Set(11,11)
    set02.add(888)
    set02.add(11)
    set02.foreach(println)


    println("--------end-----------")
    dun06("Hello")
    fun07(new Date(),"info","ok")
  }

  def dun06(a: String): Unit = {
    println(a)
  }

  def fun07(date:Date,tp:String,msg:String):Unit={
    println(s"$date\t$tp\t$msg")
  }
}


/*
现eclipse对Scala的支持并不是很好。用户体验比较差，比如联想速度比较慢等。由于在公司一直使用的Scala开发工具是Intellij IDEA（好吧，其实我使用Scala IDE的目的就是想试一下这两个各有什么优缺点），各方面感觉还不错，所以在此介绍一下这个开发环境。
*/