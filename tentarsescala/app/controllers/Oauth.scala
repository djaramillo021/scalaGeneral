package controllers

import play.api.mvc.{Action, Controller}
import scala.annotation.tailrec
import models.Usuario
import play.Logger
import com.mongodb.MongoException
import com.mongodb.DuplicateKeyException
import play.api.libs.json.JsObject
import play.api.libs.json.JsString



object Oauth extends Controller {
  
  
  
  
  def mapeoSimple(query:Map[String, Seq[String]]):Map[String, String ]=
  {
	    @tailrec
	    def mS(qY:Map[String, Seq[String]],lQ:Map[String, String] ):Map[String, String ]=
	    {
	      val kyList:List[String]= qY.keySet.toList;
			kyList match {
			    case (x::y) => {		    				
			    				if(qY.get(x).getOrElse(List()).isEmpty)
			    				{
			    				  return mS(qY-x,lQ);
			    				}
			    				else
			    				{
			    				  val mTep=Map(x-> qY.get(x).get(0));
			    				  return mS(qY-x,lQ ++ mTep);
			    				}
			    					    				
			    			   }
			    case Nil =>  lQ;
			  }
	    }
	    
	    return mS(query,Map());
    
  }
  

  
  def registroSistemaCuenta = Action(parse.multipartFormData) { implicit request =>{

    
    	   val email = request.body.asFormUrlEncoded.get("email") match {
					  case Some(dtos) =>dtos.head
					  case None => ""
					}
    
        val nombre = request.body.asFormUrlEncoded.get("nombre") match {
					  case Some(dtos) =>dtos.head
					  case None => ""
					}
        
            
        val apellido = request.body.asFormUrlEncoded.get("apellido") match {
					  case Some(dtos) =>dtos.head
					  case None => ""
					}

    

         val clave = request.body.asFormUrlEncoded.get("clave") match {
					  case Some(dtos) =>dtos.head
					  case None => ""
					}
    
        val query:Map[String, Seq[String]]=request.queryString;
       
        val querySimple:Map[String,String]=mapeoSimple(query);

        

        
        if(querySimple.get("email")!=None)
        {
           val usu:Usuario= new Usuario(_id=email, email=email,nombre=nombre,apellido=apellido,clave=clave);
           //val respuesta=UsuarioDB.agregarUsuario(usu);
           
          // Either[(Boolean,Exception),Usuario]
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
        else{
          
      			Status(503)(
		            JsObject(
  		            Seq("mensaje"->JsString("error no existe parametros de entrada"))
  		            )
		         );
        }
        
     
        
		   
  	}
  }
  
  
  def registroSistemaFacebook = Action(parse.multipartFormData) { implicit request =>{
    
    
 

    
    	   val idFacebook = request.body.asFormUrlEncoded.get("idFace") match {
					  case Some(dtos) =>dtos.head
					  case None => ""
					}
    	   
    
    	   val email = request.body.asFormUrlEncoded.get("email") match {
					  case Some(dtos) =>dtos.head
					  case None => ""
					}
    
        val nombre = request.body.asFormUrlEncoded.get("nombre") match {
					  case Some(dtos) =>dtos.head
					  case None => ""
					}
        
            
        val apellido = request.body.asFormUrlEncoded.get("apellido") match {
					  case Some(dtos) =>dtos.head
					  case None => ""
					}
        
       val codOauth = request.body.asFormUrlEncoded.get("codOauth") match {
					  case Some(dtos) =>dtos.head
					  case None => ""
					}

        
    
        val query:Map[String, Seq[String]]=request.queryString;
       
        val querySimple:Map[String,String]=mapeoSimple(query);

        

        
        if(querySimple.get("email")!=None)
        {
           val usu:Usuario= new Usuario(_id=idFacebook,email=email,nombre=nombre,apellido=apellido,codOauth=codOauth);
           //val respuesta=UsuarioDB.agregarUsuario(usu);
           
          // Either[(Boolean,Exception),Usuario]
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
        else{
          
      			Status(503)(
		            JsObject(
  		            Seq("mensaje"->JsString("error no existe parametros de entrada"))
  		            )
		         );
        }
        
     
        
		   
  	}
  }

}