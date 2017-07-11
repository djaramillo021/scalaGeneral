package util.google


import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.HttpRequest
import com.google.api.client.http.HttpRequestFactory
import com.google.api.client.http.HttpResponse
import com.google.api.client.http.HttpTransport
import com.google.api.client.json.JsonFactory
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.client.util.Preconditions
import java.io.InputStream
import play.Play
import org.apache.commons.codec.binary.Base64
import java.security.spec.PKCS8EncodedKeySpec
import java.security.KeyFactory
import java.security.PrivateKey
import java.util.Collections
import play.Logger
import play.api.mvc.MultipartFormData.FilePart
import com.google.api.client.http.FileContent
import play.api.libs.Files.TemporaryFile
import play.api.libs.iteratee.Enumerator
import com.sksamuel.scrimage.Image
import com.sksamuel.scrimage.Format
import com.sksamuel.scrimage.io.PngWriter
import java.io.File
import com.sksamuel.scrimage.ScaleMethod




object CloudStorage {

    val  KEY:String = "XXX";
	val  SERVICE_ACCOUNT_EMAIL:String = "XXX";
	val  BUCKET_NAME:String ="tentarse";
	val  FOLDER_THUMBNAILS:String ="thumbnail"
  val  FOLDER_THUMBNAILS_PLATO:String ="thumbnailPlato"
  
	val  FOLDER_RESTAURANT:String ="restaurante"
  val  FOLDER_PLATOS:String ="platos"
  val  FOLDER_USUARIO:String ="usuarios"
	val  STORAGE_SCOPE:String = "https://www.googleapis.com/auth/devstorage.read_write";
	
	val  JSON_FACTORY:JsonFactory = JacksonFactory.getDefaultInstance();
	val httpTransport:HttpTransport = GoogleNetHttpTransport.newTrustedTransport();
	
	val privateKey:Either[Exception,GoogleCredential]=loadPrivateKey();
	
	def  loadPrivateKey():Either[Exception,GoogleCredential]=  {
		  try {
				    val encoded:Array[Byte] = Base64.decodeBase64(KEY);
				    // PKCS8 decode the encoded RSA private key
				    val keySpec:PKCS8EncodedKeySpec = new PKCS8EncodedKeySpec(encoded);
				    val kf:KeyFactory = KeyFactory.getInstance("RSA");
				    val privKey:PrivateKey = kf.generatePrivate(keySpec);
				    
				     val  credential:GoogleCredential = new GoogleCredential.Builder().setTransport(httpTransport)
		            .setJsonFactory(JSON_FACTORY)
		            .setServiceAccountId(SERVICE_ACCOUNT_EMAIL)
		            .setServiceAccountScopes(Collections.singleton(STORAGE_SCOPE))
		            /*.setServiceAccountPrivateKeyFromP12File(new File("key.p12"))*/
		            .setServiceAccountPrivateKey(privKey)
		            .build();
				    
				    
				    return Right(credential);
			}
		  catch {
			  case ex: Exception => return Left(ex);
			}
	}
	
	
  
