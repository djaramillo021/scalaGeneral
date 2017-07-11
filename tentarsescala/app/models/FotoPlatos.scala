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

case class FotoPlatos(_id: ObjectId = new ObjectId ,uploder:String,infoFoto:InfoFoto, nombreArchivo:String,loc:Loc,fecha:Date = new Date() /*DateTime=  DateTime.now*/) 

object FotoPlatosDAO extends SalatDAO[FotoPlatos, ObjectId]( collection = mongoCollection("fotos")
  

)


object FotoPlatos {


     def obtenerListaPlatosPuntuados(loc:Loc): Either[(Boolean,Exception),List[FotoPlatos]]={
   
       
   
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
            val resultado:List[FotoPlatos]=FotoPlatosDAO.find(query).toList
            return Right(resultado);
    	    }
          catch{
        	  case ex: Exception => Left(false,ex);
        	
        	}
   
    
    }
     
    def obtenerListaPlatosVotados(loc:Loc): Either[(Boolean,Exception),List[FotoPlatos]]={
   
       
   
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
            val resultado:List[FotoPlatos]=FotoPlatosDAO.find(query).toList
            return Right(resultado);
    	    }
          catch{
        	  case ex: Exception => Left(false,ex);
        	
        	}
   
    
    }
    
    
    def obtenerListaPlatosUsuario(usuario:String): Either[(Exception),List[FotoPlatos]]={
   
       
   
          val query= MongoDBObject(
             "uploder"->usuario   
          );
          try
          { 
            val resultado:List[FotoPlatos]=FotoPlatosDAO.find(query).toList
            return Right(resultado);
          }
          catch{
            case ex: Exception => Left(ex);
          
          }
   
    
    }

    
   

}






