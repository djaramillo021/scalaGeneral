package com.djaramillo

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

import java.util.regex._;

object Main extends App {
  
  //val conf = new SparkConf().setMaster("local").setAppName("My App")
//val sc = new SparkContext(conf)
  
  
  
  
  
   val conf = new SparkConf().setMaster("local").setAppName("BuscaFml")
  val sc = new SparkContext(conf)
  // Load our input data.
   
   
  val input = sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/entrada/listaFml.txt")
  
  
  
   val lineasFml =input.collect()
   

   
  val archivosFml = lineasFml.foldLeft(List[ (String,RDD[String]  ) ]() ) (  (z,x)=>{ List( (x,sc.textFile(x)) ) ::: z  } )   //map(x => sc.textFile(x)  )
  

  
 
  val paresFml=archivosFml. map(
      new Function1[(  String, RDD[String]),RDD[(String,String,String,String,String)]]{
        
        def apply(rdd:( String, RDD[String])):RDD[(String,String,String,String,String)]={ 
          
           //println("entro");
          
            val list=rdd._2.map((x) =>{
                if(x.toLowerCase().contains("#define"))
                {
                  //println(x);
                   //String delimiters = "\\s*";
                   val split= x.split("\\s");
                   
                   //split.foreach { p => println(p); }
                    
                    //#define SGT_NOMBRE  ((FLDID32)167789165)  /* number: 17005   type: string */
                   
                    val r:Pattern  = Pattern.compile("\\(\\(FLDID32\\)(\\d*)\\)");
                    
                    val m:Matcher = r.matcher(split(2));
                     m.find( );
                   // println("Found value: " + m.group(1) );
                    //nombreArchivo,nombreFml,codigo1,codigo2,tipo
                    val tp=(rdd._1,split(1),m.group(1),split(5),split(8));
                    //val tp=(lineasFml(rdd._2),split(1),split(2),split(5),split(8));
                    //println(tp);
                    tp;
                    
                }
                else
                {
                  //println("null");
                  null
                   
                }
              
            }
            ); 
            
           // val f=list.filter( (x)=> {  x!=null })
            
            return list.filter( (x)=> {  x!=null })
        }
      }
     );
  
   
   
   
   
   val salida=paresFml.foldLeft(List[RDD[(String,String,String,String,String)]]())( (z,x)=> List(x):::z   )
  val s2=sc.union(salida)
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  val inputProgramaCaja = sc.textFile("/home/x4/libreria/sbt/AnalisisCajaSpark/entrada/archivos.txt.limpio")
  
 val lineasProgramaCaja =inputProgramaCaja.collect()
   

   
  val archivosPrograma = lineasProgramaCaja.foldLeft(List[ (String,RDD[String]  ) ]() ) (  (z,x)=>{ List( (x,sc.textFile(x)) ) ::: z  } )   //map(x => sc.textFile(x)  )
  
  
  
  
  
  
 
  def buscarFmlArchivo(codigo:String):List[String]={
     
 val eLisArc=  archivosPrograma.map ( (x:(String,RDD[String] )) => {
     val rp=x._2.filter(line => line.contains(codigo)).count() 
       if(rp==0)
       {
         null
       }
       else
       {
         x._1
       }
   } )
   
   eLisArc.filter { x => x!=null }
   
     
   };
   
   
   
   
   val salidAnalisis=salida.map((x:RDD[(String,String,String,String,String)])=>{
     
               x.map((y)=>{
                     val (nombreArchivo,nombreFml,codigo1,codigo2,tipo)=y;
                 
                     val matches= buscarFmlArchivo(codigo1).map ( (ls) => {
                       
                          (ls,nombreArchivo,nombreFml,codigo1,tipo)
                     });
                     
                     if(!matches.isEmpty)
                     {
                       matches
                     }
                     else
                     {
                       null
                     }
                 
               })//.filter((f1)=>{f1!=null})
               
             })
   val s3=sc.union(salidAnalisis);
     s3.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/paresAnalisis.txt")         
    //s2.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/paresFull.txt")
   
  
 /* 
  
  
   val paresPrograma=archivosPrograma. map(
      new Function1[(  String, RDD[String]),RDD[(String,String,String,String,String)]]{
        
        def apply(rdd:( String, RDD[String])):RDD[(String,String,String,String,String)]={ 
          
           //println("entro");
          
          
          
          
          
            val list=rdd._2.map((x) =>{
              
                //archivo programa x =linea del programa
              
              
                  
              
                    
              
              
              
              
              
                
                if(x.toLowerCase().contains("#define"))
                {
                  //println(x);
                   //String delimiters = "\\s*";
                   val split= x.split("\\s");
                   
                   //split.foreach { p => println(p); }
                    
                    //#define SGT_NOMBRE  ((FLDID32)167789165)  /* number: 17005   type: string */
                   
                    val r:Pattern  = Pattern.compile("\\(\\(FLDID32\\)(\\d*)\\)");
                    
                    val m:Matcher = r.matcher(split(2));
                     m.find( );
                   // println("Found value: " + m.group(1) );
                    
                    val tp=(rdd._1,split(1),m.group(1),split(5),split(8));
                    //val tp=(lineasFml(rdd._2),split(1),split(2),split(5),split(8));
                    //println(tp);
                    tp;
                    
                }
                else
                {
                  //println("null");
                  null
                   
                }
              
            }
            ); 
            
           // val f=list.filter( (x)=> {  x!=null })
            
            return list.filter( (x)=> {  x!=null })
        }
      }
     );
  
  
  */
  
  
  
  
  
   // s2.saveAsTextFile("/home/x4/libreria/sbt/AnalisisCajaSpark/salida/paresFull.txt")
  
  /*
  
  
  
val pairs = input.map(x => (x.split(" ")(0), x.split(" ")(1)))
//pairs.foreach((x)=>{ print( x)})

//val pairs2=pairs.flatMapValues(x => (x.toInt to 5))


val pairs2=pairs.filter{case (key, value) => value.toInt <= 3}
  // Split it up into words.
  //val words = input.flatMap(line => line.split(" "))
  // Transform into pairs and count.
  //val counts = words.map(word => (word, 1)).reduceByKey{case (x, y) => x + y}
  // Save the word count back out to a text file, causing evaluation.
  //--counts.saveAsTextFile("/home/x4/libreria/sbt/EjemploSpark/salida/salidaSpark.txt")
   pairs2.saveAsTextFile("/home/x4/libreria/sbt/EjemploSpark/salida/salidaSpark3.txt")
   
   */

}