    def uploadPlatos(file:FilePart[TemporaryFile], fileName:String):Boolean= {



     val respThumbnailPlatos:Boolean=privateKey.fold((ex)=>{Logger.error("Error al obtener credencial google",ex);  false;},
            (credential)=>{
              try{
                
                
                val inFile = file.ref.file;
                val outFile =  File.createTempFile("thl", ".tbd");

                 Image(inFile).cover(240, 160, ScaleMethod.FastScale).write(outFile);
                //compressed.
                
                  val fileContent:FileContent= new FileContent(file.contentType.get,outFile);    
                            
                  val URI:String = "https://"+BUCKET_NAME+".storage.googleapis.com/"+FOLDER_THUMBNAILS_PLATO+"/" +fileName ;
                  
                  
                  
              val requestFactory:HttpRequestFactory = httpTransport.createRequestFactory(credential);
              val url:GenericUrl = new GenericUrl(URI);
              val request:HttpRequest = requestFactory.buildPutRequest(url,fileContent);
              val response:HttpResponse = request.execute();
              val content:String = response.getHeaders().toString();   
              Logger.trace(content);
                true;
              }
              catch {
                case ex: Exception => {
                              Logger.error("Error subir archivo a google",ex);
                              false;
                            }
              }

              
              
            });
     
       val respPlatos:Boolean=privateKey.fold((ex)=>{Logger.error("Error al obtener credencial google",ex);  false;},
            (credential)=>{
              try{
                

                
                
                  val fileContent:FileContent= new FileContent(file.contentType.get,file.ref.file);               
                  val URI:String = "https://"+BUCKET_NAME+".storage.googleapis.com/"+FOLDER_PLATOS+"/"+fileName ;
                  
                  
                  
              val requestFactory:HttpRequestFactory = httpTransport.createRequestFactory(credential);
              val url:GenericUrl = new GenericUrl(URI);
              val request:HttpRequest = requestFactory.buildPutRequest(url,fileContent);
              val response:HttpResponse = request.execute();
              val content:String = response.getHeaders().toString();   
              Logger.trace(content);
                true;
              }
              catch {
                case ex: Exception => {
                              Logger.error("Error subir archivo a google",ex);
                              false;
                            }
              }

              
              
            });
     
     
     return respThumbnailPlatos && respPlatos;
  }
	
	
	
	
	
	def upload(file:FilePart[TemporaryFile], fileName:String):Boolean= {



	   val respThumbnail:Boolean=privateKey.fold((ex)=>{Logger.error("Error al obtener credencial google",ex);  false;},
	          (credential)=>{
	            try{
	              
	              
	              val inFile = file.ref.file;
                val outFile =  File.createTempFile("thl", ".tbd");
               // val image = Image(in)
                	//Image(inFile).cover(targetWidth, targetHeight, scaleMethod, position)     
//(240, 160, ScaleMethod.FastScale)        
	//              .cover(240, 160, ScaleMethod.FastScale)
	              
	              //val compressed:PngWriter =
	               Image(inFile).cover(240, 160, ScaleMethod.FastScale).write(outFile);
	              //compressed.
	              
	                val fileContent:FileContent= new FileContent(file.contentType.get,outFile);	   
	                          
	                val URI:String = "https://"+BUCKET_NAME+".storage.googleapis.com/"+FOLDER_THUMBNAILS+"/" +fileName ;
	                
	                
	                
			        val requestFactory:HttpRequestFactory = httpTransport.createRequestFactory(credential);
			        val url:GenericUrl = new GenericUrl(URI);
			        val request:HttpRequest = requestFactory.buildPutRequest(url,fileContent);
			        val response:HttpResponse = request.execute();
			        val content:String = response.getHeaders().toString();   
			        Logger.trace(content);
		            true;
	            }
	            catch {
	              case ex: Exception => {
	            	  						Logger.error("Error subir archivo a google",ex);
	            	  						false;
	            	  					}
	            }

	            
	            
	          });
	   
	     val respRestaurante:Boolean=privateKey.fold((ex)=>{Logger.error("Error al obtener credencial google",ex);  false;},
	          (credential)=>{
	            try{
	              

	              
	              
	                val fileContent:FileContent= new FileContent(file.contentType.get,file.ref.file);	              
	                val URI:String = "https://"+BUCKET_NAME+".storage.googleapis.com/"+FOLDER_RESTAURANT +"/"+fileName ;
	                
	                
	                
			        val requestFactory:HttpRequestFactory = httpTransport.createRequestFactory(credential);
			        val url:GenericUrl = new GenericUrl(URI);
			        val request:HttpRequest = requestFactory.buildPutRequest(url,fileContent);
			        val response:HttpResponse = request.execute();
			        val content:String = response.getHeaders().toString();   
			        Logger.trace(content);
		            true;
	            }
	            catch {
	              case ex: Exception => {
	            	  						Logger.error("Error subir archivo a google",ex);
	            	  						false;
	            	  					}
	            }

	            
	            
	          });
	   
	   
	   return respThumbnail && respRestaurante;
  }

  
  def uploadUsuario(file:FilePart[TemporaryFile], fileName:String):Boolean= {


     
       val respUsuario:Boolean=privateKey.fold((ex)=>{Logger.error("Error al obtener credencial google",ex);  false;},
            (credential)=>{
              try{
                
               val inFile = file.ref.file;
                val outFile =  File.createTempFile("thl", ".tbd");

                 Image(inFile).cover(160, 240, ScaleMethod.FastScale).write(outFile);
                

                
                
                  //val fileContent:FileContent= new FileContent(file.contentType.get,file.ref.file);               
                   val fileContent:FileContent= new FileContent(file.contentType.get,outFile);  
                 
                 val URI:String = "https://"+BUCKET_NAME+".storage.googleapis.com/"+FOLDER_USUARIO +"/"+fileName ;
                  
                  
                  
              val requestFactory:HttpRequestFactory = httpTransport.createRequestFactory(credential);
              val url:GenericUrl = new GenericUrl(URI);
              val request:HttpRequest = requestFactory.buildPutRequest(url,fileContent);
              val response:HttpResponse = request.execute();
              val content:String = response.getHeaders().toString();   
              Logger.trace(content);
                true;
              }
              catch {
                case ex: Exception => {
                              Logger.error("Error subir archivo a google",ex);
                              false;
                            }
              }

              
              
            });
     
     
     return respUsuario;
  }

	  
	  
