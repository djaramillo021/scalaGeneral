package util.google


import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import java.net.URLEncoder
import com.squareup.okhttp.Response
import java.io.IOException
import org.json.simple.JSONObject
import org.json.simple.JSONValue
import org.json.simple.JSONArray
import org.json.simple.parser.ParseException


object GoogleMaps {
  val  SERV_GPS:String="http://maps.googleapis.com/maps/api/geocode/json";

  
  def  getDireccion( lon:Double, lat:Double ):String={
    val charset:String = "UTF-8";
  
    val query:String = String.format("latlng=%s,%s&sensor=false",
          URLEncoder.encode(lat.toString(), charset),
          URLEncoder.encode(lon.toString(), charset));
    val urlString:String= GoogleMaps.SERV_GPS + "?" + query;
    val client:OkHttpClient = new OkHttpClient();
    val request:Request = new Request.Builder()
        .url(urlString)
        .build();


        try {
            val responseHttp:Response = client.newCall(request).execute();
            val  response:Int = responseHttp.code();
            if(response==200)
            {
              val contentAsString:String =responseHttp.body().string();
              
              val obj:Object=JSONValue.parse(contentAsString);
              //JSONArray array=(JSONArray)obj;
              val resp:JSONObject = obj.asInstanceOf[JSONObject] ;
              if(resp.containsKey("results"))
              {
                val array:JSONArray= resp.get("results").asInstanceOf[JSONArray];
                return (array.get(0).asInstanceOf[JSONObject]).get("formatted_address").asInstanceOf[String];
              }
              
            
            }
            return null
        } catch{
            case ex: IOException =>return null;
            case ex: ParseException =>return null;
 
          
          }

  }

}