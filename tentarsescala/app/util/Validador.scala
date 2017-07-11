package util
import scala.util.matching.Regex

object Validador {
  
  
   def obtenerExtension(file:String):String=
  {
     val pattern = new Regex("\\.([a-zA-Z0-9]+)$","ex");
     
     val extension: String = pattern findFirstIn file match {
	  case Some(pattern(ex)) => ex
	  case None=> null
	}
     return extension;
  }
  
    def verificarEmail(correo:String):Boolean=
  {
     val pattern = new Regex("^([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+.)+([a-zA-Z0-9]{2,4})+$");
     val respuesta:Option[String]= pattern.findFirstIn(correo.trim());
     return respuesta!=None;
  }

}