	def download( fileName:String):Either[Exception,(InputStream,String,Long)]= {
	   val resp:Either[Exception,(InputStream,String,Long)]=privateKey.fold((ex)=>{Logger.error("Error al obtener credencial google",ex);  Left(ex);},
	          
	       (credential)=>{
	            try{             
	                val URI:String = "https://"+BUCKET_NAME+".storage.googleapis.com/" +fileName;
			        val requestFactory:HttpRequestFactory = httpTransport.createRequestFactory(credential);
			        val url:GenericUrl = new GenericUrl(URI);
			        val request:HttpRequest = requestFactory.buildGetRequest(url);
			        val response:HttpResponse = request.execute();
			        
			        val lenArchivo:Long=response.getHeaders().getContentLength();
			        val inputStream:InputStream=response.getContent();
		          
			         Logger.info("lenArchivo="+lenArchivo) 
			        
			        Right( (inputStream,response.getContentType() ,lenArchivo)  );
	            }
	            catch {
	              case ex: Exception => {
	            	  						Logger.error("Error bajar archivo a google",ex);
	            	  						Left(ex);
	            	  					}
	            }
	          });
	  return resp;
	  
 
  }
	

	
		def downloadRestaurante( fileName:String):Either[Exception,(InputStream,String,Long)]= {
	   val resp:Either[Exception,(InputStream,String,Long)]=privateKey.fold((ex)=>{Logger.error("Error al obtener credencial google",ex);  Left(ex);},
	          
	       (credential)=>{
	            try{             
	                val URI:String = "https://"+BUCKET_NAME+".storage.googleapis.com/"+FOLDER_RESTAURANT+"/"  +fileName;
			        val requestFactory:HttpRequestFactory = httpTransport.createRequestFactory(credential);
			        val url:GenericUrl = new GenericUrl(URI);
			        val request:HttpRequest = requestFactory.buildGetRequest(url);
			        val response:HttpResponse = request.execute();
			        val inputStream:InputStream=response.getContent();
			         val lenArchivo:Long=response.getHeaders().getContentLength();
			        
		            Right((inputStream,response.getContentType(),lenArchivo));
	            }
	            catch {
	              case ex: Exception => {
	            	  						Logger.error("Error bajar archivo a google",ex);
	            	  						Left(ex);
	            	  					}
	            }
	          });
	  return resp;
	  
 
  }
    
