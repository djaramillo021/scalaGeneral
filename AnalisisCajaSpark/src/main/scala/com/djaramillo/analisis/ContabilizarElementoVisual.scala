package com.djaramillo.analisis

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import com.djaramillo.rutinas.visual.ElementosVisual

object ContabilizarElementoVisual extends App {

  val conf = new SparkConf().setMaster("local").setAppName("BuscaFml")
  val sc = new SparkContext(conf)
  // Load our input data.
   
   
  val inputTiposElementos = sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/entrada/visual/Plataforma\\ de\\ Caja.vbp")
  
  
  val paresElemento=inputTiposElementos.map((x)=>{
       ElementosVisual.obtenerElemento(x); 
  }).filter((f)=>{f!=null} );
 val salidaPares= paresElemento.map((x)=>{
   x._1+";"+x._2+";"+x._3+";"+x._4;
 });
 //paresElemento.foreach((x)=>{println(x)})
  salidaPares.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/elementosVisual")
  
  
}