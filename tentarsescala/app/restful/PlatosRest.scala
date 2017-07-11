package restful


import util.PeticionesRest
import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.collection.immutable.Seq
import models.FotoUsuario
import models.FotoUsuarioDAO
import models.Loc
import models.Loc
import models.FotoPlatos
import org.bson.types.ObjectId
import models.ComentPlatos
import models.BusquedaPlato
import org.joda.time.DateTime
import org.joda.time.Period
import java.util.Date

object PlatosRest extends Controller  {
  

 
  def calcularTiempo(_fecha:Date):String=
  {
    //todo
    val fechaActual:DateTime=  DateTime.now()
    val fecha:DateTime=  new DateTime(_fecha.getTime());
    val  diff:Period = new Period(fecha, fechaActual);
    //val delta:DateTime= fechaActual.minus(fecha);

    if(diff.getYears()>0) 
    {
         return "Hace "+diff.getYears()+" años"
    }
    else if(diff.getMonths()>0 && diff.getMonths()<=12)
    {
      return "Hace "+diff.getMonths()+" meses"
    }
    else if(diff.getDays()>0 &&  diff.getDays()<=30)
    {
       return "Hace "+diff.getDays()+" dias"
    }
    else
    {
       return "Hace "+diff.getHours()+" horas"
    }
    
  }
  
    def obtenerDireccion(loc:String):String=
  {
    //todo
      if(loc!=null)
        return loc;
      else
        return "S/N"
  }



    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    def  deg2rad( deg:Double):Double= {
      return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    def  rad2deg( rad:Double):Double= {
      return (rad * 180.0 / Math.PI);
    }
    
  
  def calcularDistancia(locRest:Loc,locUsu:Loc):String=
  {
      val theta:Double = locRest.longitude - locUsu.longitude// lon1 - lon2;
      val dist:Double = Math.sin(deg2rad(locRest.latitude /*lat1*/)) * Math.sin(deg2rad(locUsu.latitude/* lat2*/)) + Math.cos(deg2rad(locRest.latitude /*lat1*/)) * Math.cos(deg2rad(locUsu.latitude /*lat2*/)) * Math.cos(deg2rad(theta));
      val _dist:Double = rad2deg(Math.acos(dist));
     

      
      val distTotal = _dist * 60 * 1.1515;
       if(distTotal<1)
       {
         val distTotalMetro= (distTotal/1000);
         if(distTotalMetro<10)
         {
           return "Muy cercano";
         }
         else if(distTotalMetro<100)
         {
             return "A menos de 100 metros";
         }
         else
         {
             return Math.round(distTotalMetro)+ "m";
         }
       }
      return  Math.round(distTotal * 1.609344)+ "km";


  }



  
  

  
  
    def platoMasCercanos() =  Action {request =>{ 
           Logger.info("platoMasCercanos") 
//      request.body.asJson.map { json => {

            val user: Option[String] = request.getQueryString("us")  //json.asOpt[String]((JsPath \ "lt").read[String]); 
            val _lat: Option[String] = request.getQueryString("lt")  //json.asOpt[String]((JsPath \ "lt").read[String]); 
            val _lon: Option[String] = request.getQueryString("ln") //json.asOpt[String]((JsPath \ "ln").read[String]);
            
            val _latDelta: Option[String] = request.getQueryString("ltd") //json.asOpt[String]((JsPath \ "ltd").read[String]);
            val _lonDelta: Option[String] = request.getQueryString("lnd") // json.asOpt[String]((JsPath \ "lnd").read[String]);
            
            
            if(
               user!=None
                && _lat!=None
                && _latDelta!=None
                && _lon!=None
                && _lonDelta!=None
                && PeticionesRest.toDouble (_lat.get)!=None
                && PeticionesRest.toDouble (_latDelta.get)!=None
                && PeticionesRest.toDouble (_lon.get)!=None
                && PeticionesRest.toDouble (_lonDelta.get)!=None
                
                )
            {
              
               
                    val lat:Double= PeticionesRest.toDouble (_lat.get).get; 
                    val lon:Double= PeticionesRest.toDouble (_lon.get).get; 
                    
                    val latDelta:Double= PeticionesRest.toDouble (_latDelta.get).get; 
                    val lonDelta:Double= PeticionesRest.toDouble (_lonDelta.get).get;
                    
                   
                    val locRest= new Loc(longitude=lon, latitude=lat,longitudeDelta=lonDelta,latitudeDelta=latDelta)
                    FotoUsuario.obtenerListaPuntosCercano(locRest).fold((ex)=>{
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
                              Logger.error("Error en insersion documento",ex._2) 
                              Status(503)(
                                    JsObject(
                                      Seq("mensaje"->JsString("error al insertar registro usuario"))
                                      )
                                 );
                            }
                          
                        },
                        (listaFoto:List[FotoUsuario])=>{
                           
                          
                          val lsFotos:JsArray=listaFoto.map(new Function1[FotoUsuario,JsObject]{
                            def apply(ft:FotoUsuario): JsObject = {
                               
                              return new  JsObject(
                                          "i"->JsString(ft._id.toString())::

                                          "nA"->JsString(ft.nombreArchivo)::
                                                "g"->JsObject(
                                                          "ln"->JsNumber(ft.loc.longitude) ::
                                                          "lt"->JsNumber(ft.loc.latitude) ::
                                                          Nil
                                                 )::
                                                 Nil
                                  );
                            }
                            
                          }).foldLeft(JsArray ())((z,x) =>   z ++ Json.arr(x) ) ;
                          
                          Ok(
                            JsObject(
                              "r"->JsNumber(20)::  
                               "g"->JsObject(
                                  "ln"->JsNumber(lon) ::
                                  "lt"->JsNumber(lat) ::
                                  "lnd"->JsNumber(lonDelta) ::
                                  "ltd"->JsNumber(latDelta) ::
                                  Nil
                              )::
                              "d"->lsFotos:: Nil
                            )
                          )
                          
                        }
                    );
                    
                    
                    

                  
                }
                else
                {
                     Status(503)(
                          JsObject(
                            Seq("mensaje"->JsString("Expecting Json data"))
                            )
                       );
                }    
//           } }.getOrElse {
//                      Status(503)(
//                JsObject(
//                  Seq("mensaje"->JsString("Expecting Json data"))
//                  )
//             );
//
//          }
    
   }



  }
  
  
  
  
  
