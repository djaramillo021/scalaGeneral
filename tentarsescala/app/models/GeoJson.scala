package models

//{ loc : { type : "Point" ,
//          coordinates : [ 40, 5 ]
//} }

case class GeoJson(`type`:String="Point"
    , coordinates:List[Double]
)
{
  
}