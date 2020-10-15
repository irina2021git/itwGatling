package util

import com.google.gson.Gson
import io.alphash.faker.Person


case class PersonClass(name: String, age: Int, created: Long)

object PersonJson {
  val pcs: Array[PersonClass] = new Array[PersonClass](10)
  for (i <- 0 to 9)
    pcs.update(i, PersonClass(Person().firstNameFemale, scala.util.Random.nextInt(100), 1234567892))

  val gson1 = new Gson
  val pearsonMJsonString = gson1.toJson(pcs)

  val p = PersonClass(Person().firstNameFemale, scala.util.Random.nextInt(100), 1234567891)
  val gson2 = new Gson
  val pearsonJsonString = gson2.toJson(p)


}
