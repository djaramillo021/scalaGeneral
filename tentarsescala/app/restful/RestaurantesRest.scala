package restful


import util.PeticionesRest
import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.collection.immutable.Seq
import models.FotoUsuario
import models.Loc
import models.FotoRestaurantes
import models.ComentRestaurantes
import org.joda.time.DateTime
import org.joda.time.Period
import java.util.Date




object RestaurantesRest extends Controller  {
  
  
 

def calcularTiempo(_fecha:Date):String=
  {
    //todo
    val fechaActual:DateTime=  DateTime.now()
    val fecha:DateTime=  new DateTime(_fecha.getTime());
    val  diff:Period = new Period(fecha, fechaActual);
    //val delta:DateTime= fechaActual.minus(fecha);
    if(diff.getHours()<=24)
    {
       return "Hace "+diff.getHours()+" horas"
    }
    else if(diff.getDays()<=30)
    {
       return "Hace "+diff.getDays()+" dias"
    }
    else if(diff.getMonths()<=12)
    {
      return "Hace "+diff.getMonths()+" meses"
    }
    else 
    {
         return "Hace "+diff.getYears()+" años"
    }
    
  }
    def obtenerDireccion(locRest:Loc):String=
  {
    //todo
    return "Av.de mar 1996"
  }

  
  def calcularDistancia(locRest:Loc,locUsu:Loc):String=
  {
    //todo
    return "2,6 km"
  }

  
  

  
   def listaRestaurant() =  Action {request =>{ 
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

    
   }



  }
  
  
  
  
  
  
  

  
   def restauranteDia() = Action {implicit request =>  { 
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
             
             val idFoto:String="548c971a6fe8e58a7b39eb27";
    
    
    
             FotoUsuario.obtenerFoto(idFoto).fold((ex)=>{

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
                                    "id"->JsString(idFoto)::  
                                    "nC"->JsString(foto.uploder)::
                                    "nA"->JsString(foto.nombreArchivo)::
                                    "fch"->JsNumber(foto.fecha.getTime())::
                                    "ltF"->JsString(calcularTiempo(foto.fecha) )::
                                    "dir"->JsString(obtenerDireccion(foto.loc) )::   
                                    "nlo"->JsString(foto.infoFoto.nombreRestaurante)::   
                                    "npl"->JsString(foto.infoFoto.nombrePlato)::   
                                    "dst"->JsString(calcularDistancia(foto.loc,locUsu) )::   
                                    "prc"->JsString(foto.infoFoto.valorEstimado)::   
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
                                    "dir"->JsString(obtenerDireccion(foto.loc) )::   
                                    "nlo"->JsString(foto.infoFoto.nombreRestaurante)::   
                                    "npl"->JsString(foto.infoFoto.nombrePlato)::   
                                    "dst"->JsString(calcularDistancia(foto.loc,locUsu) )::   
                                    "prc"->JsString(foto.infoFoto.valorEstimado)::   
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
   
   
   
   

   
    def restauranteVotados() =  Action {request =>{ 
           Logger.info("platoVotados") 

            val user: Option[String] = request.getQueryString("us")  //json.asOpt[String]((JsPath \ "lt").read[String]); 
            val _lat: Option[String] = request.getQueryString("lt")  //json.asOpt[String]((JsPath \ "lt").read[String]); 
            val _lon: Option[String] = request.getQueryString("ln") //json.asOpt[String]((JsPath \ "ln").read[String]);
            
            //val _latDelta: Option[String] = request.getQueryString("ltd") //json.asOpt[String]((JsPath \ "ltd").read[String]);
            //val _lonDelta: Option[String] = request.getQueryString("lnd") // json.asOpt[String]((JsPath \ "lnd").read[String]);
            
            
            if(
               user!=None
                && _lat!=None
              //  && _latDelta!=None
                && _lon!=None
                //&& _lonDelta!=None
                && PeticionesRest.toDouble (_lat.get)!=None
               // && PeticionesRest.toDouble (_latDelta.get)!=None
                && PeticionesRest.toDouble (_lon.get)!=None
               // && PeticionesRest.toDouble (_lonDelta.get)!=None
                
                )
            {
              
               
                    val lat:Double= PeticionesRest.toDouble (_lat.get).get; 
                    val lon:Double= PeticionesRest.toDouble (_lon.get).get; 
                    
               
                    
                   
                    val locRest= new Loc(longitude=lon, latitude=lat/*,longitudeDelta=lonDelta,latitudeDelta=latDelta*/);
                    FotoRestaurantes.obtenerListaRestauranteVotados(locRest).fold((ex)=>{
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
	        	   					(listaFoto:List[FotoRestaurantes])=>{
	        	   					   
	        	   					  
	        	   					  val lsFotos:JsArray=listaFoto.map(new Function1[FotoRestaurantes,JsObject]{
	        	   					    def apply(ft:FotoRestaurantes): JsObject = {
	        	   					       
	        	   					      return new  JsObject(
	        	   					                  "id"->JsString(ft._id.toString())::
	        	   					                  "nA"->JsString(ft.nombreArchivo):: 
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
  
  
  
  
  
  
  
     def restauranteComentados(idRest:String) =  Action {request =>{ 
           Logger.info("restauranteComentados")  

           
               ComentRestaurantes.comentarioListaRestaurante(idRest).fold((ex:(Boolean,Exception))=>{
                 
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
                 
               }, (listaComent:List[ComentRestaurantes])=>{
                 
                           val lsFotos:JsArray=listaComent.map(new Function1[ComentRestaurantes,JsObject]{
                            def apply(ft:ComentRestaurantes): JsObject = {
                               
                              return new  JsObject(
                                          "id"->JsString(ft._id.toString())::
                                          "fc"->JsNumber(ft.fecha.getTime):: 
                                          "iU"->JsString(ft.idUser.toString()):: 
                                          "tc"->JsString(ft.txtComent):: 
                                          Nil
                                  );
                            }
                            
                          }).foldLeft(JsArray ())((z,x) =>   z ++ Json.arr(x) ) ;
                          
                          Ok(lsFotos)
               })
  
                               

           
       }
     }
    
    

  
   def restaurantePuntuados() =  Action {request =>{ 
           Logger.info("listaRestaurant") 

            val user: Option[String] = request.getQueryString("us")  //json.asOpt[String]((JsPath \ "lt").read[String]); 
            val _lat: Option[String] = request.getQueryString("lt")  //json.asOpt[String]((JsPath \ "lt").read[String]); 
            val _lon: Option[String] = request.getQueryString("ln") //json.asOpt[String]((JsPath \ "ln").read[String]);
           
            
            if(
               user!=None
                && _lat!=None
               // && _latDelta!=None
                && _lon!=None
                //&& _lonDelta!=None
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
                    
                   
                    val locRest= new Loc(longitude=lon, latitude=lat/*,longitudeDelta=lonDelta,latitudeDelta=latDelta*/)
                    FotoRestaurantes.obtenerListaRestaurantePuntuados(locRest).fold((ex)=>{
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
	        	   					(listaFoto:List[FotoRestaurantes])=>{
	        	   					   
	        	   					  
	        	   					  val lsFotos:JsArray=listaFoto.map(new Function1[FotoRestaurantes,JsObject]{
	        	   					    def apply(ft:FotoRestaurantes): JsObject = {
	        	   					       
	        	   					      return new  JsObject(
	        	   					                  "id"->JsString(ft._id.toString())::
	        	   					                  "nA"->JsString(ft.nombreArchivo):: 
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
   
   
   
   
   
   
   
   
   
   
   
   
   

  
     def textoRestaurant( idRest:String) =  Action {implicit request =>  
    
    

     val texto="Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed rhoncus sodales viverra. Cras tincidunt urna velit, in iaculis enim imperdiet et. Maecenas pharetra id dolor ac ultricies. Quisque nec molestie urna, sed lobortis tortor. In sollicitudin, nunc non dignissim pharetra, turpis nisl sodales odio, vel tristique massa urna quis tellus. Donec elementum vitae purus vitae placerat. Sed blandit a justo sed gravida. Nam id sem non tellus interdum tempus non eu lectus. Sed vitae ex condimentum, convallis erat in, gravida lorem. Etiam sit amet maximus ligula. Sed molestie sodales odio, sed aliquam lectus tempus sit amet."
  
      Ok(
          JsObject(
            "id"->JsString(idRest)::  
            "txt"->JsString(texto)
            :: Nil
          )
        )
  
    }

  
  
}
