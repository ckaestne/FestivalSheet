import java.net.{URLEncoder, URL}
import collection.mutable.Map
import xml.NodeSeq._


object ArtistInfo extends LastFMRequest {
    val baseURL = "http://ws.audioscrobbler.com/2.0/?" +
            "method=artist.getInfo&" +
            "api_key="+key+"&" +
            "artist="

    def topTagsFor(artist: String) = baseURL + (URLEncoder encode artist)


    import scala.xml._;
    import java.net._;



    def renderArtistInfoHtml(artist: String): String =
            if (artist == "") ""
            else {
                val (success, body) = Http request topTagsFor(artist) /* 1 */
                if (!success) {
                    println(artist + ": error")
                    ""
                } else {
                    val xml = XML.load(body) /* 2 */

                    if (!(xml \\ "error code" isEmpty)) {
                        println(artist + ": " + (xml \\ "error code" text))
                        ""
                    } else {
                      var result=""
                        val artist = {xml \ "artist"}.head

                        result=result+("<h2 style='clear:both'>" + (artist \ "name" text) + " <small>"+
                                getI(artist,"listeners")/1000 +"k listeners</small></h2>\n")

                        result=result+"<img style='float:left;margin-right:4px' src=\""+getImgUrl(artist)+"\" />\n"
                        result=result+"<div class='tags'>"+getTags(artist).take(8).mkString(", ")+"</div>\n"
                        result=result+"<div class='summary'>"+get(artist,"summary")+"</div>\n"
                      result
                    }
                }
            }

  def getImgUrl(artist:Node):String =
      {artist \ "image" find { (i:Node)=> (i \ "@size").text == "medium" } get}.text
    def getTags(artist:Node):Seq[String] =
        for (t<-artist \\ "tag") yield t \ "name" text

    def get(artist:Node, field:String):String =
        artist \\ field text
    def getI(artist:Node, field:String):Int =
        get(artist,field) match {
            case "" => -1
            case e=>e.toInt
        }

}