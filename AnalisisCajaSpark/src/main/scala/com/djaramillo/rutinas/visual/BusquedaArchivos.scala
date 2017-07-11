package com.djaramillo.rutinas.visual

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.rdd.RDD

import java.util.regex._;

object BusquedaArchivos {
  
  def busquedaCall(nombreAcrhivo:String)(implicit sc:SparkContext):Array[String]={
    
    
    //    val conf = new SparkConf().setMaster("local").setAppName("BuscaFml")
    // val sc = new SparkContext(conf)
    
   // println(nombreAcrhivo)
       
      val inputArchivo = sc.textFile(nombreAcrhivo)
      
     // inputArchivo.foreach { x => println(x) }
      
      val rpd=inputArchivo.map( (x)=>{
               // println(nombreAcrhivo+": "+x)
                buscarCallLinea(x);
                
        
      }).filter((x)=> x!=null );

      rpd.collect();

  }
  
  
  
  
  
  
  
    def busquedaFunction(nombreAcrhivo:String)(implicit sc:SparkContext):Array[String]={
    
    
    //    val conf = new SparkConf().setMaster("local").setAppName("BuscaFml")
    // val sc = new SparkContext(conf)
    
   // println(nombreAcrhivo)
       
      val inputArchivo = sc.textFile(nombreAcrhivo)
      
     // inputArchivo.foreach { x => println(x) }
      
      val rpd=inputArchivo.map( (x)=>{
               // println(nombreAcrhivo+": "+x)
                buscarFuncionLinea(x);
                
        
      }).filter((x)=> x!=null );

      rpd.collect();

  }
    
    
    
    def busquedaSub(nombreAcrhivo:String)(implicit sc:SparkContext):Array[String]={
    
    
    //    val conf = new SparkConf().setMaster("local").setAppName("BuscaFml")
    // val sc = new SparkContext(conf)
    
   // println(nombreAcrhivo)
       
      val inputArchivo = sc.textFile(nombreAcrhivo)
      
     // inputArchivo.foreach { x => println(x) }
      
      val rpd=inputArchivo.map( (x)=>{
               // println(nombreAcrhivo+": "+x)
                buscarSubLinea(x);
                
        
      }).filter((x)=> x!=null );

      rpd.collect();

  }
  
   def busquedaTipoDatos(nombreAcrhivo:String)(implicit sc:SparkContext):Array[String]={
    
    
    //    val conf = new SparkConf().setMaster("local").setAppName("BuscaFml")
    // val sc = new SparkContext(conf)
    
   // println(nombreAcrhivo)
       
      val inputArchivo = sc.textFile(nombreAcrhivo)
      
     // inputArchivo.foreach { x => println(x) }
      
      val rpd=inputArchivo.map( (x)=>{
               // println(nombreAcrhivo+": "+x)
                buscarTipoDatoLinea(x);
                
        
      }).filter((x)=> x.length>0 );
      

      val sld=rpd.collect().foldLeft(List[String]())( (z,x) =>{
        
        if(x.length>0)
         x:::z 
        else
          z
        
      });

      
      sld.toArray
  }
   
   
    def busquedaEstructuraDatos(nombreAcrhivo:String)(implicit sc:SparkContext):Array[String]={
    
    
    //    val conf = new SparkConf().setMaster("local").setAppName("BuscaFml")
    // val sc = new SparkContext(conf)
    
   // println(nombreAcrhivo)
       
      val inputArchivo = sc.textFile(nombreAcrhivo)
      
     // inputArchivo.foreach { x => println(x) }
      
      val rpd=inputArchivo.map( (x)=>{
               // println(nombreAcrhivo+": "+x)
                buscarEstructuraLinea(x);
                
        
      }).filter((x)=> x.length>0 );
      

      val sld=rpd.collect().foldLeft(List[String]())( (z,x) =>{
        if(x.length>0)
         x:::z 
        else
          z
      });

      
      sld.toArray
  }
   

   
   def esComentario(linea:String):Boolean={
     
      val r:Pattern  = Pattern.compile("^\\s*(['|#])",Pattern.CASE_INSENSITIVE);
      val m:Matcher = r.matcher(linea);
      if(m.find( ))
           return true
      else
        return false

     
   }
  
   
   def buscarEstructuraLinea(linea:String):List[String]={
      val r:Pattern  = Pattern.compile("^\\s*([\\w*|_|\\s*]*){0,1}\\s*Type\\s*([\\w*|.]*\\s([\\w*|.]*){0,1})",Pattern.CASE_INSENSITIVE);
      val m:Matcher = r.matcher(linea);
      
     if(esComentario(linea))
     {
       return List();
     }
      
      
      def rec(ee:List[String]):List[String]={
    
            
        if(m.find( ))
        {
          val t=m.group(2).trim()
          if(t.length()>0)
            rec(ee:::List( t)) 
          else
            rec(ee)
        }
        else
          ee
           
         
      }
      
      
      val salida=rec(List());
      return salida
  }
   
