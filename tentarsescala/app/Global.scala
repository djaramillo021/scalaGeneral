
import play.api.mvc._
import play.filters.gzip.GzipFilter
import play.api.GlobalSettings
import play.Logger
import scala.concurrent.Future
import play.mvc.Http.Status
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.DateTime
import play.libs.Json
import util.UtilAes



object AuthorizedFilter {
  def apply(actionNames: String*) = new AuthorizedFilter(actionNames)
}

class AuthorizedFilter(actionNames: Seq[String]) extends Filter {

  def apply(next: (RequestHeader) => Future[Result])(request: RequestHeader): Future[Result] = {
     Logger.info("filtro 0") 
      Logger.info(s"${request.method} ${request.uri}" +
      s"and returned ${request.tags}")
   
      val vAutorizacion:Boolean=actionNames.foldLeft(false)(
            (x,vl)=>{
                if(!x && request.uri.contains(vl))
                {
                 true; 
                }
                else{
                  x
                }
            })
      
      if( vAutorizacion || authorizationRequired(request)) {
      /* do the auth stuff here */
      Logger.info("filtro 1") 
      next(request)
    }
    else //next(request)
      //Results.Status(Status.UNAUTHORIZED);
      // next(  Results.Status(Status.UNAUTHORIZED) )
    Future.successful(Results.Status(Status.UNAUTHORIZED));

  }

  private def authorizationRequired(request: RequestHeader):Boolean = {
    Logger.info("filtro 2") 
    
    val llave:Option[String]=request.headers.get("KEY");
    llave  match {
      case Some(x) =>{
                  val denc:Option[String]= UtilAes.decrypt(x);
                  denc match {
                    case Some(x) =>{
                      true;
                    } 
                    case None=> false;
                    
                  }
                  
      } 
      case None=> false
    }
    
   // Results.Status(Status.UNAUTHORIZED)
   // val actionInvoked: String = request.tags.getOrElse(play.api.Routes.ROUTE_ACTION_METHOD, "")
    //actionNames.contains(actionInvoked)
  }


}


object Global extends WithFilters(
                              AuthorizedFilter("/restful/foto/plato/"
                                              , "/restful/usuario/perfil/foto/"
                                              , "/restful/foto/thumbnail/plato/"),
                              new GzipFilter())
            with GlobalSettings {}
