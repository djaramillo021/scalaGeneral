package com.djaramillo.resumen

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

object FormularioSolventa  extends App {

   val conf = new SparkConf().setMaster("local").setAppName("BuscaFml")
      val sc = new SparkContext(conf)
  
    val inputformSolventa = sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/entrada/analisis/formularioSolventa.txt")
  
     val inputformAlias = sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/alias/formAlias/part-00000")
  
     
   val paresElementoSolventaJoin= inputformAlias.map(x => {
        val spl=x.split(";")     
        (spl(1), spl(0)) 

    });
     

    
    val paresElementoSolventa1= inputformSolventa.map(x => {
        val spl=x.split(";")     
        (spl(2), 1) 

    });
   
    
    
    
    
    
    val nombreFormularioArchivo =paresElementoSolventa1.join(paresElementoSolventaJoin)
    .map((x)=>{
      (x._2._2,x._1)
    }).distinct();
  
//     paresElementoSolventaJoin.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/resumen/cvs/paresElementoSolventaJoin")
//  
//      paresElementoSolventa1.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/resumen/cvs/paresElementoSolventa1")
//  
//     nombreFormularioArchivo.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/resumen/cvs/nombreFormularioArchivo")
//  
    
    
    
    val paresElementoSolventa2= inputformSolventa.map(x => {
        val spl=x.split(";")     
        (spl(2), spl(0)) 

    });
   
   
   val reduce1=paresElementoSolventa1.reduceByKey(
              (x, y) => x + y)
     
     
     val reduce2=paresElementoSolventa1.groupByKey()
               
              
   
    val nombreElementoSolventa= inputformSolventa.map(x => {
     x
    }).distinct();
  
   
   
   ////////////////
   
   
   val inputTipoDato = sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/tipo/formTipo/part-00000");
   val paresTipoDato= inputTipoDato.map(x => {
        val spl=x.split(";")     
        (spl(0), spl(1)) 

    });
   
   val inputCall = sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/dependencia/formAnalisis/part-00000");
   val paresCall= inputCall.map(x => {
        val spl=x.split(";")     
        (spl(0), spl(1)) 

    });
   

   
   val inputEstructura = sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/estructura/formEstructura/part-00000");
   val paresEstructura= inputEstructura.map(x => {
        val spl=x.split(";")     
        (spl(0), spl(1)) 

    });

   

   
   val inputSub = sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/sub/formSub/part-00000");
   val paresSub= inputSub.map(x => {
        val spl=x.split(";")     
        (spl(0), spl(1)) 

    });
   
   val inputFuncion = sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/function/formFunction/part-00000");
   val paresFuncion= inputFuncion.map(x => {
        val spl=x.split(";")     
        (spl(0), spl(1)) 

    });
   
   
   
//         paresTipoDato
//   paresCall
//   paresSub
//   paresFuncion
   
  val csvResumen=nombreFormularioArchivo.collect()/*.filter((p) => {p._1.equals("CJASobranteFaltanteDeCaja.Frm")})*/.map((x)=>{
         val fTipoDato=paresTipoDato.collect().filter( (y)=> {
         //  println("[["+y._1+"]" +"["+x._1+"]");
           y._1.toUpperCase().trim().equals(x._1.toUpperCase().trim())
           
         }).length
         
         
         // println(fTipoDato);
         
         val fCall=paresCall.filter( (y)=> y._1.toUpperCase().trim().equals(x._1.toUpperCase().trim())   ).distinct().collect().length
         val fSub=paresSub.filter( (y)=> y._1.toUpperCase().trim().equals(x._1.toUpperCase().trim())   ).distinct().collect().length
         val fFuncion=paresFuncion.filter( (y)=> y._1.toUpperCase().trim().equals(x._1.toUpperCase().trim())   ).distinct().collect().length
         val fparesEstructura=paresEstructura.filter( (y)=> y._1.toUpperCase().trim().equals(x._1.toUpperCase().trim())   ).distinct().collect().length
         val delta=fTipoDato- fparesEstructura;
         if(delta<0)
         {
            x._1+";"+x._2+";"+ 0 +";"+fparesEstructura+";"+fCall+";"+fSub+";"+fFuncion
         }
         else
         {
             x._1+";"+x._2+";"+ delta +";"+fparesEstructura+";"+fCall+";"+fSub+";"+fFuncion

         }
        
         
   });
  
 val  csv= sc.parallelize(csvResumen)
  
  csv.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/resumen/cvs/resumenForm")
  //CJASobranteFaltanteDeCaja.Frm;CJASobranteFaltanteDeCaja.Frm;-1;9;7;9;9
  
 
  
  val shellFormulario=nombreFormularioArchivo.collect().foldLeft(List[String]())((z,x)=>{
    val ls=List(
    "mkdir -p /home/x4/planilla/"+x._2,
    "cd /home/x4/planilla/"+x._2,
    "echo \"Nombre Archivo: "+x._1+"\" >> "+x._2 +".txt",
    "echo \"Tipo Datos: \" >> "+x._2 +".txt",
   "grep  \""+x._1+"\"  /home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/tipo/formTipo/part-00000 >> "+x._2 +".txt",
    "echo \"Estructuras Internas: \" >> "+x._2 +".txt",
    "grep  \""+x._1+"\"  /home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/estructura/formEstructura/part-00000 >> "+x._2 +".txt",
    "echo \"LLamados Externos: \" >> "+x._2 +".txt",
    "grep  \""+x._1+"\" /home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/dependencia/formAnalisis/part-00000  >>"+x._2 +".txt",
    "echo \"LLamados Interno: \" >> "+x._2 +".txt",
    "echo \"LLamados Funcion: \" >> "+x._2 +".txt",
     "grep \""+x._1+"\" /home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/function/formFunction/part-00000 >> "+x._2 +".txt",
    "echo \"LLamados Sub: \" >> "+x._2 +".txt",
     "grep \""+x._1+"\" /home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/sub/formSub/part-00000 >> "+x._2 +".txt"
    )
    
    ls:::z
    
  });
  
   val  shell= sc.parallelize(shellFormulario)
  
  shell.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/resumen/shell/shellForm")
   
}