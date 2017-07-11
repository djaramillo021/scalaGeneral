package models

//{ loc : { type : "Point" ,
//          coordinates : [ 40, 5 ]
//} }

case class Loc(longitude: Double, latitude:Double,longitudeDelta:Double=0,latitudeDelta:Double=0,direccion:String=null,direccionCorta:String=null)
//Loc(`type`:String=new String("Point"), coordinates:Array[Double])
{
  
}