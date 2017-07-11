package util

import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec;
import com.google.api.client.util.Base64

object UtilAes {
  
    val algorithm:String  = "AES";
    val keyValue:Array[Byte] ="qddhueolshmba+e&".getBytes();


    // Performs Encryption
    def  encrypt( plainText:String):Option[String] ={ 
    
        val key:Option[ Key] = generateKey();
        key  match {
          case Some(x) => {
                val chiper:Cipher = Cipher.getInstance(algorithm);
                chiper.init(Cipher.ENCRYPT_MODE, x);
                val encVal:Array[Byte] = chiper.doFinal(plainText.getBytes());
                val encryptedValue:String =  Base64.encodeBase64String(encVal);
                Some( encryptedValue);
                      
          }
          case None=> None;  
        }

      
    }

    // Performs decryption
    def decrypt( encryptedText:String):Option[String]={
     
        // generate key
        val key:Option[Key] = generateKey();
   
        key  match {
          case Some(x) => {
              val chiper:Cipher = Cipher.getInstance(algorithm);
              chiper.init(Cipher.DECRYPT_MODE, x);
              val decordedValue:Array[Byte] = Base64.decodeBase64(encryptedText);
              val decValue:Array[Byte] = chiper.doFinal(decordedValue);
              val decryptedValue:String = new String(decValue);
              Some( decryptedValue);
          }
          case None=> None;  
        }
    }

    //generateKey() is used to generate a secret key for AES algorithm
    def  generateKey():Option[Key]={
      
      try{
        val key:Key = new SecretKeySpec(keyValue, algorithm);
        return Some(key);
      }
     catch{
            case ex: Exception => return  None;//(ex);
          
          }
        
    }

}