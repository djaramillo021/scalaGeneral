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
import org.bson.types.ObjectId
import java.util.Date







// [ longitud , latitud ]
//loc : [ -3.7037902 , 40.4167754 ]

case class FotoRestaurantes(_id: ObjectId = new ObjectId ,uploder:String,infoFoto:InfoFoto, nombreArchivo:String,loc:Loc,fecha:Date = new Date() /*DateTime=  DateTime.now*/) 

object FotoRestaurantesDAO extends SalatDAO[FotoRestaurantes, ObjectId]( collection = mongoCollection("fotos")
  

)


object FotoRestaurantes {
  
  
  

//
     
  
  


     def obtenerListaRestaurantePuntuados(loc:Loc): Either[(Boolean,Exception),List[FotoRestaurantes]]={
   
       
   
          val query= MongoDBObject(
              
//              "loc"->
//              
//    						MongoDBObject(
//    						    "$near" ->  MongoDBList(loc.longitude,loc.latitude)
//        						,"$maxDistance" -> 100
//        						,"$minDistance" -> 0
//    						)			
					);
          try
          { 
            val resultado:List[FotoRestaurantes]=FotoRestaurantesDAO.find(query).toList
            return Right(resultado);
    	    }
          catch{
        	  case ex: Exception => Left(false,ex);
        	
        	}
   
    
    }
     
    def obtenerListaRestauranteVotados(loc:Loc): Either[(Boolean,Exception),List[FotoRestaurantes]]={
   
       
   
          val query= MongoDBObject(
              
//              "loc"->
//              
//    						MongoDBObject(
//    						    "$near" ->  MongoDBList(loc.longitude,loc.latitude)
//        						,"$maxDistance" -> 100
//        						,"$minDistance" -> 0
//    						)			
					);
          try
          { 
            val resultado:List[FotoRestaurantes]=FotoRestaurantesDAO.find(query).toList
            return Right(resultado);
    	    }
          catch{
        	  case ex: Exception => Left(false,ex);
        	
        	}
   
    
    }

    
   

}