   def downloadUsuario( fileName:String):Either[Exception,(InputStream,String,Long)]= {
     val resp:Either[Exception,(InputStream,String,Long)]=privateKey.fold((ex)=>{Logger.error("Error al obtener credencial google",ex);  Left(ex);},
            
         (credential)=>{
              try{             
                  val URI:String = "https://"+BUCKET_NAME+".storage.googleapis.com/"+FOLDER_USUARIO+"/"  +fileName;
              val requestFactory:HttpRequestFactory = httpTransport.createRequestFactory(credential);
              val url:GenericUrl = new GenericUrl(URI);
              val request:HttpRequest = requestFactory.buildGetRequest(url);
              val response:HttpResponse = request.execute();
              val inputStream:InputStream=response.getContent();
               val lenArchivo:Long=response.getHeaders().getContentLength();
              
                Right((inputStream,response.getContentType(),lenArchivo));
              }
              catch {
                case ex: Exception => {
                              Logger.error("Error bajar archivo a google",ex);
                              Left(ex);
                            }
              }
            });
    return resp;
    
 
  }  
		
    
    
    def downloadPlato( fileName:String):Either[Exception,(InputStream,String,Long)]= {
     val resp:Either[Exception,(InputStream,String,Long)]=privateKey.fold((ex)=>{Logger.error("Error al obtener credencial google",ex);  Left(ex);},
            
         (credential)=>{
              try{             
                  val URI:String = "https://"+BUCKET_NAME+".storage.googleapis.com/"+FOLDER_PLATOS+"/"  +fileName;
              val requestFactory:HttpRequestFactory = httpTransport.createRequestFactory(credential);
              val url:GenericUrl = new GenericUrl(URI);
              val request:HttpRequest = requestFactory.buildGetRequest(url);
              val response:HttpResponse = request.execute();
              val inputStream:InputStream=response.getContent();
               val lenArchivo:Long=response.getHeaders().getContentLength();
              
                Right((inputStream,response.getContentType(),lenArchivo));
              }
              catch {
                case ex: Exception => {
                              Logger.error("Error bajar archivo a google",ex);
                              Left(ex);
                            }
              }
            });
    return resp;
    
 
  }  
    
    
  def downloadPlatoThumbnail( fileName:String):Either[Exception,(InputStream,String,Long)]= {
     val resp:Either[Exception,(InputStream,String,Long)]=privateKey.fold((ex)=>{Logger.error("Error al obtener credencial google",ex);  Left(ex);},
            
         (credential)=>{
              try{             
                  val URI:String = "https://"+BUCKET_NAME+".storage.googleapis.com/"+FOLDER_THUMBNAILS_PLATO+"/" +fileName;
                  val requestFactory:HttpRequestFactory = httpTransport.createRequestFactory(credential);
                  val url:GenericUrl = new GenericUrl(URI);
                  val request:HttpRequest = requestFactory.buildGetRequest(url);
                  val response:HttpResponse = request.execute();
                  val inputStream:InputStream=response.getContent();
                  val lenArchivo:Long=response.getHeaders().getContentLength();
                  Right((inputStream,response.getContentType(),lenArchivo));
              }
              catch {
                case ex: Exception => {
                              Logger.error("Error bajar archivo a google",ex);
                              Left(ex);
                            }
              }
            });
    return resp;
    
 
  }
		
	def downloadRestauranteThumbnail( fileName:String):Either[Exception,(InputStream,String,Long)]= {
	   val resp:Either[Exception,(InputStream,String,Long)]=privateKey.fold((ex)=>{Logger.error("Error al obtener credencial google",ex);  Left(ex);},
	          
	       (credential)=>{
	            try{             
	                val URI:String = "https://"+BUCKET_NAME+".storage.googleapis.com/"+FOLDER_THUMBNAILS+"/" +fileName;
			        val requestFactory:HttpRequestFactory = httpTransport.createRequestFactory(credential);
			        val url:GenericUrl = new GenericUrl(URI);
			        val request:HttpRequest = requestFactory.buildGetRequest(url);
			        val response:HttpResponse = request.execute();
			        val inputStream:InputStream=response.getContent();
			         val lenArchivo:Long=response.getHeaders().getContentLength();
		            Right((inputStream,response.getContentType(),lenArchivo));
	            }
	            catch {
	              case ex: Exception => {
	            	  						Logger.error("Error bajar archivo a google",ex);
	            	  						Left(ex);
	            	  					}
	            }
	          });
	  return resp;
	  
 
  }
	
	
	

}
