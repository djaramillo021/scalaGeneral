package util.crypto

 

  import javax.crypto.spec.SecretKeySpec
  import javax.crypto.Cipher
  
  class JavaCryptoEncryption(algorithmName: String) extends Encryption  {

    def encrypt(bytes: Array[Byte], secret: String): Either[Exception,Array[Byte]] = {
      try {
       
      val secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), algorithmName)
      val encipher = Cipher.getInstance(algorithmName + "/ECB/PKCS5Padding")
      encipher.init(Cipher.ENCRYPT_MODE, secretKey);
      val ar= encipher.doFinal(bytes)
       Right(ar)
      } catch {
		  case e: Exception => Left(e);
		}
    }

    def decrypt(bytes: Array[Byte], secret: String): Either[Exception,Array[Byte]] = {
      try {
  

      val secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), algorithmName)
      val encipher = Cipher.getInstance(algorithmName + "/ECB/PKCS5Padding")
      encipher.init(Cipher.DECRYPT_MODE, secretKey)
      val des=encipher.doFinal(bytes)
       Right(des)
      }
      catch {
		 case e: Exception => Left(e);
		}
    }
  }