     def platoDelDia() = Action {implicit request =>  { 
           Logger.info("restauranteDia") 
//      request.body.asJson.map { json => {
          
    //    json.v
        
                  val usuario:Option[String] = request.getQueryString("us")   //json.asOpt[String]((JsPath \ "us").read[String]);
                  
                  //\("idFace").astoString();
                  val longitude:Option[String]  =  request.getQueryString("lon")  //  json.asOpt[String]((JsPath \ "lon").read[String]);
                    //json.\("email").as[String];
                  val latitude:Option[String]  =  request.getQueryString("lat")  //  json.asOpt[String]((JsPath \ "lat").read[String]);
                    //json.\("email").as[String];
             

                  
           if( usuario !=None
                && longitude!=None
                && latitude!=None 
                && PeticionesRest.toDouble(longitude.get)!=None
                && PeticionesRest.toDouble(latitude.get)!=None
               )
           {
             
             
             val locUsu= new Loc(longitude=  PeticionesRest.toDouble(longitude.get).get, latitude=PeticionesRest.toDouble(latitude.get).get);
             
            // val idFoto:String="558ebeda231851f51ae176a8";
    
             
             
         
             val fotoPlato=FotoUsuario.obtenerFotoDelDia(locUsu);
             
             if(/*comentarios.isLeft ||*/ fotoPlato.isLeft  )
             {
       
                   Logger.error("error obtenr informacion") 
                   Status(503)(
                      JsObject(
                        Seq("mensaje"->JsString("error obtenr informacion"))
                        )
                   );
                              
             }
             else
             {

               

      
                     
                     //=comentarios.right
                        
                        
                        
                    val foto=fotoPlato.right.get;    
                    
                     Logger.info("foto._id="+foto._id.toString()); 
                    val comentarios=ComentPlatos.comentarioListaPlatos(foto._id);
    
                     val lsComentario:JsArray = comentarios match{
                       case Right(x) =>{
                         
                             x.map(new Function1[ComentPlatos,JsObject]{
                             def apply(ft:ComentPlatos): JsObject = {
                                   
                                  return new  JsObject(
                                                "usu"->JsString(ft.idUser) ::
                                                "url"->JsString(null) ::
                                                 "nAusu"->JsString(ft.nArchivoUsu)::
                                                "txt"->JsString(ft.txtComent) ::
                                                "fch"->JsString(ft.fecha.toString()) ::
                                              Nil
                                      );
                                }
                                
                              }).foldLeft(JsArray ())((z,x) =>   z ++ Json.arr(x) ) ;
                       }
                       case Left(x) => JsArray (); 
                     }
                   
                   Ok(      
                       JsObject(
                            "id"->JsString(foto._id.toString())::  
                            "nC"->JsString(foto.uploder)::
                            "nA"->JsString(foto.nombreArchivo)::
                            "nAusu"->JsString(foto.nArchivoUsu)::
                            "fch"->JsNumber(foto.fecha.getTime())::
                            "ltF"->JsString(calcularTiempo(foto.fecha) )::
                            "dir"->//JsString(foto.loc.direccionCorta)::
                            JsString(obtenerDireccion(foto.loc.direccionCorta) )::   
                            "nlo"->JsString(foto.infoFoto.nombreRestaurante)::   
                            "npl"->JsString(foto.infoFoto.nombrePlato)::   
                            "dst"->JsString(calcularDistancia(foto.loc,locUsu) )::   
                            "prc"->JsString(foto.infoFoto.valorEstimado)::   
                             "g"->JsObject(
                                              "lt"->JsNumber(foto.loc.latitude )::
                                              "ln"->JsNumber(foto.loc.longitude) ::
                                              Nil 
                                          )::
                            "inf"->JsObject(
                                "lk"->JsNumber(2) ::
                                "nlk"->JsNumber(2) ::
                                "cm"->JsNumber(2) ::
                                "clf"->JsNumber(3) ::
                                Nil
                            )::
                            "lcm"->lsComentario
                            ::Nil

                          )
                              
                   
                     )
                          
                        
                        
                            
                            
             }
             
            
             
             
           }
            else
            {
                         Logger.error("no se envio todos los parametros") 
                          Status(503)(
                                      JsObject(
                                        Seq("mensaje"->JsString("Expecting Json data"))
                                        )
                                   );
                        // BadRequest("Expecting Json data")
              
            }
          
      
          }

}
  
     
 
