import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model.Element

import scala.collection.mutable.ListBuffer

//create buffer list
var allDate = new ListBuffer[String]()
var allSix1 = new ListBuffer[String]()
var allSix2 = new ListBuffer[String]()
var allSix3 = new ListBuffer[String]()
var allSix4 = new ListBuffer[String]()
var allSix5 = new ListBuffer[String]()
var allSix6 = new ListBuffer[String]()
var allSpecial = new ListBuffer[String]()

for(iter <- 1 to 45) {
  //scrap content from web
  val browser = JsoupBrowser()
  val doc = browser.get("http://www.lotto-8.com/listltobig.asp?indexpage="+iter+"&orderby=new")
  val items: List[Element] = doc >> elementList(".auto-style5")
  val itemContents: List[String] = items.map(_ >> text(".auto-style5"))
  val colContents: List[String] = itemContents.drop(4)//without headers
  //extract content element
  val date:List[String] = colContents.zipWithIndex.filter {case (data,idx)=> idx%4==0}.map(_._1)
  val six:List[String] = colContents.zipWithIndex.filter {case (data,idx)=> idx%4==1}.map(_._1)
  val splitSix = six.map(_.split(",").toVector)
  val six1 = splitSix.map(_.zipWithIndex).flatMap(_.filter{case (data,idx)=>idx%6==0}).map(_._1)
  val six2 = splitSix.map(_.zipWithIndex).flatMap(_.filter{case (data,idx)=>idx%6==1}).map(_._1)
  val six3 = splitSix.map(_.zipWithIndex).flatMap(_.filter{case (data,idx)=>idx%6==2}).map(_._1)
  val six4 = splitSix.map(_.zipWithIndex).flatMap(_.filter{case (data,idx)=>idx%6==3}).map(_._1)
  val six5 = splitSix.map(_.zipWithIndex).flatMap(_.filter{case (data,idx)=>idx%6==4}).map(_._1)
  val six6 = splitSix.map(_.zipWithIndex).flatMap(_.filter{case (data,idx)=>idx%6==5}).map(_._1)
  val special: List[String] = colContents.zipWithIndex.filter {case (data,idx)=> idx%4==2}.map(_._1)
  allDate ++= date
  allSix1 ++= six1
  allSix2 ++= six2
  allSix3 ++= six3
  allSix4 ++= six4
  allSix5 ++= six5
  allSix6 ++= six6
  allSpecial ++= special
  allDate ++= date
}

//list buffer to list
val dateList = allDate.toList
val allSix1List = allSix1.toList
val allSix2List = allSix2.toList
val allSix3List = allSix3.toList
val allSix4List = allSix4.toList
val allSix5List = allSix5.toList
val allSix6List = allSix6.toList
val allSpecialList = allSpecial.toList

val colNames: List[String] = List("num1","num2","num3","num4","num5","num6","special")





import org.saddle._
//http://stackoverflow.com/questions/20293014/concatenating-saddle-series-into-one-frame
val v = dateList.toVec
val idx = Index(v)

val col1 = Series(allSix1List.toVec,idx)
val col2 = Series(allSix2List.toVec,idx)
val col3 = Series(allSix3List.toVec,idx)
val col4 = Series(allSix4List.toVec,idx)
val col5 = Series(allSix5List.toVec,idx)
val col6 = Series(allSix6List.toVec,idx)
val col7 = Series(allSpecialList.toVec,idx)

val f = Frame(colNames(0) -> col1,colNames(1) -> col2,colNames(2) -> col3,colNames(3) -> col4,colNames(4) -> col5,colNames(5) -> col6,colNames(6) -> col7)
//
//val f2 = Frame(colNames(0) -> col1,colNames(1) -> col2,colNames(2) -> col3,colNames(3) -> col4,colNames(4) -> col5,colNames(5) -> col6,colNames(6) -> col7)
//
//val f3 = f.concat(f2)
//
//val f4 = f.join(f2, how=index.LeftJoin)
//val double = f.mapValues { case elt => elt.toDouble }

//val sixNum = six.toVec
//val specialNum = special.toVec
//val s1 = Series(sixNum,idx)
//val s2 = Series(specialNum,idx)
//val f =  Frame(colNames(0) -> s1, colNames(1) -> s2)