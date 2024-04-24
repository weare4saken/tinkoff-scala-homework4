import scala.compiletime.{erasedValue, summonInline}
import scala.deriving.*


trait JsonFormat

case object Compact extends JsonFormat

case object Pretty extends JsonFormat

trait AsJson[A] {
  def asJson(a: A, format: JsonFormat): String
}

object AsJsonInstances {
  given AsJson[Int] with {
    def asJson(a: Int, format: JsonFormat = Compact): String = a.toString
  }

  given AsJson[String] with {
    def asJson(a: String, format: JsonFormat = Compact): String = "\"" + a + "\""
  }

  given AsJson[Boolean] with {
    def asJson(a: Boolean, format: JsonFormat = Compact): String = a.toString
  }

  given [A](using aAsJson: AsJson[A]): AsJson[List[A]] with {
    def asJson(a: List[A], format: JsonFormat = Compact): String = a.map(aAsJson.asJson(_, format)).mkString("[", ",", "]")
  }

  given [T, R](using tAsJson: AsJson[T], rAsJson: AsJson[R]): AsJson[Map[T, R]] with {
    def asJson(a: Map[T, R], format: JsonFormat = Compact): String =
      a.map { case (t, r) => s"${tAsJson.asJson(t, format)}:${rAsJson.asJson(r, format)}" }.mkString("{", ",", "}")
  }

  given [A](using aAsJson: AsJson[A]): AsJson[Option[A]] with {
    def asJson(a: Option[A], format: JsonFormat = Compact): String = a match {
      case Some(value) => aAsJson.asJson(value, format)
      case None => "{}"
    }
  }
}

object AsJsonAuto {
  inline def summonAll[T <: Tuple]: List[AsJson[?]] = inline erasedValue[T] match {
    case _: EmptyTuple => Nil
    case _: (t *: ts) => summonInline[AsJson[t]] :: summonAll[ts]
  }

  inline given derived[T](using m: Mirror.Of[T]): AsJson[T] = new AsJson[T] {
    def asJson(a: T, format: JsonFormat = Compact): String = {
      val elems = summonAll[m.MirroredElemTypes]
      val product = a.asInstanceOf[Product]
      val keyValuePairs = elems.iterator.zip(product.productIterator).zip(product.productElementNames).map {
        case ((asJson, value), name) =>
          s""""$name":${asJson.asInstanceOf[AsJson[Any]].asJson(value, format)}"""
      }.mkString(",")
      s"{$keyValuePairs}"
    }
  }
}

case class User(name: String, age: Int, email: Option[String])

object User {

  import AsJsonInstances.{given AsJson[Int], given AsJson[String], given AsJson[Option[String]]}

  given AsJson[User] = AsJsonAuto.derived

  def asJson[A](a: A, format: JsonFormat)(using asJson: AsJson[A]): String = asJson.asJson(a, format)
}