   def platoBuscarTipoComida() = Action {implicit request =>  { 
        Logger.info("platoBuscarNombre") 
//      request.body.asJson.map { json => {
          
    //    json.v
        
                  val usuario:Option[String] = request.getQueryString("us")   //json.asOpt[String]((JsPath \ "us").read[String]);
                  
                  //\("idFace").astoString();
                  val texto:Option[String]  =  request.getQueryString("text")  //  json.asOpt[String]((JsPath \ "lon").read[String]);
                    //json.\("email").as[String];
              

                  
           if( usuario !=None
                && texto!=None
               )
           {
             
              Logger.info("platoBuscarNombre despues del if") 
             
   
    
             
             
             val nombrePlatos=BusquedaPlato.obtenerListaTipoComidaPlato(texto.get);
    
             
             if(nombrePlatos.isLeft  )
             {
       
                   Logger.error("error obtenr informacion") 
                   Status(503)(
                      JsObject(
                        Seq("mensaje"->JsString("error obtenr informacion"))
                        )
                   );
                              
             }
             else
             {

               
Logger.info("platoBuscarNombre despues del else") 
                  
                     val lsNombrePlatos:JsArray=nombrePlatos.right.get.map(new Function1[BusquedaPlato,JsObject]{
                     def apply(ft:BusquedaPlato): JsObject = {
                           
                      
                          return new  JsObject(
                                        "idp"->JsString(ft._id.toString()) ::
                                        "npl"->JsString(ft.infoFoto.nombrePlato) ::

                                      Nil
                              );
                        }
                        
                      }).foldLeft(JsArray ())((z,x) =>   z ++ Json.arr(x) ) ;
                        
                        
                        
                  
                   
                   Ok(      
                        lsNombrePlatos
                              
                   
                     )
                          
                        
                        
                            
                            
             }
             
            
             
             
           }
            else
            {
                         Logger.error("no se envio todos los parametros") 
                          Status(503)(
                                      JsObject(
                                        Seq("mensaje"->JsString("Expecting Json data"))
                                        )
                                   );
                        // BadRequest("Expecting Json data")
              
            }
          
      
          }

}
   
  
def platoBuscarNombre() = Action {implicit request =>  { 
        Logger.info("platoBuscarNombre") 
//      request.body.asJson.map { json => {
          
    //    json.v
        
                  val usuario:Option[String] = request.getQueryString("us")   //json.asOpt[String]((JsPath \ "us").read[String]);
                  
                  //\("idFace").astoString();
                  val texto:Option[String]  =  request.getQueryString("text")  //  json.asOpt[String]((JsPath \ "lon").read[String]);
                    //json.\("email").as[String];
              

                  
           if( usuario !=None
                && texto!=None
               )
           {
             
              Logger.info("platoBuscarNombre despues del if") 
             
   
    
             
             
             val nombrePlatos=BusquedaPlato.obtenerListaNombrePlato(texto.get);
    
             
             if(nombrePlatos.isLeft  )
             {
       
                   Logger.error("error obtenr informacion") 
                   Status(503)(
                      JsObject(
                        Seq("mensaje"->JsString("error obtenr informacion"))
                        )
                   );
                              
             }
             else
             {

               
                    Logger.info("platoBuscarNombre despues del else") 
                  
                     val lsNombrePlatos:JsArray=nombrePlatos.right.get.map(new Function1[BusquedaPlato,JsObject]{
                     def apply(ft:BusquedaPlato): JsObject = {
                           
                      
                          return new  JsObject(
                                        "idp"->JsString(ft._id.toString()) ::
                                        "npl"->JsString(ft.infoFoto.nombrePlato) ::

                                      Nil
                              );
                        }
                        
                      }).foldLeft(JsArray ())((z,x) =>   z ++ Json.arr(x) ) ;
                        
                        
                        
                  
                   
                   Ok(      
                        lsNombrePlatos
                              
                   
                     )
                          
                        
                        
                            
                            
             }
             
            
             
             
           }
            else
            {
                         Logger.error("no se envio todos los parametros") 
                          Status(503)(
                                      JsObject(
                                        Seq("mensaje"->JsString("Expecting Json data"))
                                        )
                                   );
                        // BadRequest("Expecting Json data")
              
            }
          
      
          }

}
 


def platoBuscarPrecio(id:Int) = Action {implicit request =>  { 
           Logger.info("restauranteDia") 
//      request.body.asJson.map { json => {
          
    //    json.v
        
                  val usuario:Option[String] = request.getQueryString("us")   //json.asOpt[String]((JsPath \ "us").read[String]);
                  
                  //\("idFace").astoString();
                  val longitude:Option[String]  =  request.getQueryString("ln")  //  json.asOpt[String]((JsPath \ "lon").read[String]);
                    //json.\("email").as[String];
                  val latitude:Option[String]  =  request.getQueryString("lt")  //  json.asOpt[String]((JsPath \ "lat").read[String]);
                    //json.\("email").as[String];
             

                  
           if( usuario !=None
                && longitude!=None
                && latitude!=None 
                && PeticionesRest.toDouble(longitude.get)!=None
                && PeticionesRest.toDouble(latitude.get)!=None
               )
           {
             

            val nombrePlatos=BusquedaPlato.obtenerListaPrecioPlato(id,longitude.get.toDouble,latitude.get.toDouble);
    
             
             if(nombrePlatos.isLeft  )
             {
       
                   Logger.error("error obtenr informacion") 
                   Status(503)(
                      JsObject(
                        Seq("mensaje"->JsString("error obtenr informacion"))
                        )
                   );
                              
             }
             else
             {

               
               
                 val lsFotos:JsArray=nombrePlatos.right.get.map(new Function1[FotoPlatos,JsObject]{
                            def apply(ft:FotoPlatos): JsObject = {
                               
                              return new  JsObject(
                                          "id"->JsString(ft._id.toString())::
                                          "nA"->JsString(ft.nombreArchivo)::
                                          "g"->JsObject(
                                              "lt"->JsNumber(ft.loc.latitude )::
                                              "ln"->JsNumber(ft.loc.longitude) ::
                                              Nil 
                                          )::
                                           Nil
                                  );
                            }
                            
                          }).foldLeft(JsArray ())((z,x) =>   z ++ Json.arr(x) ) ;
                          
                          Ok(lsFotos )
               

//                  
//                     val lsFotos:JsArray=nombrePlatos.right.get.map(new Function1[BusquedaPlato,JsObject]{
//                     def apply(ft:BusquedaPlato): JsObject = {
//                           
//                      
//                          return new  JsObject(
//                                        "idp"->JsString(ft._id.toString()) ::
//                                        "npl"->JsString(ft.infoFoto.nombrePlato) ::
//
//                                      Nil
//                              );
//                        }
//                        
//                      }).foldLeft(JsArray ())((z,x) =>   z ++ Json.arr(x) ) ;
//                        
                        
                        
                  
                   
                   //  Ok(lsFotos )
                          
                        
                        
                            
                            
             }
             
             
             
           }
            else
            {
                         Logger.error("no se envio todos los parametros") 
                          Status(503)(
                                      JsObject(
                                        Seq("mensaje"->JsString("Expecting Json data"))
                                        )
                                   );
                        // BadRequest("Expecting Json data")
              
            }
          
      
          }

}
 

  
 def datoRestaurant( idRest:String) = Action {implicit request =>  { 
           Logger.info("restauranteDia") 
//      request.body.asJson.map { json => {
          
    //    json.v
        
                  val usuario:Option[String] = request.getQueryString("us")   //json.asOpt[String]((JsPath \ "us").read[String]);
                  
                  //\("idFace").astoString();
                  val longitude:Option[String]  =  request.getQueryString("lon")  //  json.asOpt[String]((JsPath \ "lon").read[String]);
                    //json.\("email").as[String];
                  val latitude:Option[String]  =  request.getQueryString("lat")  //  json.asOpt[String]((JsPath \ "lat").read[String]);
                    //json.\("email").as[String];
             

                  
           if( usuario !=None
                && longitude!=None
                && latitude!=None 
                && PeticionesRest.toDouble(longitude.get)!=None
                && PeticionesRest.toDouble(latitude.get)!=None
               )
           {
             
             
             val locUsu= new Loc(longitude=  PeticionesRest.toDouble(longitude.get).get, latitude=PeticionesRest.toDouble(latitude.get).get);
             
           //  val idFoto:String="548c971a6fe8e58a7b39eb27";
    
    
    
             FotoUsuario.obtenerFoto(idRest).fold((ex)=>{
                              Logger.error("Error obtener documento",ex) 
                              Status(503)(
                                    JsObject(
                                      Seq("mensaje"->JsString("Error obtener documento"))
                                      )
                                 );
                            
                          
                        },
                        (foto)=>{
                           Ok(      
                               JsObject(
                                    "id"->JsString(idRest)::  
                                    "nC"->JsString(foto.uploder)::
                                    "nA"->JsString(foto.nombreArchivo)::
                                    "fch"->JsNumber(foto.fecha.getTime())::
                                    "ltF"->JsString(calcularTiempo(foto.fecha) )::
                                    "dir"->JsString(obtenerDireccion(foto.loc.direccionCorta) )::   
                                    "nlo"->JsString(foto.infoFoto.nombreRestaurante)::   
                                    "npl"->JsString(foto.infoFoto.nombrePlato)::   
                                    "dst"->JsString(calcularDistancia(foto.loc,locUsu) )::   
                                    "prc"->JsString(foto.infoFoto.valorEstimado)::   
                                    "g"->JsObject(
                                              "lt"->JsNumber(foto.loc.latitude )::
                                              "ln"->JsNumber(foto.loc.longitude) ::
                                              Nil 
                                          )::
                                    "inf"->JsObject(
                                        "lk"->JsNumber(2) ::
                                        "nlk"->JsNumber(2) ::
                                        "cm"->JsNumber(2) ::
                                        "clf"->JsNumber(3) ::
                                        Nil
                                    ):: Nil
                                  )
                                      
                           
                             )
                          
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
                        // BadRequest("Expecting Json data")
              
            }
          
      
          }

}  

def platoFotoMapaUsuario() =  Action {request =>{ 
           Logger.info("listaRestaurant") 
//      request.body.asJson.map { json => {

            val user: Option[String] = request.getQueryString("us")  //json.asOpt[String]((JsPath \ "lt").read[String]); 
            val _lat: Option[String] = request.getQueryString("lt")  //json.asOpt[String]((JsPath \ "lt").read[String]); 
            val _lon: Option[String] = request.getQueryString("ln") //json.asOpt[String]((JsPath \ "ln").read[String]);
            
            val _latDelta: Option[String] = request.getQueryString("ltd") //json.asOpt[String]((JsPath \ "ltd").read[String]);
            val _lonDelta: Option[String] = request.getQueryString("lnd") // json.asOpt[String]((JsPath \ "lnd").read[String]);
            
            
            if(
               user!=None
                && _lat!=None
                && _latDelta!=None
                && _lon!=None
                && _lonDelta!=None
                && PeticionesRest.toDouble (_lat.get)!=None
                && PeticionesRest.toDouble (_latDelta.get)!=None
                && PeticionesRest.toDouble (_lon.get)!=None
                && PeticionesRest.toDouble (_lonDelta.get)!=None
                
                )
            {
              
               
                    val lat:Double= PeticionesRest.toDouble (_lat.get).get; 
                    val lon:Double= PeticionesRest.toDouble (_lon.get).get; 
                    
                    val latDelta:Double= PeticionesRest.toDouble (_latDelta.get).get; 
                    val lonDelta:Double= PeticionesRest.toDouble (_lonDelta.get).get;
                    
                   
                    val locRest= new Loc(longitude=lon, latitude=lat,longitudeDelta=lonDelta,latitudeDelta=latDelta)
                    FotoPlatos.obtenerListaPlatosUsuario(user.get).fold((ex)=>{

                              Logger.error("Error al obtener documento",ex) 
                              Status(503)(
                                    JsObject(
                                      Seq("mensaje"->JsString("error al obtener documento"))
                                      )
                                 );
                            
                          
                        },
                        (listaFoto:List[FotoPlatos])=>{
                           
                          
                          val lsFotos:JsArray=listaFoto.map(new Function1[FotoPlatos,JsObject]{
                            def apply(ft:FotoPlatos): JsObject = {
                               
                              return new  JsObject(
                                          "i"->JsString(ft._id.toString())::

                                          "nA"->JsString(ft.nombreArchivo)::
                                                "g"->JsObject(
                                                          "ln"->JsNumber(ft.loc.longitude) ::
                                                          "lt"->JsNumber(ft.loc.latitude) ::
                                                          Nil
                                                 )::
                                                 Nil
                                  );
                            }
                            
                          }).foldLeft(JsArray ())((z,x) =>   z ++ Json.arr(x) ) ;
                          
                          Ok(
                            JsObject(
                              "r"->JsNumber(20)::  
                               "g"->JsObject(
                                  "ln"->JsNumber(lon) ::
                                  "lt"->JsNumber(lat) ::
                                  "lnd"->JsNumber(lonDelta) ::
                                  "ltd"->JsNumber(latDelta) ::
                                  Nil
                              )::
                              "d"->lsFotos:: Nil
                            )
                          )
                          
                        }
                    );
                    
                    
                    

                  
                }
                else
                {
                     Status(503)(
                          JsObject(
                            Seq("mensaje"->JsString("Expecting Json data"))
                            )
                       );
                }    

    
   }



  }
  
def platoFotoUsuario() =  Action {request =>{ 
           Logger.info("platoFotoUsuario") 

            val user: Option[String] = request.getQueryString("us")  //json.asOpt[String]((JsPath \ "lt").read[String]); 
            val _lat: Option[String] = request.getQueryString("lt")  //json.asOpt[String]((JsPath \ "lt").read[String]); 
            val _lon: Option[String] = request.getQueryString("ln") //json.asOpt[String]((JsPath \ "ln").read[String]);
            
            //val _latDelta: Option[String] = request.getQueryString("ltd") //json.asOpt[String]((JsPath \ "ltd").read[String]);
            //val _lonDelta: Option[String] = request.getQueryString("lnd") // json.asOpt[String]((JsPath \ "lnd").read[String]);
            
            
            if(
               user!=None
                && _lat!=None
               // && _latDelta!=None
                && _lon!=None
               // && _lonDelta!=None
                && PeticionesRest.toDouble (_lat.get)!=None
                //&& PeticionesRest.toDouble (_latDelta.get)!=None
                && PeticionesRest.toDouble (_lon.get)!=None
               // && PeticionesRest.toDouble (_lonDelta.get)!=None
                
                )
            {
              
               
                    val lat:Double= PeticionesRest.toDouble (_lat.get).get; 
                    val lon:Double= PeticionesRest.toDouble (_lon.get).get; 
                    
                 //   val latDelta:Double= PeticionesRest.toDouble (_latDelta.get).get; 
                   // val lonDelta:Double= PeticionesRest.toDouble (_lonDelta.get).get;
                    
                   
                    val locRest= new Loc(longitude=lon, latitude=lat/*,longitudeDelta=lonDelta,latitudeDelta=latDelta*/);
                    FotoPlatos.obtenerListaPlatosUsuario(user.get).fold((ex)=>{
                            
                              Logger.error("Error al obtener documento",ex) 
                              Status(503)(
                                    JsObject(
                                      Seq("mensaje"->JsString("Error al obtener documento"))
                                      )
                                 );
                            
                          
                        },
                        (listaFoto:List[FotoPlatos])=>{
                           
                          
                          val lsFotos:JsArray=listaFoto.map(new Function1[FotoPlatos,JsObject]{
                            def apply(ft:FotoPlatos): JsObject = {
                               
                              return new  JsObject(
                                          "id"->JsString(ft._id.toString())::
                                          "nA"->JsString(ft.nombreArchivo):: 
                                           "g"->JsObject(
                                              "lt"->JsNumber(ft.loc.latitude )::
                                              "ln"->JsNumber(ft.loc.longitude) ::
                                              Nil 
                                          )::
                                           Nil
                                  );
                            }
                            
                          }).foldLeft(JsArray ())((z,x) =>   z ++ Json.arr(x) ) ;
                          
                          Ok(lsFotos )
                          
                        }
                    );
                    
                    
                    

                  
                }
                else
                {
                     Status(503)(
                          JsObject(
                            Seq("mensaje"->JsString("Expecting Json data"))
                            )
                       );
                }    
    
   }
}
  
  
def platoVotados() =  Action {request =>{ 
           Logger.info("platoVotados") 

            val user: Option[String] = request.getQueryString("us")  //json.asOpt[String]((JsPath \ "lt").read[String]); 
            val _lat: Option[String] = request.getQueryString("lat")  //json.asOpt[String]((JsPath \ "lt").read[String]); 
            val _lon: Option[String] = request.getQueryString("lon") //json.asOpt[String]((JsPath \ "ln").read[String]);
            
            //val _latDelta: Option[String] = request.getQueryString("ltd") //json.asOpt[String]((JsPath \ "ltd").read[String]);
            //val _lonDelta: Option[String] = request.getQueryString("lnd") // json.asOpt[String]((JsPath \ "lnd").read[String]);
            
            
            if(
               user!=None
                && _lat!=None
               // && _latDelta!=None
                && _lon!=None
               // && _lonDelta!=None
                && PeticionesRest.toDouble (_lat.get)!=None
                //&& PeticionesRest.toDouble (_latDelta.get)!=None
                && PeticionesRest.toDouble (_lon.get)!=None
               // && PeticionesRest.toDouble (_lonDelta.get)!=None
                
                )
            {
              
               
                    val lat:Double= PeticionesRest.toDouble (_lat.get).get; 
                    val lon:Double= PeticionesRest.toDouble (_lon.get).get; 
                    
                 //   val latDelta:Double= PeticionesRest.toDouble (_latDelta.get).get; 
                   // val lonDelta:Double= PeticionesRest.toDouble (_lonDelta.get).get;
                    
                   
                    val locRest= new Loc(longitude=lon, latitude=lat/*,longitudeDelta=lonDelta,latitudeDelta=latDelta*/);
                    FotoPlatos.obtenerListaPlatosVotados(locRest).fold((ex)=>{
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
    												  Logger.error("Error en insersion documento",ex._2) 
  												  	Status(503)(
                    		            JsObject(
                      		            Seq("mensaje"->JsString("error al insertar registro usuario"))
                      		            )
                    		         );
    												}
    											
	        	   					},
	        	   					(listaFoto:List[FotoPlatos])=>{
	        	   					   
	        	   					  
	        	   					  val lsFotos:JsArray=listaFoto.map(new Function1[FotoPlatos,JsObject]{
	        	   					    def apply(ft:FotoPlatos): JsObject = {
	        	   					       
	        	   					      return new  JsObject(
	        	   					                  "id"->JsString(ft._id.toString())::
	        	   					                  "nA"->JsString(ft.nombreArchivo)::
                                          "g"->JsObject(
                                              "lt"->JsNumber(ft.loc.latitude )::
                                              "ln"->JsNumber(ft.loc.longitude) ::
                                              Nil 
                                          )::
                                           Nil
	        	   					          );
	        	   					    }
	        	   					    
	        	   					  }).foldLeft(JsArray ())((z,x) =>   z ++ Json.arr(x) ) ;
	        	   					  
      	        	   			Ok(
                              JsObject(

                              "lst"->lsFotos:: Nil
                                )
                              
                            )
	        	   					  
	        	            }
	        	   			);
                    
                    
                    

                  
                }
                else
                {
                     Status(503)(
                    	    JsObject(
                    		    Seq("mensaje"->JsString("Expecting Json data"))
                    		    )
                    	 );
                }    
//        	 } }.getOrElse {
//                    	Status(503)(
//        		    JsObject(
//        			    Seq("mensaje"->JsString("Expecting Json data"))
//        			    )
//        		 );
//
//          }
    
   }



  }
  
  

 def platosComentados(idRest:String) =  Action {request =>{ 
           Logger.info("restauranteComentados")  

           
               ComentPlatos.comentarioListaPlatos(idRest).fold((ex:(Boolean,Exception))=>{
                 
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
                              Logger.error("Error en insersion documento",ex._2) 
                              Status(503)(
                                    JsObject(
                                      Seq("mensaje"->JsString("error al insertar registro usuario"))
                                      )
                                 );
                            }
                 
               }, (listaComent:List[ComentPlatos])=>{
                 
                           val lsFotos:JsArray=listaComent.map(new Function1[ComentPlatos,JsObject]{
                            def apply(ft:ComentPlatos): JsObject = {
                               
                              return new  JsObject(
                                  
                                                "usu"->JsString(ft.idUser) ::
                                                 "nAusu"->JsString(ft.nArchivoUsu)::
                                                "txt"->JsString(ft.txtComent) ::
                                                "fch"->JsString(ft.fecha.toString()) ::
                                  
                                  
                             
                                          Nil
                                  );
                            }
                            
                          }).foldLeft(JsArray ())((z,x) =>   z ++ Json.arr(x) ) ;
                          
                          Ok( JsObject(
                        Seq("lcm"->lsFotos)
                              ))
               })
  
                               

           
       }
     }
    
  
  
  
 
