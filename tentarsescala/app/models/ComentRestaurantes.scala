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
import com.mongodb.casbah.MongoConnection
import com.mongodb.casbah.commons.Imports.DBObject
import org.bson.types.ObjectId
import java.util.Date







// [ longitud , latitud ]
//loc : [ -3.7037902 , 40.4167754 ]

case class ComentRestaurantes(_id: ObjectId = new ObjectId, idRest: ObjectId ,idUser:ObjectId,txtComent:String, fecha:Date = new Date() /*DateTime=  DateTime.now*/) 

object ComentRestaurantesDAO extends SalatDAO[ComentRestaurantes, ObjectId]( collection = mongoCollection("comentarioRest")
  

)


object ComentRestaurantes {
  
  
  
  //def addfCommentRest(idRest:ObjectId): Either[(Boolean,Exception),List[ComentRestaurantes]]={
   def agregarComentario(comentario:ComentRestaurantes): Either[(Boolean,Exception),ComentRestaurantes]={
        
      try
      { 
         ComentRestaurantesDAO.insert(comentario);
         return Right(comentario);
      }
      catch{
        case ex: MongoException.DuplicateKey => Left(true,ex);
        case ex: Exception => Left(false,ex);
      
      }
  }
  
  
 
  
  
    def comentarioListaRestaurante(idRest:String): Either[(Boolean,Exception),List[ComentRestaurantes]]={
   
     
         
   
          def gQuery(_idRest:String):DBObject =
          {
            
                  return MongoDBObject(
                    
                    "idRest"->new ObjectId(_idRest) 
                    
                );
           }
          
          
          
          try
          { 
            val resultado:List[ComentRestaurantes]=ComentRestaurantesDAO.find(gQuery(idRest)).toList
            return Right(resultado);
          }
          catch{
            case ex: Exception => Left(false,ex);
          
          }
   
    
    }
  
  


     /*def comentarioListaRestauranteInferior(idRest:ObjectId,fecha:Long): Either[(Boolean,Exception),List[ComentRestaurantes]]={
   
         val limite=20;
         
   
          def gQuery(_idRest:ObjectId,_fecha:Long):DBObject =
          {
            
                  return MongoDBObject(
                    
                    "idRest"->_idRest	
                    ,"fecha"-> MongoDBObject( "$lt"  ->new Date(_fecha)  )
                    
      					);
           }
          
          
          
          try
          { 
            val resultado:List[ComentRestaurantes]=ComentRestaurantesDAO.find(gQuery(idRest,fecha)).limit(limite).toList
            return Right(resultado);
    	    }
          catch{
        	  case ex: Exception => Left(false,ex);
        	
        	}
   
    
    }
     
     
        def comentarioListaRestauranteSuperior(idRest:ObjectId,fecha:Long): Either[(Boolean,Exception),List[ComentRestaurantes]]={
   
         val limite=20;
         
   
          def gQuery(_idRest:ObjectId,_fecha:Long):DBObject =
          {
            
                  return MongoDBObject(
                    
                    "idRest"->_idRest 
                    ,"fecha"-> MongoDBObject( "$gt"  ->new Date(_fecha)  )
                    
                );
           }
          
          
          
          try
          { 
            val resultado:List[ComentRestaurantes]=ComentRestaurantesDAO.find(gQuery(idRest,fecha)).limit(limite).toList
            return Right(resultado);
          }
          catch{
            case ex: Exception => Left(false,ex);
          
          }
   
   
    
    }
     
   */

}












