import net.ruippeixotog.scalascraper.browser.JsoupBrowser

import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.model.Element



val browser = JsoupBrowser()
val doc = browser.get("http://www.lotto-8.com/listltobig.asp?indexpage=1&orderby=new")
//doc.body

//val title: String = doc >> text("h1")

// Extract the elements with class "auto-style4"
val items: List[Element] = doc >> elementList(".auto-style5")

val itemContents: List[String] = items.map(_ >> text(".auto-style5"))


val colContents: List[String] = itemContents.drop(4)//without headers
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
val colNames: List[String] = List("num1","num2","num3","num4","num5","num6","special")





import org.saddle._
//http://stackoverflow.com/questions/20293014/concatenating-saddle-series-into-one-frame
val v = date.toVec
val idx = Index(v)

val col1 = Series(six1.toVec,idx)
val col2 = Series(six2.toVec,idx)
val col3 = Series(six3.toVec,idx)
val col4 = Series(six4.toVec,idx)
val col5 = Series(six5.toVec,idx)
val col6 = Series(six6.toVec,idx)
val col7 = Series(special.toVec,idx)

val f = Frame(colNames(0) -> col1,colNames(1) -> col2,colNames(2) -> col3,colNames(3) -> col4,colNames(4) -> col5,colNames(5) -> col6,colNames(6) -> col7)

val f2 = Frame(colNames(0) -> col1,colNames(1) -> col2,colNames(2) -> col3,colNames(3) -> col4,colNames(4) -> col5,colNames(5) -> col6,colNames(6) -> col7)

val f3 = f.concat(f2)

val f4 = f.join(f2, how=index.LeftJoin)
//val double = f.mapValues { case elt => elt.toDouble }

//val sixNum = six.toVec
//val specialNum = special.toVec
//val s1 = Series(sixNum,idx)
//val s2 = Series(specialNum,idx)
//val f =  Frame(colNames(0) -> s1, colNames(1) -> s2)