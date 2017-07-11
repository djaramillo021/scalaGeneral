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


object DownloadPlato extends Controller {
  
  
  	def mostrarListaFotos() = Action { implicit request =>  
		//println(fName);
  	  
      val lon: Option[String] = request.getQueryString("lon")
      val lat: Option[String] = request.getQueryString("lat")  
  	  
      
      
      if(!lon.isEmpty && !lat.isEmpty)
      { 
        
             
	
        val localizacion:Loc= new Loc(longitude=lon.get.toDouble,latitude=lat.get.toDouble );
        
       val resultado:Either[(Boolean,Exception),List[FotoUsuario]]= FotoUsuario.obtenerListaPuntosCercano(localizacion);
       
       resultado.fold((ex)=>{
															if(ex._1)
															{
															    Logger.info("usuario ya existe(duplicado)") 
															}
															else
															{
															  Logger.error("Error en obtener documento",ex) 
															}
															
                						  Status(503)(
                    		        JsObject(
                    		            Seq("mensaje"->JsString("error obtener datos"))
                    		            )
                    		         )
	  													
										  },
										  (listaFotos)=>{
										    val listaJson=listaFotos.map((x)=>{
										      
										         
										      JsObject(Seq(
										              "id" -> JsString(x._id.toString()),
                                  "upl" -> JsString(x.uploder),
                                  "nArc" -> JsString(x.nombreArchivo),
                                  "info" ->   JsObject(Seq(
//                                         "v1" ->   JsString(x.infoFoto.valor1),
//                                         "v2" ->  JsString(x.infoFoto.valor2)
                                        )),
                                   "loc" ->   JsObject(Seq(
                                         "lon" ->   JsNumber(x.loc.longitude),
                                         "lat" ->  JsNumber(x.loc.latitude)
                                        )),
                                   "fecha" ->JsNumber(  x.fecha.getTime())
                                ))
										    })    
										    //val json = Json.toJson(listaFotos)
										    
										      Ok( 
										          JsArray(listaJson)
										      );
										  }
							);
       

    
        
      }
      else{
         Status(503)(
		        JsObject(
		            Seq("mensaje"->JsString("error no se ingreso parametros de entrada"))
		            )
		       )
        
      }
	}
  	
  	
  	
  	
  
	def mostrartFoto(fName: String) = Action { implicit request =>  
		//println(fName);
	  
	  
		val in:Either[Exception,(InputStream,String,Long)] =CloudStorage.download(fName);
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
	
	
	
	
	  	
  		


  
	def mostrartFotoPlato(fName: String) = Action { implicit request =>  
     Logger.info("fName:"+fName) 
		//println(fName);
		val in:Either[Exception,(InputStream,String,Long)] =CloudStorage.downloadPlato(fName);
		in.fold((ex)=>{  
		    Status(503)(
		        JsObject(
		            Seq("mensaje"->JsString("error cargar archivo"))
		            )
		     )
		}, (ins)=>{
		  val dataContent: Enumerator[Array[Byte]] = Enumerator.fromStream(ins._1)
		 Result(
        header = ResponseHeader(200 ,Map(CONTENT_LENGTH -> ins._3.toString(),CONTENT_TYPE -> ins._2  )),
        body = dataContent
      );
		});	
	}
  		
  def mostrartFotoPlatoThumbnail(fName: String) = Action { implicit request =>  
		//println(fName);
		val in:Either[Exception,(InputStream,String,Long)] =CloudStorage.downloadPlatoThumbnail(fName);
		in.fold((ex)=>{  
		    Status(503)(
		        JsObject(
		            Seq("mensaje"->JsString("error cargar archivo"))
		            )
		     )
		}, (ins)=>{
		  val dataContent: Enumerator[Array[Byte]] = Enumerator.fromStream(ins._1)
		  Result(
        header = ResponseHeader(200 ,Map(CONTENT_LENGTH -> ins._3.toString(),CONTENT_TYPE -> ins._2 )),
        body = dataContent
      );
		});	
	}

}