  def buscarTipoDatoLinea(linea:String):List[String]={
      val r:Pattern  = Pattern.compile("\\sAs\\s*([\\w*|.]*\\s([\\w*|.]*){0,1})",Pattern.CASE_INSENSITIVE);
      val m:Matcher = r.matcher(linea);
      
     if(esComentario(linea))
     {
       return List();
     }
      
      
      def rec(ee:List[String]):List[String]={
    
            
   
        if(m.find( ))
        {
          val t=m.group(1).trim()
          if(t.length()>0)
            rec(ee:::List( t) )
          else
            rec(ee)
        }
        else
          ee
           
         
      }
      
      
      val salida=rec(List());

     return salida;
    
  }

  
  def buscarCallLinea(linea:String):String={
     if(esComentario(linea))
     {
       return null;
     }
      
    
     val r:Pattern  = Pattern.compile("Call\\s*([\\w*|.]+)",Pattern.CASE_INSENSITIVE);
      val m:Matcher = r.matcher(linea);
      if(m.find( ))
           m.group(1) 
      else
        null
      
    
  }
  
  
  def buscarFuncionLinea(linea:String):String={
     val r:Pattern  = Pattern.compile("function\\s*([\\w*|_]+)",Pattern.CASE_INSENSITIVE);
   //  r.flags().
     
     if(esComentario(linea))
     {
       return null;
     }
     
      val m:Matcher = r.matcher(linea);
      if(m.find( ))
           m.group(1) 
      else
        null

  }
  
  
    def buscarSubLinea(linea:String):String={
     val r:Pattern  = Pattern.compile("sub\\s*([\\w*|_]+)",Pattern.CASE_INSENSITIVE);
   //  r.flags().
     
     if(esComentario(linea))
     {
       return null;
     }
     
      val m:Matcher = r.matcher(linea);
      
      if(m.find( ))
           m.group(1) 
      else
        null

  }
  
    
   def buscarOptionExplicitLinea(linea:String):Boolean={
       if(esComentario(linea))
       {
         return false;
       }
     
      //Option Explicit
      val r:Pattern  = Pattern.compile("(OPTION\\s*EXPLICIT)",Pattern.CASE_INSENSITIVE);
     
      val m:Matcher = r.matcher(linea);
      m.find()
          
          
       //Pattern.matches("(OPTION\\s*EXPLICIT)", linea.toUpperCase());
  }
  
  
   
     
   def buscarAliasLinea(linea:String):String={
       if(esComentario(linea))
       {
         return null;
       }
     //Attribute VB_Name = "mdiPrincipal"
      //Option Explicit
      val r:Pattern  = Pattern.compile("Attribute\\s*VB_Name\\s*=\\s*\"([\\w*|_|.]+)\"",Pattern.CASE_INSENSITIVE);
     
      val m:Matcher = r.matcher(linea);
      if(m.find( ))
           m.group(1) 
      else
        null
          
       //Pattern.matches("(OPTION\\s*EXPLICIT)", linea.toUpperCase());
  }
  
   
    def buscarBaseDatosLinea(linea:String):Boolean={
      //Option Explicit
      // Pattern.matches(".mdb", linea.toLowerCase());
       
      val r:Pattern  = Pattern.compile("(\\.mdb)",Pattern.CASE_INSENSITIVE);
     
      val m:Matcher = r.matcher(linea);
      m.find()
  }
   
    
    def buscarBaseDatos(nombreAcrhivo:String)(implicit sc:SparkContext):Integer={
    
    
    //    val conf = new SparkConf().setMaster("local").setAppName("BuscaFml")
    // val sc = new SparkContext(conf)
    
   // println(nombreAcrhivo)
       
      val inputArchivo = sc.textFile(nombreAcrhivo)
      
     // inputArchivo.foreach { x => println(x) }
      
      val rpd=inputArchivo.map( (x)=>{
             buscarBaseDatosLinea(x);
      }).filter ( x => x ).collect();
      

     
      rpd.length
  }
  
   def buscarAlias(nombreAcrhivo:String)(implicit sc:SparkContext):String={
    
    
    //    val conf = new SparkConf().setMaster("local").setAppName("BuscaFml")
    // val sc = new SparkContext(conf)
    
   // println(nombreAcrhivo)
       
      val inputArchivo = sc.textFile(nombreAcrhivo)
      
     // inputArchivo.foreach { x => println(x) }
      
      val rpd=inputArchivo.map( (x)=>{
             buscarAliasLinea(x);
      }).filter ( x => x!=null ).collect();
      

     
      if(rpd.length>0)
      {
        if(rpd.length==1)
          return rpd(0)
        else
          return "revisar manual tiene mas de un nombre"
      }
      else
      {
        return "revisar manual no se encontro nombre"
      }
      

  }
  
    
    
  def buscarOptionExplicit(nombreAcrhivo:String)(implicit sc:SparkContext):Boolean={
    
    
    //    val conf = new SparkConf().setMaster("local").setAppName("BuscaFml")
    // val sc = new SparkContext(conf)
    
   // println(nombreAcrhivo)
       
      val inputArchivo = sc.textFile(nombreAcrhivo)
      
     // inputArchivo.foreach { x => println(x) }
      
      val rpd=inputArchivo.map( (x)=>{
             buscarOptionExplicitLinea(x);
      }).filter ( x => x ).collect();
      

     
      if(rpd.length>0)
      {
        true
      }
      else
      {
        false
      }
      
      //sld.toArray
      
      //rpd.collect()
  }
  

  
  ///mensaje EjecutarServicio
    def buscarEjecutarServicio_MensajeLinea(linea:String):(String,String)={
     if(esComentario(linea))
     {
       return null;
     }
           
     val r:Pattern  = Pattern.compile("EjecutarServicio(SinMensaje)?\\s*\\(\\s*\"?([\\w*|_|.]+)\"?",Pattern.CASE_INSENSITIVE);
   //  r.flags().
     
     
      val m:Matcher = r.matcher(linea);
      if(m.find( ))
           (linea.split("\\s")(0).replace(":", ""),m.group(2)) 
      else
        null

  }
  

    
    
    

    
 
  
}
