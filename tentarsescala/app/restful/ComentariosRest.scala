package restful

import play.api._
import play.api.mvc._
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import models.ComentPlatos
import org.bson.types.ObjectId

object ComentariosRest extends Controller  {
  

     def registrarComentarioPlato(idPlato: String) = Action { request =>{ 
        Logger.info("registrarComentarioPlato") 
        request.body.asJson.map { json => {
             Logger.info("registrarComentarioPlato antes") 
          
              val idUser:Option[String] = json.asOpt[String]((JsPath \ "upl").read[String]);  
              //\("idFace").astoString();
            //  val idPlato:Option[String]  =  json.asOpt[String]((JsPath \ "em").read[String]);
                //json.\("email").as[String];
              val txtComent:Option[String] = json.asOpt[String]((JsPath \ "txt").read[String]);
                //json.\("nombre").as[String];

             // val codOauth:Option[String] =json.asOpt[String]((JsPath \ "coo").read[String]);
                //json.\("codOauth").as[String];
                
              


             if( 
                  idPlato!=null
                  && idPlato.length()>0
                  &&idUser !=None
                  && txtComent!=None
                //  && codOauth!=None
                 )
             {
    
               


            	 val cmPlato:ComentPlatos= new ComentPlatos(idPlato=new ObjectId( idPlato),idUser=idUser.get,txtComent=txtComent.get);
    
                //,codOauth=codOauth.get
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