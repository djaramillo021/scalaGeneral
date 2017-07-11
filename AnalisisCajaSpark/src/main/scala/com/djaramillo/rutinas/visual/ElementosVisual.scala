package com.djaramillo.rutinas.visual
import java.util.regex._;

object ElementosVisual {
  
  def  obtenerElemento(linea:String):(String,String,String,String)={
    val rp=obtenerReporte(linea);
    if(  rp!=null)
    {
      return rp
    }
    
    val frm=obtenerFormulario(linea);
    if(  frm!=null)
    {
      return frm
    }
    
    val cls=obtenerClase(linea);
    if(  cls!=null)
    {
      return cls
    }
    
    val mdl=obtenerModulo(linea);
    if(  mdl!=null)
    {
      return mdl
    }
    
      return null
  }

  
  
 private def obtenerReporte(linea:String):(String,String,String,String)={
    
      //Designer=Reportes\repPlanillaTransaccionesTarjetaCMR.Dsr
      val r:Pattern  = Pattern.compile("^Designer=([\\w*|\\s|\\\\|.]*)");
      val m:Matcher = r.matcher(linea);
      if(m.find( ))
      {

               val nombreArchivo=m.group(1).split("\\\\")
              ("Designer",nombreArchivo(nombreArchivo.length-1),"",m.group(1)) 

      }
      else
      {
        null
      }
  }
  
 private def obtenerModulo(linea:String):(String,String,String,String)={
    
      //Module=mFLDdocValoradoCMRPeruFML; Bas\mFLDdocValoCMRPeru.bas
      val r:Pattern  = Pattern.compile("^Module=(\\w*);\\s([\\w*|\\s|\\\\|.]*)");
      val m:Matcher = r.matcher(linea);
      if(m.find( ))
      {

              val nombreArchivo=m.group(2).split("\\\\")
              ("Module",nombreArchivo(nombreArchivo.length-1),m.group(1),m.group(2)) 
      }
      else
      {
        null
      }
  }
  

private def obtenerFormulario(linea:String):(String,String,String,String)={
    
      //Form=Frm\CJAIntercambioDivisa.frm
      val r:Pattern  = Pattern.compile("^Form=([\\w*|\\s|\\\\|.]*)");
      val m:Matcher = r.matcher(linea);
      if(m.find( ))
      {

              val nombreArchivo=m.group(1).split("\\\\")
              ("Form",nombreArchivo(nombreArchivo.length-1),"",m.group(1)) 
              
      }
      else
      {
        null
      }
  }
  
 private  def obtenerClase(linea:String):(String,String,String,String)={
    
    
    //Class=CCaja; Cls\CCaja.cls
   
    val r:Pattern  = Pattern.compile("^Class=(\\w*); ([\\w*|\\s|\\\\|.]*)");
    val m:Matcher = r.matcher(linea);
    if(m.find( ))
    {

            val nombreArchivo=m.group(2).split("\\\\")
            ("Class",nombreArchivo(nombreArchivo.length-1),m.group(1),m.group(2)) 
    }
    else
    {
      null
    }
    
  }
  
  
}