 def datoPlato( idRest:String) = Action {implicit request =>  { 
           Logger.info("restauranteDia") 
//      request.body.asJson.map { json => {
          
    //    json.v
        
                  val usuario:Option[String] = request.getQueryString("us")   //json.asOpt[String]((JsPath \ "us").read[String]);
                  
                  //\("idFace").astoString();
                  val longitude:Option[String]  =  request.getQueryString("lon")  //  json.asOpt[String]((JsPath \ "lon").read[String]);
                    //json.\("email").as[String];
                  val latitude:Option[String]  =  request.getQueryString("lat")  //  json.asOpt[String]((JsPath \ "lat").read[String]);
                    //json.\("email").as[String];
             

                  
           if( usuario !=None
                && longitude!=None
                && latitude!=None 
                && PeticionesRest.toDouble(longitude.get)!=None
                && PeticionesRest.toDouble(latitude.get)!=None
               )
           {
             
             
             val locUsu= new Loc(longitude=  PeticionesRest.toDouble(longitude.get).get, latitude=PeticionesRest.toDouble(latitude.get).get);
             
           //  val idFoto:String="548c971a6fe8e58a7b39eb27";
    
             val comentarios=ComentPlatos.comentarioListaPlatos(idRest);
    
             val fotoPlato=FotoUsuario.obtenerFoto(idRest);
             
             if(comentarios.isLeft || fotoPlato.isLeft  )
             {
       
                   Logger.error("error obtenr informacion") 
                   Status(503)(
                      JsObject(
                        Seq("mensaje"->JsString("error obtenr informacion"))
                        )
                   );
                              
             }
             else
             {

               

                  
                     val lsComentario:JsArray=comentarios.right.get.map(new Function1[ComentPlatos,JsObject]{
                     def apply(ft:ComentPlatos): JsObject = {
                           
                          return new  JsObject(
                                        "usu"->JsString(ft.idUser) ::
                                        "url"->JsString(null) ::
                                        "txt"->JsString(ft.txtComent) ::
                                        "nAusu"->JsString(ft.nArchivoUsu)::
                                        "fch"->JsString(ft.fecha.toString()) ::
                                      Nil
                              );
                        }
                        
                      }).foldLeft(JsArray ())((z,x) =>   z ++ Json.arr(x) ) ;
                        
                        
                        
                    val foto=fotoPlato.right.get;    
                    
                   
                   Ok(      
                       JsObject(
                            "id"->JsString(idRest)::  
                            "nC"->JsString(foto.uploder)::
                            "nA"->JsString(foto.nombreArchivo)::
                            "fch"->JsNumber(foto.fecha.getTime())::
                            "nAusu"->JsString(foto.nArchivoUsu)::
                            "ltF"->JsString(calcularTiempo(foto.fecha) )::
                            "dir"->JsString(obtenerDireccion(foto.loc.direccionCorta) )::   
                            "nlo"->JsString(foto.infoFoto.nombreRestaurante)::   
                            "npl"->JsString(foto.infoFoto.nombrePlato)::   
                            "dst"->JsString(calcularDistancia(foto.loc,locUsu) )::   
                            "prc"->JsString(foto.infoFoto.valorEstimado)::   
                             "g"->JsObject(
                                              "lt"->JsNumber(foto.loc.latitude )::
                                              "ln"->JsNumber(foto.loc.longitude) ::
                                              Nil 
                                          )::
                            "inf"->JsObject(
                                "lk"->JsNumber(2) ::
                                "nlk"->JsNumber(2) ::
                                "cm"->JsNumber(2) ::
                                "clf"->JsNumber(3) ::
                                Nil
                            )::
                            "lcm"->lsComentario
                            ::Nil

                          )
                              
                   
                     )
                          
                        
                        
                            
                            
             }

             
           }
            else
            {
                         Logger.error("no se envio todos los parametros") 
                          Status(503)(
                                      JsObject(
                                        Seq("mensaje"->JsString("Expecting Json data"))
                                        )
                                   );
                        // BadRequest("Expecting Json data")
              
            }
          
      
          }

}
   

  
   def platosPuntuados() =  Action {request =>{ 
           Logger.info("listaRestaurant") 

            val user: Option[String] = request.getQueryString("us")  //json.asOpt[String]((JsPath \ "lt").read[String]); 
            val _lat: Option[String] = request.getQueryString("lt")  //json.asOpt[String]((JsPath \ "lt").read[String]); 
            val _lon: Option[String] = request.getQueryString("ln") //json.asOpt[String]((JsPath \ "ln").read[String]);
            
           // val _latDelta: Option[String] = request.getQueryString("ltd") //json.asOpt[String]((JsPath \ "ltd").read[String]);
           /// val _lonDelta: Option[String] = request.getQueryString("lnd") // json.asOpt[String]((JsPath \ "lnd").read[String]);
            
            
            if(
               user!=None
                && _lat!=None
              //  && _latDelta!=None
                && _lon!=None
               // && _lonDelta!=None
                && PeticionesRest.toDouble (_lat.get)!=None
               // && PeticionesRest.toDouble (_latDelta.get)!=None
                && PeticionesRest.toDouble (_lon.get)!=None
               // && PeticionesRest.toDouble (_lonDelta.get)!=None
                
                )
            {
              
               
                    val lat:Double= PeticionesRest.toDouble (_lat.get).get; 
                    val lon:Double= PeticionesRest.toDouble (_lon.get).get; 
                    
                 //   val latDelta:Double= PeticionesRest.toDouble (_latDelta.get).get; 
                  //  val lonDelta:Double= PeticionesRest.toDouble (_lonDelta.get).get;
                    
                   
                    val locRest= new Loc(longitude=lon, latitude=lat/*,longitudeDelta=lonDelta,latitudeDelta=latDelta*/)
                    FotoPlatos.obtenerListaPlatosPuntuados(locRest).fold((ex)=>{
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
    												  Logger.error("Error en insersion documento",ex._2) 
  												  	Status(503)(
                    		            JsObject(
                      		            Seq("mensaje"->JsString("error al insertar registro usuario"))
                      		            )
                    		         );
    												}
    											
	        	   					},
	        	   					(listaFoto:List[FotoPlatos])=>{
	        	   					   
	        	   					  
	        	   					  val lsFotos:JsArray=listaFoto.map(new Function1[FotoPlatos,JsObject]{
	        	   					    def apply(ft:FotoPlatos): JsObject = {
	        	   					       
	        	   					      return new  JsObject(
	        	   					                  "id"->JsString(ft._id.toString())::
	        	   					                  "nA"->JsString(ft.nombreArchivo):: 
                                           "g"->JsObject(
                                              "lt"->JsNumber(ft.loc.latitude )::
                                              "ln"->JsNumber(ft.loc.longitude) ::
                                              Nil 
                                          )::
	        	   					                  Nil
	        	   					          );
	        	   					    }
	        	   					    
	        	   					  }).foldLeft(JsArray ())((z,x) =>   z ++ Json.arr(x) ) ;
	        	   					  
      	        	   			Ok(lsFotos)
	        	   					  
	        	            }
	        	   			);
                    
                    
                    

                  
                }
                else
                {
                     Status(503)(
                    	    JsObject(
                    		    Seq("mensaje"->JsString("Expecting Json data"))
                    		    )
                    	 );
                }    

    
   }



  }





  
  
}
