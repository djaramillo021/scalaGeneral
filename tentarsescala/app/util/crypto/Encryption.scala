package util.crypto
 import util.protocol.Writes

  trait Encryption {
    def encrypt(dataBytes: Array[Byte], secret: String):Either[Exception,Array[Byte]]
    def decrypt(codeBytes: Array[Byte], secret: String): Either[Exception,Array[Byte]]

    def encrypt[T:Writes](data: T, secret: String): Either[Exception,Array[Byte]]= encrypt(implicitly[Writes[T]].writes(data), secret)
    
    //def dencrypt[T:Writes](data: T, secret: String): Array[Byte] = decrypt(implicitly[Writes[T]].writes(data), secret)
    
  }