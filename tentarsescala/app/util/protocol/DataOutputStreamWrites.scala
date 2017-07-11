package util.protocol
  import java.io.{DataOutputStream, ByteArrayOutputStream}

  class DataOutputStreamWrites[T](writeValue: (DataOutputStream, T) => Unit) extends Writes[T] {

    def writes(value: T): Array[Byte] = {
      val bos = new ByteArrayOutputStream
      val dos = new DataOutputStream(bos)
      writeValue(dos, value)
      dos.flush()
      val byteArray = bos.toByteArray
      bos.close()
      byteArray
    }
  }