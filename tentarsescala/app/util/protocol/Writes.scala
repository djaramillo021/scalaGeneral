package util.protocol

  import annotation.implicitNotFound
  
 @implicitNotFound(msg = "Could not find a Writes for ${T}")
  trait Writes[T] {

    def writes(value: T): Array[Byte]
  }