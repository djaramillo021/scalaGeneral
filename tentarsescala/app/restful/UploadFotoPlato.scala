package restful

import play.api.mvc.{Action, Controller}
import play.Logger
import java.io.File
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsNumber
import java.util.Date
import org.apache.commons.codec.binary.Base64
import util.crypto.AES
import util.protocol.defaults._
import util.Validador.obtenerExtension
import play.api.Play.current
import play.Play
import java.io.InputStream
import util.google.CloudStorage
import models.FotoUsuario
import models.Loc
import play.api.libs.json.JsArray
import models.InfoFoto
import util.google.GoogleMaps
import models.GeoJson
import models.ComentPlatos

object UploadFotoPlato  extends Controller {

  def codificarNombre(file:String):String={
	   val ext=obtenerExtension(file);
     def encodeBase64(bytes: Array[Byte]) = Base64.encodeBase64String(bytes)
     def dencodeBase64(bytes: String) = Base64.decodeBase64(bytes)
     val encriptada=AES.encrypt((new Date()).getTime(), "qsGfgyhjiolop4Sv").fold((e)=>{Logger.error("Error generar nombre",e); null }, (x)=>encodeBase64(x).replaceAll("[^0-9a-zA-Z]+","").toLowerCase())
     
     if(ext==null)
    	 return encriptada
     else
    	  return encriptada+"."+ext
  }
  
  
  
  
  
  

  
  
  
  def upload = Action(parse.multipartFormData) { 
  

    
  request =>
    {
      
      

  
 	   val uploader = request.body.asFormUrlEncoded.get("upl") match {
			  case Some(dtos) =>dtos.head
			  case None => ""
			} 
 	   
 	   val valorEstimado = request.body.asFormUrlEncoded.get("vlEst") match {
					  case Some(dtos) =>dtos.head
					  case None => ""
					}
      
 	   val tipoComida = request.body.asFormUrlEncoded.get("tpCmd") match {
					  case Some(dtos) =>dtos.head
					  case None => ""
					}
      
	   val nombrePlato = request.body.asFormUrlEncoded.get("nPlt") match {
					  case Some(dtos) =>dtos.head
					  case None => ""
					}
	   val nombreRestaurante = request.body.asFormUrlEncoded.get("nRest") match {
					  case Some(dtos) =>dtos.head
					  case None => ""
					}
	   
	   val comentario = request.body.asFormUrlEncoded.get("cmt") match {
					  case Some(dtos) =>dtos.head
					  case None => ""
					}
	   
	   val geolatitud = request.body.asFormUrlEncoded.get("lat") match {
					  case Some(dtos) =>dtos.head
					  case None => "{}"
					}

   	   val geolongitud = request.body.asFormUrlEncoded.get("lon") match {
				  case Some(dtos) =>dtos.head
				  case None => "{}"
				}
   

       Logger.info("geolatitud"+geolatitud);
       Logger.info("geolongitud"+geolongitud);
      
	  request.body.file("foto").map { picture =>
	    val filename = picture.filename 
	    val contentType = picture.contentType    
	    val fileNamePublic=codificarNombre(filename.replaceAll("[^0-9a-zA-Z.]+","").toLowerCase());
	 
	   
	    if(fileNamePublic!=null){
	        Logger.trace(fileNamePublic)
		    CloudStorage.uploadPlatos(picture,fileNamePublic);
	        
          val direccion:String=GoogleMaps.getDireccion(geolongitud.toDouble, geolatitud.toDouble);
	 
          val direccionCorta:String=direccion  match {
            case null=> {  null}
            case xDc =>  {
              val sspl=xDc.split(",")
              if(sspl.length>0)
              {
                sspl(0);
              }
              else
              {
                null
              }
            }
          };

          
          
          
	        val fotoUsuario:FotoUsuario= new FotoUsuario(uploder=uploader
                                    ,  geo=new GeoJson(
                                        coordinates=List(geolongitud.toDouble,geolatitud.toDouble)
                                        )
                                    ,nombreArchivo= fileNamePublic,
	                                                      infoFoto=new InfoFoto(
	                                                           nombreRestaurante=nombreRestaurante,
                                                            nombrePlato=nombrePlato,tipoComida=tipoComida,
                                                            valorEstimado=valorEstimado,
                                                           
                                                            comentario=comentario
	                                                          
	                                                          ),loc=new Loc(longitude= geolongitud.toDouble,latitude=geolatitud.toDouble
                                                                        , direccion=direccion
                                                                        ,direccionCorta=direccionCorta
                                                                          )) ;
	        val resultado:Either[(Boolean,Exception),FotoUsuario]=FotoUsuario.agregarFotoUsuario(fotoUsuario); 
	        
	        resultado.fold((ex)=>{
															if(ex._1)
															{
															    Logger.info("usuario ya existe(duplicado)") 
															}
															else
															{
															  Logger.error("Error en insersion documento",ex) 
															}
															
                						  Status(503)(
                    		        JsObject(
                    		            Seq("mensaje"->JsString("error al ingresar datos"))
                    		            )
                    		         )
	  													
										  },
										  (x:FotoUsuario)=>{
                        
                   
                          val cmPlato:ComentPlatos= new ComentPlatos(idPlato=x._id,idUser=x.uploder,txtComent=comentario);
                      
                                    ComentPlatos.agregarComentarioPlatos(cmPlato).fold((ex)=>{
                  
                              if(ex._1)
                              {
                                  Logger.info("usuario ya existe(duplicado)") 
                              }
                              else
                              {
                                Logger.error("Error en insersion documento",ex._2) 
                              }
                              
                              Status(503)(
                                JsObject(
                                    Seq("mensaje"->JsString("error al ingresar datos"))
                                    )
                                 )
                  


                                
                              },
                              (cmp)=>{
                                         val json=
                          
                                          JsObject(Seq(
                                                  "uploder" -> JsString(x.uploder),
                                                  "nombreArchivo" -> JsString(x.nombreArchivo)
                                                ))
                                          
                
                                        
                                          Ok(json);
                                
                              });
                        
                        

										  }
							);
           
		 

		    
	    }
	    else
	    {
	       Status(503)(
	        JsObject(
	            Seq("mensaje"->JsString("error guardar archivo "))
	            )
	     )
	      
	    }

	  }.getOrElse {
	    
	    
	    Status(503)(
	        JsObject(
	            Seq("mensaje"->JsString("error guardar archivo "))
	            )
	     )

	  }
  
  
  }
}
  

}
