package com.djaramillo.resumen


import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

object InfoMdb extends App{
  
  
     val conf = new SparkConf().setMaster("local").setAppName("BuscaFml")
      val sc = new SparkContext(conf)
  
    val inputModulo = sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/db/moduloBD/part-00000")
  
    val paresElementoModulo= inputModulo.map(x => {
      val spl=x.split(";")     
     (spl(0), spl(1).toInt) 
    });
     
     

     
       
    val inputClase= sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/db/clasesBD/part-00000")
  
    val paresElementoClase= inputClase.map(x => {
      val spl=x.split(";")     
     (spl(0), spl(1).toInt) 
    });
    
    
    
   val inputForm= sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/db/formBD/part-00000")
  
    val paresElementoForm= inputForm.map(x => {
      val spl=x.split(";")     
     (spl(0), spl(1).toInt) 
    });
   
   
   val inputDisenio= sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/db/designerBD/part-00000")
  
    val paresElementoDisenio= inputDisenio.map(x => {
      val spl=x.split(";")     
     (spl(0), spl(1).toInt) 
    });
   
   
   
   println("paresElementoForm")
  paresElementoForm.filter( (x) => x._2>0).foreach((x)=>println(x) )
   
  
  println("paresElementoDisenio")
   paresElementoDisenio.filter( (x) => x._2>0)  .foreach((x)=>println(x) )
   
   
   println("paresElementoModulo")
        paresElementoModulo.filter( (x) => x._2>0).foreach((x)=>println(x) )
     
        
    println("paresElementoClase")    
     paresElementoClase.filter( (x) => x._2>0).foreach((x)=>println(x) )
   
   
     
   

}