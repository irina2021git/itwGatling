package test

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import util.PersonJson

class PearsonTest extends Simulation {

  val httpProtocol = http
    .baseUrl(url = "http://164.90.242.141")
  val headers = Map("Content-Type" -> """application/json""")

  val scn = scenario("CreatePersons")
    // create single person 10 times
    .repeat(times = 10) {
      exec(http("CreateSinglePerson")
        .post("/persons")
        .headers(headers)
        .body(StringBody(PersonJson.pearsonJsonString)).asJson)
        .pause(10, 15)
    }

    //create bulk 10 persons - 10 times
    .repeat(times = 10) {
      exec(http("CreateBulkPersons")
        .post("/persons/bulk")
        .headers(headers)
        .body(StringBody(PersonJson.pearsonMJsonString)).asJson)
        .pause(15, 30)
    }

    //list first 5 pages of persons
    .exec(http("List50persons")
      .get("/persons/1")
      .check(jsonPath("$[*].name").findRandom(5).exists.saveAs("namesSearch")))
    .pause(5, 10)

    .exec(http("List50persons")
      .get("/persons/2"))
    .pause(5, 10)

    .exec(http("List50persons")
      .get("/persons/3"))
    .pause(5, 10)

    .exec(http("List50persons")
      .get("/persons/4"))
    .pause(5, 10)

    .exec(http("List50persons")
      .get("/persons/5"))
    .pause(5, 10)

    //search 5 times -  person by name
    .foreach("${namesSearch}", "nameSearch") {
      exec(http("SearchPersonByName")
        .get("/persons/name/${nameSearch}"))
        .pause(10, 15)
    }

  setUp(
    scn.inject(
      rampConcurrentUsers(5) to (200) during (DurationInteger(5).minutes),
      constantConcurrentUsers(200) during (DurationInteger(10).minutes))

  ).protocols(httpProtocol)

}
