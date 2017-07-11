package restful

import play.api.mvc.{Action, Controller}
import play.Logger
import java.io.File
import play.api.libs.json.JsObject
import play.api.libs.json.JsString
import play.api.libs.json.JsNumber
import java.util.Date
import play.api.Play.current
import play.Play
import java.io.InputStream
import util.google.CloudStorage
import models.FotoUsuario
import models.Loc
import play.api.libs.json.JsArray
import models.InfoFoto
import models.Usuario
import play.api.libs.json.JsPath
import util.PeticionesRest

object UsuarioRest  extends Controller {


  
  


  
  

  

  
  def actualizarDatosUsuarioConFoto = Action(parse.multipartFormData) { 
  

    
  request =>
    {
      
      

  
 	   val uploader = request.body.asFormUrlEncoded.get("upl") match {
			  case Some(dtos) =>dtos.head
			  case None => ""
			} 
 	   
 	   val nombre = request.body.asFormUrlEncoded.get("nmb") match {
					  case Some(dtos) =>dtos.head
					  case None => null
					}
      
 	   val apellido = request.body.asFormUrlEncoded.get("ape") match {
					  case Some(dtos) =>dtos.head
					  case None => null
					}
      
	   val sexo = request.body.asFormUrlEncoded.get("sex") match {
					  case Some(dtos) =>dtos.head
					  case None => null
					}

     
       Logger.debug("antes de foto") 
     val dFoto=request.body.file("fotoPerfil");
      Logger.debug("despues de foto") 
     
     if(uploader!=null &&  nombre!=null && apellido!=null && sexo!=null /*&& email!=null*/ && dFoto!=None   )
     {

           
          Logger.error("entro if1") 

             Logger.error("entro if2") 
           
            dFoto.map { picture =>
                val filename = picture.filename 
                val contentType = picture.contentType    
                val fileNamePublic=PeticionesRest.codificarNombre(filename.replaceAll("[^0-9a-zA-Z.]+","").toLowerCase());
             
               
                if(fileNamePublic!=null){
                    Logger.trace(fileNamePublic)
                  CloudStorage.uploadUsuario(picture,fileNamePublic);
                    
                   val dUsuario= new Usuario(_id=uploader,nombre=nombre,apellido=apellido,sexo=sexo,nombreArchivo= fileNamePublic);
                   
                   

                    val resultado:Either[(Exception),Usuario]=Usuario.actualizarDatos(dUsuario); 
                    
                    resultado.fold((ex)=>{

                                        Logger.error("Error al actualizar",ex) 
                                        
                                        
                                        Status(503)(
                                          JsObject(
                                              Seq("mensaje"->JsString("error al ingresar datos"))
                                              )
                                           )
                                        
                                },
                                (x:Usuario)=>{
                                  val json=
                                    
                                    JsObject(Seq(
                                            "mensaje" -> JsString("datos actualizado")
                                          ))                        
                                    Ok(json);
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
      
      
                  Logger.error("entro getOrElse") 
                Status(503)(
                    JsObject(
                        Seq("mensaje"->JsString("error guardar archivo "))
                        )
                 )
          
              }
            
        
     }
     else
     {
       Status(503)(
          JsObject(
              Seq("mensaje"->JsString("error no hay  datos usuario "))
              )
       )
       
     }
	   

      
  
  
  }
}
  
  
  def obtenerDatosUsuario() = Action { implicit request =>{ 
        Logger.info("registrarComentarioPlato") 
//        request.body.asJson.map { json => {
             Logger.info("registrarComentarioPlato antes") 
             

          
              val uploader:Option[String] = request.getQueryString("upl") //json.asOpt[String]((JsPath \ "upl").read[String]);  
          


             if( uploader !=None )
             {
               
               Logger.info("uploader:"+uploader.get) 
        
                   val resultado:Either[(Exception),Usuario]=Usuario.obtenerDatoUsuario(uploader.get); 
                    
                   resultado.fold((ex)=>{
        
                                        Logger.error("Error al actualizar",ex) 
                                        
                                        
                                        Status(503)(
                                          JsObject(

                                              Seq("mensaje"->JsString("error al ingresar datos"))
                                              )
                                           )
                                                
                                        },
                                        (x:Usuario)=>{
                                          
                                          if(x==null)
                                          {
                                            Status(503)(
                                                  JsObject(
        
                                                      Seq("mensaje"->JsString("error al ingresar datos"))
                                                      )
                                                   );
                                         
                                          }
                                          else
                                          {
                                          
                                          val json=
                                            
                                              JsObject(Seq(
  
                                                   "upl" -> JsString(x._id)
                                                   ,"nmb" -> JsString(x.nombre)
                                                   ,"ape" -> JsString(x.apellido)
                                                   ,"sex" -> JsString(x.sexo)
                                                   ,"eml" -> JsString(x.email)
                                                   ,"nA" -> JsString(x.nombreArchivo)
                                                
                                                    ))                        
                                              Ok(json);
                                          
                                          }
              
                                        }
                                );
                   
        
                         }
                          else
                          {
                                       Logger.error("no se envio todos los parametros") 
                                       Status(503)(
                                                    JsObject(
                                                      Seq("mensaje"->JsString("Expecting Json data"))
                                                      )
                                                 );
                          }
//                    } }.getOrElse {
//                        Status(503)(
//                                                JsObject(
//                                                  Seq("mensaje"->JsString("Expecting Json data"))
//                                                    )
//                                               );
//                      }  
              }
    }
  
  
  
   def actualizarDatosUsuario() = Action { request =>{ 
        Logger.info("registrarComentarioPlato") 
        request.body.asJson.map { json => {
             Logger.info("registrarComentarioPlato antes") 
             

          
              val uploader:Option[String] = json.asOpt[String]((JsPath \ "upl").read[String]);  
       
              val nombre:Option[String] = json.asOpt[String]((JsPath \ "nmb").read[String]);
         

              val apellido:Option[String] =json.asOpt[String]((JsPath \ "ape").read[String]);
        
                
              val sexo:Option[String] =json.asOpt[String]((JsPath \ "sex").read[String]);
              


             if( 
                  uploader !=None
                  && nombre!=None
                  && apellido!=None
                  && sexo!=None
                 // && email!=None
                 )
             {
    
               
               
          val dUsuario= new Usuario(_id=uploader.get,nombre=nombre.get,apellido=apellido.get,/*email=email.get,*/sexo=sexo.get,nombreArchivo= null);
                   
                   

           val resultado:Either[(Exception),Usuario]=Usuario.actualizarDatos(dUsuario); 
            
           resultado.fold((ex)=>{

                                Logger.error("Error al actualizar",ex) 
                                
                                
                                Status(503)(
                                  JsObject(
                                      Seq("mensaje"->JsString("error al ingresar datos"))
                                      )
                                   )
                                        
                                },
                                (x:Usuario)=>{
                                  val json=
                                    
                                    JsObject(Seq(
                                            "mensaje" -> JsString("datos actualizado")
                                          ))                        
                                    Ok(json);
                                }
                        );
           

                 }
                  else
                  {
                               Logger.error("no se envio todos los parametros") 
                               Status(503)(
                                            JsObject(
                                              Seq("mensaje"->JsString("Expecting Json data"))
                                              )
                                         );
                  }
            } }.getOrElse {
                Status(503)(
                                        JsObject(
                                          Seq("mensaje"->JsString("Expecting Json data"))
                                            )
                                       );
              }  
      }
    }
  

}
