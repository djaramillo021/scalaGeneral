package controllers

import play.api._
import play.api.mvc._
import play.api.libs.json._
import scala.collection.immutable.Seq

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
   //  Ok(views.html.registro.restaurante("Your new application is ready."))
//    Ok(
//        JsObject(
//            Seq("message"->JsString("Its works"))
//            )
//        
//      )
  }

}