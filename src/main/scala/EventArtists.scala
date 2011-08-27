import java.net.{URLEncoder, URL}
import collection.mutable.Map


object EventArtists extends LastFMRequest {
  val baseURL = "http://ws.audioscrobbler.com/2.0/?" +
    "method=event.getInfo&" +
    "api_key=" + key + "&" +
    "event="

  def artistsForEvent(eventId: Int) = baseURL + eventId


  import scala.xml._;
  import java.net._;

  var cache: Map[Int, Seq[String]] = Map()


  def fetchArtistsForEvent(eventId: Int): Seq[String] =
    cache.getOrElseUpdate(eventId, {
      val (success, body) = Http request artistsForEvent(eventId) /* 1 */
      if (!success) {
        println("Event " + eventId + ": error")
        List()
      } else {
        val xml = XML.load(body) /* 2 */

        if (!(xml \\ "error code" isEmpty)) {
          println("Event " + eventId + ": " + (xml \\ "error code" text))
          List()
        } else
          for (event <- xml \\ "event";
               artists <- event \\ "artists";
                 artist <- artists \\ "artist"
          ) yield artist text
      }
    })

}