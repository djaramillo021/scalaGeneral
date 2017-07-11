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


case class ComentPlatos(_id: ObjectId = new ObjectId, idPlato: ObjectId ,idUser:String,txtComent:String,nArchivoUsu:String=null, fecha:Date = new Date() /*DateTime=  DateTime.now*/) 
{
  def setArchivoUsu( nArchivoUsu:String):ComentPlatos={
    return new ComentPlatos(_id=this._id, idPlato=this.idPlato 
                            ,idUser=this.idUser,txtComent=this.txtComent
                            ,nArchivoUsu=nArchivoUsu
                            , fecha=this.fecha );
  }
}
object ComentPlatosDAO extends SalatDAO[ComentPlatos, ObjectId]( collection = mongoCollection("comentarioPlatos")
  

)


object ComentPlatos {
  
  val usuarioDefault:String="usuarioDefecto.jpeg";
  
  //def addfCommentRest(idRest:ObjectId): Either[(Boolean,Exception),List[ComentRestaurantes]]={
   def agregarComentarioPlatos(comentario:ComentPlatos): Either[(Boolean,Exception),ComentPlatos]={
        
      try
      { 
         ComentPlatosDAO.insert(comentario);
         return Right(comentario);
      }
      catch{
        case ex: MongoException.DuplicateKey => Left(true,ex);
        case ex: Exception => Left(false,ex);
      
      }
  }
  
  

  
  
  
    
    def usuarioFoto( usuario:String):String={
      
     val us:Either[(Exception),Usuario]= Usuario.obtenerDatoUsuarioFoto(usuario);
    
     us  match {
         case Right(x) => x.nombreArchivo
         case Left(x) => usuarioDefault
       }
    }
    
    
    def listaUsuarioComentario(lComentarios:List[ComentPlatos]):Map[String,String]={
      
        def recUsuario(indice:Int,lusu:Map[String,String]):Map[String,String]={
            if(indice<lComentarios.length){
              val dd:ComentPlatos=lComentarios(indice);
              
              if(lusu.isDefinedAt(dd.idUser)) 
              {
                 return recUsuario(indice+1,lusu);
              }
              else
              {
                 val nAr=usuarioFoto(dd.idUser) ;
                 return recUsuario(indice+1,lusu++Map(dd.idUser ->nAr ));
              }
 
            }
            else
            {
              return lusu;
            }
          
        }
        
        return recUsuario(0,Map());
      
    }
    
     def comentarioListaPlatos(idPlato: ObjectId): Either[(Boolean,Exception),List[ComentPlatos]]={
   
     
         
   
          def gQuery(_idPlato: ObjectId):DBObject =
          {
            
                  return MongoDBObject(
                    
                    "idPlato"-> _idPlato
                    
                );
           }
          
          

          
          try
          { 
            //val resultado:List[ComentPlatos]=ComentPlatosDAO.find(gQuery(idPlato)).limit(10).sort(orderBy = MongoDBObject("fecha"-> -1 )).toList
           
            
            
            //return Right(resultado);
            
            val resultado:List[ComentPlatos]=ComentPlatosDAO.find(gQuery(idPlato)).limit(10).sort(orderBy = MongoDBObject("fecha"-> -1 )).toList
            val fotoUsuario:Map[String,String]=listaUsuarioComentario(resultado);
            val rFinal=resultado.map ( (x:ComentPlatos) => {
                                                fotoUsuario.get(x.idUser)  match {
                                                  case Some(xn) =>  x.setArchivoUsu(xn)
                                                  case None => x.setArchivoUsu(usuarioDefault)
                                                }
                                            } );
            return Right(rFinal);
            
          }
          catch{
            case ex: Exception => Left(false,ex);
          
          }
   
    
    }
  
    def comentarioListaPlatos(idPlato:String): Either[(Boolean,Exception),List[ComentPlatos]]={
   
     
         
   
          def gQuery(_idPlato:String):DBObject =
          {
            
                  return MongoDBObject(
                    
                    "idPlato"-> new ObjectId(_idPlato)   
                    
                );
           }
          
          

          
          try
          { 
            
            val resultado:List[ComentPlatos]=ComentPlatosDAO.find(gQuery(idPlato)).limit(10).sort(orderBy = MongoDBObject("fecha"-> -1 )).toList
            val fotoUsuario:Map[String,String]=listaUsuarioComentario(resultado);
            val rFinal=resultado.map ( (x:ComentPlatos) => {
                                                fotoUsuario.get(x.idUser)  match {
                                                  case Some(xn) =>  x.setArchivoUsu(xn)
                                                  case None => x.setArchivoUsu(usuarioDefault)
                                                }
                                            } );
            return Right(rFinal);
          }
          catch{
            case ex: Exception => Left(false,ex);
          
          }
   
    
    }
    


     
   

}












