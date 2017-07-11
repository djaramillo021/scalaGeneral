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
import com.mongodb.casbah.WriteConcern
import com.mongodb.WriteResult
import com.mongodb.casbah.query.Imports._ 
//import com.mongodb.{ WriteConcern, DBObject } 



case class Usuario(_id:String, nombre:String,apellido:String,email:String=null,codOauth:String=null, clave:String=null, fecha:Date = new Date(),sexo:String=null,nombreArchivo:String="usuarioDefecto.jpeg"/*,codOauthFacebook:String=null*/) 
{
// 	def this(nombre:String,apellido:String,email:String,codOauth:String)
// 	{
// 	  this(_id=email,nombre=nombre,apellido=apellido,email=email,codOauth=codOauth);
// 	}
    //new ObjectId(email.getBytes());
}

object UsuarioDAO extends SalatDAO[Usuario, String]( collection = mongoCollection ("usuarios") )


object Usuario {
    def agregarUsuario(usuario:Usuario): Either[(Boolean,Exception),Usuario]={
          try
          {
              val rt:Option[String]=UsuarioDAO.insert(usuario,WriteConcern.Safe);//save(usuario);
              if(rt!=None)
              {
                Logger.info("agregarUsuario"+rt.get); 
                return Right(usuario);
              }
              else
              {
                return Left(false,null);
              }
      
          }
          catch{
            case ex: MongoException.DuplicateKey => Left(true,ex);
            case ex: Exception => Left(false,ex);
          
          }
    
      }
    
    
    
    
        def agregarUsuarioFacebook(usuario:Usuario): Either[(Exception),Usuario]={
          try
          {
            val cr:WriteResult =UsuarioDAO.save(usuario,WriteConcern.Safe);//save(usuario);
          
            if(cr.isUpdateOfExisting())
            {
              Logger.info("Se actualizo usuario facebook"); 
              
            }
            else
            {
               Logger.info("Se ingreso usuario facebook");
             
            }
            return Right(usuario);
      
          }
          catch{
            //case ex: MongoException.DuplicateKey => Left(true,ex);
            case ex: Exception => Left(ex);
          
          }
    
      }
        
        def actualizarDatos(usuario:Usuario): Either[(Exception),Usuario]={
          try
          {
           

             Logger.debug("usuario.nombre:"+usuario.nombre) 
             Logger.debug("usuario.apellido:"+usuario.apellido) 
             //Logger.debug("usuario.email:"+usuario.email) 
             Logger.debug("usuario.sexo:"+usuario.sexo) 
             Logger.debug("usuario._id:"+usuario._id) 
   
             
             
            
            
            
              
            val datos=MongoDBObject(
                          "nombre"-> usuario.nombre
                         ,"apellido"->usuario.apellido
                         //,"email"-> usuario.email
                         ,"sexo"-> usuario.sexo
            )

          if(usuario.nombreArchivo!=null)
          {
             datos.put("nombreArchivo", usuario.nombreArchivo);   
             Logger.debug("usuario.nombreArchivo:"+usuario.nombreArchivo) 
          }
                       
        
            
            val cr = UsuarioDAO.update(q = MongoDBObject("_id" ->  usuario._id), o = MongoDBObject("$set" -> datos), upsert = false, multi = false)
             
            
            //val cr = UsuarioDAO.update(MongoDBObject("_id" ->  usuario._id),oSet)
            
            
          //  val cr = UsuarioDAO.update(MongoDBObject("_id" -> usuario._id), dUsuario, false, false)

    
            
            
            
            
            
         //   val cr:WriteResult =UsuarioDAO.save(usuario,WriteConcern.Safe);//save(usuario);
          

            return Right(usuario);
      
          }
          catch{
            //case ex: MongoException.DuplicateKey => Left(true,ex);
            case ex: Exception => Left(ex);
          
          }
    
      }  
        
        
        
       def obtenerDatoUsuario(idUsuario:String):Either[(Exception),Usuario]={
        
   
          val query= MongoDBObject("_id"-> idUsuario );
          
          try
          { 
            //val resultado:List[FotoUsuario]=FotoUsuarioDAO.find(query).toList
             val resultado:Option[Usuario]=UsuarioDAO.findOne(query);
     
            return Right(resultado.getOrElse(null));
          }
          catch{
            case ex: Exception => Left(ex);
          
          }
    }
       
       
       
     def obtenerDatoUsuarioFoto(idUsuario:String):Either[(Exception),Usuario]={
        
   
          val query= MongoDBObject("_id"-> idUsuario );
          
          try
          { 
            //val resultado:List[FotoUsuario]=FotoUsuarioDAO.find(query).toList
             val resultado:Option[Usuario]=UsuarioDAO.findOne(query);
            return Right(resultado.getOrElse(null));
          }
          catch{
            case ex: Exception => Left(ex);
          
          }
    }
        

}



