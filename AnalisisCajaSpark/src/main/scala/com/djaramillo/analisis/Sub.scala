package com.djaramillo.analisis


import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import com.djaramillo.rutinas.visual.BusquedaArchivos

object Sub extends App{
  
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
      
      val rpp = BusquedaArchivos.busquedaSub(pathFuentes+"/"+x._4)//(sc)  ;
      if(rpp!=null)
      {
        val lista =rpp.toList.map { (y) => {
           x._3+";"+y
        }};
        
        lista:::z
        
        //(x._3,rpp)
      }
      else
      {
        null
      }
      
    }).filter(  (x) => { x!=null   })
    
    
    val callModulo= paresElementoModulos.collect().foldLeft(List[String]())((z,x)=> {
      
      val rpp = BusquedaArchivos.busquedaSub(pathFuentes+"/"+x._4)  ;
      if(rpp!=null)
      {
        val lista =rpp.toList.map { (y) => {
           x._3+";"+y
        }};
         lista:::z
      }
      else
      {
        null
      }
      
    }).filter(  (x) => { x!=null   })
    
    
    
   val callDesigner= paresElementoDesigners.collect().foldLeft(List[String]())((z,x)=> {
      
      val rpp = BusquedaArchivos.busquedaSub(pathFuentes+"/"+x._4)  ;
      if(rpp!=null)
      {
        val lista =rpp.toList.map { (y) => {
           x._2+";"+y
        }};
         lista:::z
      }
      else
      {
        null
      }
      
    }).filter(  (x) => { x!=null   })
    
    
    
   val callForm= paresElementoForms.collect().foldLeft(List[String]())((z,x)=> {
      
      val rpp = BusquedaArchivos.busquedaSub(pathFuentes+"/"+x._4)  ;
      if(rpp!=null)
      {
        val lista =rpp.toList.map { (y) => {
           x._2+";"+y
        }};
         lista:::z
      }
      else
      {
        null
      }
      
    }).filter(  (x) => { x!=null   })
         
     
     
    val linesForm = sc.parallelize(callForm)
    linesForm.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/sub/formSub")
    
    
    val linesDesigner = sc.parallelize(callDesigner)
    linesDesigner.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/sub/designerSub")
    
 
    val linesClass = sc.parallelize(callClases)
    linesClass.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/sub/clasesSub")
    
    
    val linesModulo = sc.parallelize(callModulo)
    linesModulo.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/sub/moduloSub")
    
    
}