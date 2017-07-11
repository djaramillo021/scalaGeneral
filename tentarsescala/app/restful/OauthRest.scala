package restful

import util.UtilAes;
import util.google.CloudStorage
import util.PeticionesRest
import play.api.mvc.{Action, Controller}
import scala.annotation.tailrec
import models.Usuario
import play.Logger
import com.mongodb.MongoException
import com.mongodb.DuplicateKeyException
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import models.Login
import org.bson.types.ObjectId;






object OauthRest extends Controller {
  
     def registroSistemaFacebook() = Action { request =>{ 
        Logger.info("registroSistemaFacebook") 
        request.body.asJson.map { json => {
            
          
              val idFacebook:Option[String] = json.asOpt[String]((JsPath \ "idf").read[String]);  
              //\("idFace").astoString();
              val _email:Option[String]  =  json.asOpt[String]((JsPath \ "em").read[String]);
                //json.\("email").as[String];
              val nombre:Option[String] = json.asOpt[String]((JsPath \ "nb").read[String]);
                //json.\("nombre").as[String];
              val apellido:Option[String] = json.asOpt[String]((JsPath \ "ap").read[String]);
                //json.\("apellido").as[String];
              val _codOauth:Option[String] =json.asOpt[String]((JsPath \ "coo").read[String]);
                //json.\("codOauth").as[String];
              //val codOauthFacebook:Option[String] =json.asOpt[String]((JsPath \ "coo2").read[String]);
                //json.\("codOauth").as[String];
              
               val sexo:Option[String] = json.asOpt[String]((JsPath \ "sex").read[String]);
               val _sexo= sexo  match {
                case Some(s) =>s
                case None => null
              }
                
              
               
               val email  = _email  match {
                   case Some(x) =>{
                    UtilAes.decrypt(x);
                    
                     
                   }
                   case None=>None
                 } 
                    
                    
                 
                 val codOauth  = _codOauth  match {
                   case Some(x) =>{
                      UtilAes.decrypt(x);
                  
                     
                   }
                   case None=>None
                 } 
                   

                    
             if( idFacebook !=None
                  && email!=None
                  && nombre!=None
                  && apellido!=None
                  && codOauth!=None
                 // && codOauthFacebook!=None
                 )
             {
    
                val usu:Usuario= new Usuario(_id=idFacebook.get,sexo=_sexo,email=email.get,nombre=nombre.get,apellido=apellido.get,codOauth=codOauth.get/*,codOauthFacebook=codOauthFacebook.get*/);
    
                Usuario.agregarUsuarioFacebook(usu).fold((ex)=>{

              						       Logger.error("Error en save facebook",ex) 
                                  Status(503)(
                                        JsObject(
                                          Seq("mensaje"->JsString("save facebook"))
                                          )
                                     );
          												
          											
      	        	   					},
      	        	   					(us)=>{
      	        	   					   Ok(       JsObject(
                            		            Seq("mensaje"->JsString("registro ok"))
                            		            ))
      	        	   					  
      	        	   					});
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
      
      
       def loginSistemaCuenta() = Action { request =>{ 
        Logger.info("loginSistemaCuenta") 
        request.body.asJson.map { json => {
    
                    val _email:Option[String]  =  json.asOpt[String]((JsPath \"em").read[String]);
                    val _clave:Option[String] =json.asOpt[String]((JsPath \"ps").read[String]);
        
                 
                 val email  = _email  match {
                   case Some(x) =>{
                    UtilAes.decrypt(x);
                    
                     
                   }
                   case None=>None
                 } 
                    
                    
                 
                 val clave  = _clave  match {
                   case Some(x) =>{
                      UtilAes.decrypt(x);
                      
                     
                   }
                   case None=>None
                 } 
                    
                    
             if(  email!=None
                  && clave!=None
                 )
             {
                val login:Login= new Login(_id=email.get,clave=clave.get);
                Login.verificarLogin(login).fold((ex)=>{
      												//if(!ex._1)
      												//{
      			                       Logger.error("error al consultar usuario",ex._2) 
    												  	   Status(503)(
                      		              JsObject(
                        		              Seq("coad"->JsNumber(1),
                        		                  "msj"->JsString("error al consultar usuario"))
                        		              )
                      		           );
      												//}
      									
      											
            	   					},
            	   					(res:Boolean)=>{
            	   					      
            	   					        if(res)
            	   					        {
            	   					           Ok(  
            	   					             JsObject(
                        		            Seq(
                        		                "vrf"->JsBoolean(true)
                                             ,"coad"->JsString("coad"))
                        		            )); 
            	   					        }
            	   					        else
            	   					        {
            	   					          
            	   					           Ok(  
            	   					             JsObject(
                        		            Seq(
                        		                "vrf"->JsBoolean(false),
                                            
                        		                "msj"->JsString("no existe email y clave"))
                        		            )); 
            	   					        }
  					  
            	   					});
                    
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
      
      
      
       
 def registroSistemaCuentaFoto = Action(parse.multipartFormData) { 

      request =>
        {
    
         
         val nombre = request.body.asFormUrlEncoded.get("nb") match {
                case Some(dtos) =>dtos.head
                case None => null
              }
          
         val apellido = request.body.asFormUrlEncoded.get("ap") match {
                case Some(dtos) =>dtos.head
                case None => null
              }
          
         val sexo = request.body.asFormUrlEncoded.get("sex") match {
                case Some(dtos) =>dtos.head
                case None => null
              }
         
         
         
         val _email = request.body.asFormUrlEncoded.get("em") match {
                case Some(dtos) =>Some(dtos.head)
                case None => None
              }
         val _clave = request.body.asFormUrlEncoded.get("ps") match {
                case Some(dtos) =>Some(dtos.head)
                case None => None
              }
         
       /* val codOauth = request.body.asFormUrlEncoded.get("coo") match {
                case Some(dtos) =>dtos.head
                case None => null
              }
    */
                         
       val email  = _email  match {
         case Some(x) =>{
            val tmp=UtilAes.decrypt(x);
            tmp  match {
              case Some(y) => y 
              case None=>null
            }
           
         }
         case None=>null
       } 
         
       val clave  = _clave  match {
         case Some(x) =>{
            val tmp=UtilAes.decrypt(x);
            tmp  match {
              case Some(y) => y 
              case None=>null
            }
           
         }
         case None=>null
       } 
       
           Logger.debug("antes de foto") 
         val dFoto=request.body.file("fotoPerfil");
          Logger.debug("despues de foto") 
         
             if( nombre!=null 
                 && apellido!=null 
                 && sexo!=null 
                 && clave!=null
                 && email!=null 
                 && dFoto!=None   )
             {
        
                   
                  Logger.error("entro if1") 
        

                   
                    dFoto.map { picture =>
                        val filename = picture.filename 
                        val contentType = picture.contentType    
                        val fileNamePublic=PeticionesRest.codificarNombre(filename.replaceAll("[^0-9a-zA-Z.]+","").toLowerCase());
                     
                       
                        if(fileNamePublic!=null){
                          Logger.trace(fileNamePublic)
                          CloudStorage.uploadUsuario(picture,fileNamePublic);
                            
                           val dUsuario:Usuario= new Usuario(_id=email
                                                             ,nombre=nombre
                                                             ,apellido=apellido
                                                             ,email=email
                                                             //,codOauth=codOauth
                                                             ,clave=clave
                                                             ,sexo=sexo
                                                             ,nombreArchivo= fileNamePublic);
                           
                           
        
                            val resultado: Either[(Boolean,Exception),Usuario]=Usuario.agregarUsuario(dUsuario); 
                            resultado.fold((ex)=>{
                             if(ex._1)
                              {
                                  Logger.info("usuario ya existe(duplicado)") 
                                                        Status(503)(
                                                              JsObject(
                                                                Seq("mensaje"->JsString("error ,ya se registro usuario"))
                                                                )
                                                           );
                              }
                              else
                              {
                                Logger.error("Error en insersion documento",ex) 
                                  Status(503)(
                                                            JsObject(
                                                              Seq("mensaje"->JsString("error al insertar registro usuario"))
                                                              )
                                                         );
                                                    }
                                                },
                                                (us)=>{
                                                   Ok(       JsObject(
                                                              Seq("mensaje"->JsString("registro ok"))
                                                              ))                              
                                                });    
                        }
                        else{
                             Status(503)(
                              JsObject(
                                      Seq("mensaje"->JsString("error guardar archivo "))
                                      )
                                 )
                            
                          }

                  }.getOrElse {
                        Status(503)(
                                JsObject(
                                  Seq("mensaje"->JsString("Expecting Json data"))
                                    )
                               );
                   }
                  
                 
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
      }
}  
         
    
      
      def registroSistemaCuenta() = Action { request =>{ 
        Logger.info("registroSistemaCuenta") 
        request.body.asJson.map { json => {
            
    
                 
            val _email:Option[String]  =  json.asOpt[String]((JsPath \ "em").read[String]);
            val nombre:Option[String] = json.asOpt[String]((JsPath \ "nb").read[String]);
            val apellido:Option[String] = json.asOpt[String]((JsPath \ "ap").read[String]);
              //json.\("apellido").as[String];
            val _clave:Option[String] =json.asOpt[String]((JsPath \ "ps").read[String]);
              //json.\("codOauth").as[String];
            val sex:Option[String] = json.asOpt[String]((JsPath \ "sex").read[String]);                   
            //val codOauth:Option[String] =json.asOpt[String]((JsPath \ "coo").read[String]);
              //  //json.\("codOauth").as[String];    

            val email  = _email  match {
                   case Some(x) =>{
                    UtilAes.decrypt(x);
                    
                     
                   }
                   case None=>None
             } 
            
            val clave  = _clave  match {
                   case Some(x) =>{
                    UtilAes.decrypt(x);
                    
                     
                   }
                   case None=>None
                 } 
            
            
             if(  email!=None
                  && nombre!=None
                  && apellido!=None
                  && sex!=None
                //  && codOauth!=None
                  && clave!=None)
             {
               
                val usu:Usuario= new Usuario(_id=email.get
                        /*,codOauth=codOauth.get*/
                        ,email=email.get,nombre=nombre.get,apellido=apellido.get,clave=clave.get ,sexo=sex.get);
                Usuario.agregarUsuario(usu).fold((ex)=>{
          												if(ex._1)
          												{
          												    Logger.info("usuario ya existe(duplicado)") 
                                			Status(503)(
                            		            JsObject(
                              		            Seq("mensaje"->JsString("error ,ya se registro usuario"))
                              		            )
                            		         );
          												}
          												else
          												{
          												  Logger.error("Error en insersion documento",ex) 
        												  	Status(503)(
                          		            JsObject(
                            		            Seq("mensaje"->JsString("error al insertar registro usuario"))
                            		            )
                          		         );
          												}
      	        	   					},
      	        	   					(us)=>{
      	        	   					   Ok(       JsObject(
                            		            Seq("mensaje"->JsString("registro ok"))
                            		            ))     	        	   					  
      	        	   					});
                   
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