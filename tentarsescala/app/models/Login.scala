package models

import com.novus.salat.dao.{SalatDAO, ModelCompanion}
import se.radley.plugin.salat._
import com.mongodb.casbah.commons.MongoDBObject
import play.api.Play.current
import models.mongoContext._
import com.mongodb.MongoException
import scala.Left
import scala.Right
import com.mongodb.casbah.commons.MongoDBList
import play.api.PlayException
import org.bson.types.ObjectId
import java.util.Date
import play.Logger



case class Login(_id: String, clave:String=null, fecha:Date = new Date()) 
{
// 	def this(nombre:String,apellido:String,email:String,codOauth:String)
// 	{
// 	  this(_id=email,nombre=nombre,apellido=apellido,email=email,codOauth=codOauth);
// 	}
    //new ObjectId(email.getBytes());
}

object LoginDAO extends SalatDAO[Login, String]( collection = mongoCollection ("usuarios")
  
)


object Login {


    
    
    def verificarLogin(login:Login):Either[(Boolean,Exception),Boolean]={
        
   
     // Logger.info(login._id+" "+login.clave) 
         val query= MongoDBObject("_id"-> login._id
                                   ,"clave"-> login.clave
                                  );
      
       
          
          try
          { 
            //val resultado:List[FotoUsuario]=FotoUsuarioDAO.find(query).toList
             val resultado:Option[Login]=LoginDAO.findOne(query);
             if(resultado.isEmpty)
             {
                  return Right(false);
             }
             else
             {
                  return Right(true);
             }
         
    	    }
          catch{
        	  case ex: Exception => Left(false,ex);
        	
        	}
    }
    

  
}



