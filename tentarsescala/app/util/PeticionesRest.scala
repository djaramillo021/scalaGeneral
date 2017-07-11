package util

import util.crypto.AES
import util.protocol.defaults._
import org.apache.commons.codec.binary.Base64
import java.util.Date
import play.Logger

object PeticionesRest {
  
  def codificarNombre(file:String):String={
     val ext=Validador.obtenerExtension(file);
     def encodeBase64(bytes: Array[Byte]) = Base64.encodeBase64String(bytes)
     def dencodeBase64(bytes: String) = Base64.decodeBase64(bytes)
     val encriptada=AES.encrypt(
         (new Date()).getTime(), "qsGfgyhjiolop4Sv").fold((e)=>{
       Logger.error("Error generar nombre",e); null }
       , (x)=>encodeBase64(x).replaceAll("[^0-9a-zA-Z]+","").toLowerCase())
     
     if(ext==null)
       return encriptada
     else
        return encriptada+"."+ext
  }
  
  def toDouble(dato:String): Option[Double]={
     try {
      Some(dato.toDouble)
    } catch {
      case e:Exception => None
    }
  }
  
  
   def toInt(dato:String): Option[Int]={
     try {
      Some(dato.toInt)
    } catch {
      case e:Exception => None
    }
  }
   
   
  def toLong(dato:String): Option[Long]={
     try {
      Some(dato.toLong)
    } catch {
      case e:Exception => None
    }
  }

}