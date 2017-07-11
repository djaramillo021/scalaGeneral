package com.djaramillo.analisis


import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD
import com.djaramillo.rutinas.visual.BusquedaArchivos

object TiposDatos extends App{
  
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
      
      val rpp = BusquedaArchivos.busquedaTipoDatos(pathFuentes+"/"+x._4)//(sc)  ;
      if(rpp!=null)
      {
        val lista =rpp.toList.filter { y => y.length()>0 }.map { (y) => {
           x._3+";"+y
        }};
        
       if(lista.length>0)
           lista:::z
        else  
          z
      }
      else
      {
        z
      }
      
    }).filter(  (x) => { x.length()>0    })
    
    
    val callModulo= paresElementoModulos.collect().foldLeft(List[String]())((z,x)=> {
      
      val rpp = BusquedaArchivos.busquedaTipoDatos(pathFuentes+"/"+x._4)  ;
      if(rpp!=null)
      {
        val lista =rpp.toList.filter { y => y.length()>0 }.map { (y) => {
           x._3+";"+y
        }};
       if(lista.length>0)
           lista:::z
        else  
          z
      }
      else
      {
        z
      }
      
    }).filter(  (x) => { x.length()>0     })
    
    
    
   val callDesigner= paresElementoDesigners.collect().foldLeft(List[String]())((z,x)=> {
      
      val rpp = BusquedaArchivos.busquedaTipoDatos(pathFuentes+"/"+x._4)  ;
      if(rpp!=null)
      {
        val lista =rpp.toList.filter { y => y.length()>0 }.map { (y) => {
           x._2+";"+y
        }};
       if(lista.length>0)
           lista:::z
        else  
          z
      }
      else
      {
        z
      }
      
    }).filter(  (x) => {x.length()>0     })
    
    
    
   val callForm= paresElementoForms.collect().foldLeft(List[String]())((z,x)=> {
      
      val rpp = BusquedaArchivos.busquedaTipoDatos(pathFuentes+"/"+x._4)  ;
      if(rpp!=null)
      {
        val lista =rpp.toList.filter { y => y.length()>0 }.map { (y) => {
           x._2+";"+y
        }};
        if(lista.length>0)
           lista:::z
        else  
          z
      }
      else
      {
        z
      }
      
    }).filter(  (x) => { x.length()>0    })
         
     
     
    val linesForm = sc.parallelize(callForm)
    linesForm.distinct().saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/tipo/formTipo")
    
    
    val linesDesigner = sc.parallelize(callDesigner)
    linesDesigner.distinct().saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/tipo/designerTipo")
    
 
    val linesClass = sc.parallelize(callClases)
    linesClass.distinct().saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/tipo/clasesTipo")
    
    
    val linesModulo = sc.parallelize(callModulo)
    linesModulo.distinct().saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/visual/tipo/moduloTipo")
    
    
}