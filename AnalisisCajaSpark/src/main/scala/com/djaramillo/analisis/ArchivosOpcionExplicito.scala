package com.djaramillo.analisis


import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import com.djaramillo.rutinas.visual.BusquedaArchivos

object ArchivosOpcionExplicito extends App{
  
    val pathFuentes="/home/x4/Escritorio/pCaja/Fuentes";
  
    val conf = new SparkConf().setMaster("local").setAppName("BuscaFml")
     implicit val sc = new SparkContext(conf)
  
    val inputTiposElementos = sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/elementosVisual/part-00000")
  
    val paresElemento= inputTiposElementos.map(x => {
      val spl=x.split(";")
      val path=spl(3).replace('\\', '/').replace(" ", "\\ ");
     (spl(0), spl(1),spl(2),path) 
    });
    
    
    val paresElementoClases=paresElemento.filter((f)=>{f._1.equals("Class") });
    
    val paresElementoModulos=paresElemento.filter((f)=>{f._1.equals("Module") });
    
    
    val paresElementoDesigners=paresElemento.filter((f)=>{f._1.equals("Designer") });
    
     val paresElementoForms=paresElemento.filter((f)=>{f._1.equals("Form") });
    
    
     

    
    
    
    //print(paresElementoClases.first())
    
    
 
    
   val callClases= paresElementoClases.collect().foldLeft(List[String]())((z,x)=> {
      
      val rpp = BusquedaArchivos.buscarOptionExplicit(pathFuentes+"/"+x._4)//(sc)  ;
      
       List[String](x._3+";"+rpp):::z
   
    })//.filter(  (x) => { x!=null   })
    
    
    val callModulo= paresElementoModulos.collect().foldLeft(List[String]())((z,x)=> {
      
      val rpp = BusquedaArchivos.buscarOptionExplicit(pathFuentes+"/"+x._4)  ;
      
      List[String](x._3+";"+rpp):::z
      
      
    })//.filter(  (x) => { x!=null   })
    
    
    
   val callDesigner= paresElementoDesigners.collect().foldLeft(List[String]())((z,x)=> {
      
      val rpp = BusquedaArchivos.buscarOptionExplicit(pathFuentes+"/"+x._4)  ;

       List[String](x._2+";"+rpp):::z
    
      
    })//.filter(  (x) => { x!=null   })
    
    
    
   val callForm= paresElementoForms.collect().foldLeft(List[String]())((z,x)=> {
      
      val rpp = BusquedaArchivos.buscarOptionExplicit(pathFuentes+"/"+x._4)  ;
     List[String](x._2+";"+rpp):::z
      
    })//.filter(  (x) => { x!=null   })
         
     
     
    val linesForm = sc.parallelize(callForm)
    linesForm.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/opex/formOPEX")
    
    
    val linesDesigner = sc.parallelize(callDesigner)
    linesDesigner.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/opex/designerOPEX")
    
 
    val linesClass = sc.parallelize(callClases)
    linesClass.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/opex/clasesOPEX")
    
    
    val linesModulo = sc.parallelize(callModulo)
    linesModulo.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/opex/moduloOPEX")
    
    
}