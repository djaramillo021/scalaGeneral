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

case class BusquedaPlato(_id: ObjectId = new ObjectId ,uploder:String,infoFoto:InfoFoto, nombreArchivo:String,loc:Loc,fecha:Date = new Date() /*DateTime=  DateTime.now*/) 

object BusquedaPlatoDAO extends SalatDAO[BusquedaPlato, ObjectId]( collection = mongoCollection("fotos")
  

)


object BusquedaPlato {


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


      def obtenerListaTipoComidaPlato(patron:String): Either[(Boolean,Exception),List[BusquedaPlato]]={
   
   
          val query= MongoDBObject(
              
              "infoFoto.tipoComida"->
              
             MongoDBObject(
                    "$regex"-> ("^"+patron)
                    , "$options"-> "i"     
                    )

                   
          );
   


          try
          { 
            val resultado:List[BusquedaPlato]=BusquedaPlatoDAO.find(query).toList
            return Right(resultado);
          }
          catch{
            case ex: Exception => Left(false,ex);
          
          }
   
    
    }

    

      
      def obtenerListaPrecioPlato(idPrecio:Int,ln:Double,lt:Double): Either[(Exception),List[FotoPlatos]]={
   
        
        
        
        val q=idPrecio  match {
          case 0 => MongoDBObject(
                    "$gt"-> 0 
                    )
          case _ =>   MongoDBObject(
                    "$gt"-> 0 
                    )       
        }
   
          val query= MongoDBObject(
              "infoFoto.valorEstimado"->q
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
    
      
      
      
        def obtenerListaNombrePlato(patron:String): Either[(Boolean,Exception),List[BusquedaPlato]]={
   
   
          val query= MongoDBObject(
              
              "infoFoto.nombrePlato"->
              
             MongoDBObject(
                    "$regex"-> ("^"+patron)
                    , "$options"-> "i"     
                    )

                   
          );
   


          try
          { 
            val resultado:List[BusquedaPlato]=BusquedaPlatoDAO.find(query).toList
            return Right(resultado);
          }
          catch{
            case ex: Exception => Left(false,ex);
          
          }
   
    
    }
    
   

}






