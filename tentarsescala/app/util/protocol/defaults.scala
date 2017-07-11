package util.protocol
  import annotation.implicitNotFound

  object defaults {
    implicit object WritesString extends Writes[String] {
      def writes(value: String) = value.getBytes("UTF-8")
    }
    implicit object WritesLong extends DataOutputStreamWrites[Long](_.writeLong(_))
    implicit object WritesInt extends DataOutputStreamWrites[Int](_.writeInt(_))
    implicit object WritesShort extends DataOutputStreamWrites[Short](_.writeShort(_))
  }