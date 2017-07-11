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
import scala.util.Random
import org.joda.time.DateTime







// [ longitud , latitud ]
//loc : [ -3.7037902 , 40.4167754 ]

case class FotoUsuario(_id: ObjectId = new ObjectId ,geo:GeoJson ,uploder:String,infoFoto:InfoFoto, nombreArchivo:String,loc:Loc,nArchivoUsu:String=null,fecha:Date = new Date() /*DateTime=new DateTime() Date = new Date() DateTime=  DateTime.now*/){
  
    def setArchivoUsu( nArchivoUsu:String):FotoUsuario={
    return new FotoUsuario(_id=this._id, geo=this.geo 
                            ,uploder=this.uploder,infoFoto=this.infoFoto
                            ,nombreArchivo=this.nombreArchivo
                            ,loc=this.loc
                            ,fecha=this.fecha
                            ,nArchivoUsu=nArchivoUsu
                            );
  }
  
} 

object FotoUsuarioDAO extends SalatDAO[FotoUsuario, ObjectId]( collection = mongoCollection("fotos")
  

)


object FotoUsuario {
  
  val usuarioDefault:String="usuarioDefecto.jpeg";
  val distanceMeters =10000;
  
  //platosPuntuados
//platoVotados
  
  
  

     def obtenerListaPuntosCercano(loc:Loc): Either[(Boolean,Exception),List[FotoUsuario]]={
   
       
   

          
          
          val query= MongoDBObject(
              "geo"-> 
                    MongoDBObject("$near"->
                        MongoDBObject("$geometry"->
                                      MongoDBObject("type" -> "Point",
                                                "coordinates"->MongoDBList(loc.longitude,loc.latitude)
                                      )
                                      ,"$maxDistance" -> distanceMeters
                        )
                    )
              );
          try
          { 
            val resultado:List[FotoUsuario]=FotoUsuarioDAO.find(query).toList
            return Right(resultado);
    	    }
          catch{
        	  case ex: Exception => Left(false,ex);
        	
        	}
   
    
    }
     
     
     
     def obtenerListaFotoMapaPlato(usuario:String,loc:Loc): Either[(Exception),List[FotoUsuario]]={
   
       
   
          val query= MongoDBObject(
               "uploder"->usuario
              
//              "loc"->
//              
//                MongoDBObject(
//                    "$near" ->  MongoDBList(loc.longitude,loc.latitude)
//                    ,"$maxDistance" -> 100
//                    ,"$minDistance" -> 0
//                )     
          );
          try
          { 
            val resultado:List[FotoUsuario]=FotoUsuarioDAO.find(query).toList
            return Right(resultado);
          }
          catch{
            case ex: Exception => Left(ex);
          
          }
   
    
    }
     
    def obtenerFoto(idFoto:String):Either[(Exception),FotoUsuario]={
        
   
          val query= MongoDBObject("_id"-> new ObjectId(idFoto));
          
          try
          { 
            //val resultado:List[FotoUsuario]=FotoUsuarioDAO.find(query).toList
             val resultado:Option[FotoUsuario]=FotoUsuarioDAO.findOne(query);
            return Right(resultado.getOrElse(null));
    	    }
          catch{
        	  case ex: Exception => Left(ex);
        	
        	}
    }
    
    
  def usuarioFoto( usuario:String):String={
      
     val us:Either[(Exception),Usuario]= Usuario.obtenerDatoUsuarioFoto(usuario);
    
     us  match {
         case Right(x) => x.nombreArchivo
         case Left(x) => usuarioDefault
       }
    }
    
    
    
    def obtenerFotoDelDia(locUsu:Loc):Either[(Exception),FotoUsuario]={
        
   
          val query= MongoDBObject(
              "geo"-> 
                    MongoDBObject("$near"->
                        MongoDBObject("$geometry"->
                                      MongoDBObject("type" -> "Point",
                                                "coordinates"->MongoDBList(locUsu.longitude,locUsu.latitude)
                        
                                      )
                        )
                    )
              );
          
          

  
          try
          { 
            
            
            //val resultado:List[FotoUsuario]=FotoUsuarioDAO.find(query).toList
             val resultado:List[FotoUsuario]=FotoUsuarioDAO.find(query).limit(10).sort(orderBy = MongoDBObject("geo"-> -1 ,"fecha"-> -1 )).toList;
           val rnd= Random.nextInt(resultado.length);
           val fotoUsuario:FotoUsuario =resultado(rnd);
           val nusu:String=usuarioFoto(fotoUsuario.uploder);
         
           
           //val usuarioDefault:String="usuarioDefecto.jpeg";
             return Right(fotoUsuario.setArchivoUsu(nusu));
          }
          catch{
            case ex: Exception => Left(ex);
          
          }
    }

    
    def agregarFotoUsuario(fotoUsuario:FotoUsuario): Either[(Boolean,Exception),FotoUsuario]={
    

      
      try
      { 
         FotoUsuarioDAO.insert(fotoUsuario);
         return Right(fotoUsuario);
      }
      catch{
        case ex: MongoException.DuplicateKey => Left(true,ex);
        case ex: Exception => Left(false,ex);
      
      }
  
    }
    
//   def reads(json: JsValue) =  FotoUsuario(
//    (json \ "_id").as[String],
//    (json \ "nombreArchivo").as[String],
//    new Loc((json \ "loc" \ "lat").as[Double],(json  \ "loc" \ "long").as[Double])
//    )
    
//    def writes(fotoUsuario: FotoUsuario) = JsObject(Seq(
//      "_id" -> JsString(fotoUsuario._id),
//      "x" -> JsString(fotoUsuario.nombreArchivo)
//    ))

}












