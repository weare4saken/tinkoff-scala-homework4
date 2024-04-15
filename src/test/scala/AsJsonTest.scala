import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import AsJsonInstances.given

class AsJsonTest extends AnyFlatSpec with Matchers {
  "AsJson" should "correctly serialize a String" in {
    val result = asJson("Hello, World!", Compact)
    result should be("\"Hello, World!\"")
  }

  it should "correctly serialize an Int" in {
    val result = asJson(1991, Compact)
    result should be("1991")
  }

  it should "correctly serialize a Boolean" in {
    val result = asJson(false, Compact)
    result should be("false")
  }

  it should "correctly serialize a List of Ints" in {
    val result = asJson(List(1, 2, 3, 4, 5, 6, 7, 8, 9), Compact)
    result should be("[1,2,3,4,5,6,7,8,9]")
  }

  it should "correctly serialize a Map of String to Int" in {
    val result = asJson(Map("abc" -> List(1,2,3), "fed" -> List(6,5,4)), Compact)
    result should be("""{"abc":[1,2,3],"fed":[6,5,4]}""")
  }

  it should "correctly serialize a case class" in {
    val user = User("Alex", 27, Some("alex@mail.com"))
    val result = asJson(user, Compact)
    result should include(""""name":"Alex"""")
    result should include(""""age":27""")
    result should include(""""email":"alex@mail.com"""")
  }

  it should "return an empty string when input is null" in {
    val user: User = null
    val result = asJson(Option(user), Compact)
    result shouldBe "{}"
  }

  def asJson[A](a: A, format: JsonFormat)(using asJson: AsJson[A]): String = asJson.asJson(a, format)
}
