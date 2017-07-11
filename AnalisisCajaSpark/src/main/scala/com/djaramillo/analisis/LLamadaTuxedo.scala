package com.djaramillo.analisis


import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import org.apache.spark.rdd._
import com.djaramillo.rutinas.visual.BusquedaArchivos

object LLamadaTuxedo extends App{
  
   
    val conf = new SparkConf().setMaster("local").setAppName("BuscaFml")
    val sc = new SparkContext(conf)
  
    val inputTiposElementos = sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/entrada/visual/EjecutarServicio.txt")
  
    val paresElemento:RDD[(String,String)]= inputTiposElementos.map(x => {
      BusquedaArchivos.buscarEjecutarServicio_MensajeLinea(x);
      
    });
    
    
    val dataSndRcv = sc.parallelize(List(
          ("Cls/CCaja.cls", "CjaParamInter"), 
          ("Cls/CCaja.cls", "CjaParametros"), 
          ("Cls/CCaja.cls", "CjaRecMedioPago"), 
          ("Cls/CCaja.cls", "CjaRecProductos"), 
          ("Cls/Estacion.cls", "CjaModEstTrabaj"), 
          ("Cls/CTerminal.cls", "CjaSaldosInicio"),
          
           ("Cls/CTransaccion.cls", "Journalizar"), 
            ("Falabella/DEPReinversionDeposito.frm", "SgtRecMoneda")
        ));
    
    
    val dataSendReceiveSinc = sc.parallelize(List(
          ("Cls/cHermesUsuario.cls", "HrmRecUsuarios")
        ));

    
    val lecturaTotal = paresElemento ++ dataSndRcv ++ dataSendReceiveSinc;
    
 // val dd=  paresElemento.groupByKey();
      val dValues=   lecturaTotal.map((x)=>{
          x._2
        }).distinct();
     
     //paresElemento.
     // println("Cantidad servicioTuxedo:"+dValues.count());
     dValues.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/tuxedo/servicios.txt")
    

    
    
}