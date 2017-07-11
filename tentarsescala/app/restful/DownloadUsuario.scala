package restful


import play.api.mvc.{Action, Controller}
import util.google.CloudStorage
import java.io.InputStream
import play.api.libs.iteratee.Enumerator
import play.api.libs.concurrent.Execution.Implicits._
import models.FotoUsuario
import models.Loc
import play.api.libs.json.JsArray
import play.Logger
import play.api.libs.json._
import play.api.mvc.Result
import play.api.mvc.ResponseHeader

//import play.mvc.SimpleResult



object DownloadUsuario extends Controller {
  
  
  
  	
  	
  
	def mostrartFoto(fName: String) = Action { implicit request =>  
		//println(fName);
	  
	  
		val in:Either[Exception,(InputStream,String,Long)] =CloudStorage.downloadUsuario(fName);
		in.fold((ex)=>{  
		    Status(503)(
		        JsObject(
		            Seq("mensaje"->JsString("error cargar archivo"))
		            )
		     )
		}, (ins)=>{
		  val dataContent: Enumerator[Array[Byte]] = Enumerator.fromStream(ins._1)
		  
		   Logger.info("ins._3="+ins._3) 
		  
		  
		 Result(
        header = ResponseHeader(200 ,Map(CONTENT_LENGTH -> ins._3.toString())),
        body = dataContent
      );
		});	
	}
	
	
	
	
	  	

}