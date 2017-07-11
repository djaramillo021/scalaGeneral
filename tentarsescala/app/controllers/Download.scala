package controllers

import play.api.mvc.{Action, Controller}
import util.google.CloudStorage
import java.io.InputStream
import play.api.libs.iteratee.Enumerator
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.concurrent.Execution.Implicits._
import play.mvc.SimpleResult
import play.api.mvc.Result
import play.api.mvc.ResponseHeader


object Download extends Controller {
  
	def show(fName: String) = Action { implicit request =>  
		//println(fName);
		val in:Either[Exception,(InputStream,String,Long)] =CloudStorage.download(fName);
		in.fold((ex)=>{  
		    Status(503)(
		        JsObject(
		            Seq("mensaje"->JsString("error cargar archivo"))
		            )
		     )
		}, (ins)=>{
		 // ok(new FileInputStream(ins._1))
		  
		  val dataContent: Enumerator[Array[Byte]] = Enumerator.fromStream(ins._1);
    		  

		  
     Result(
        header = ResponseHeader(200 ,Map(CONTENT_LENGTH -> ins._3.toString())),
        body = dataContent
      )
		   //Ok(ins._1);
		 // Ok.chunked(dataContent).as(ins._2);
		});	
	}

}