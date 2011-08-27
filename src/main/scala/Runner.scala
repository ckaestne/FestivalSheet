import java.io.File
import java.util.logging.{Level, Logger}
import org.jaudiotagger.audio._
import generic.{AudioFileWriter, AudioFileReader}
import mp4.Mp4TagReader
import org.jaudiotagger.audio.mp3._
import org.jaudiotagger.tag.datatype.AbstractDataType
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.id3.{AbstractID3Tag, AbstractTagItem}

object Runner {

    def main(args: Array[String]) {

        if (args.size <= 0)
            println("eventid required")
        else
            for (arg <- args)
                present(arg.toInt)
    }


    def present(eventId: Int) {
        try {
            println("<html><body>")
            for (artist <- EventArtists.fetchArtistsForEvent(eventId)) {
                println(ArtistInfo.renderArtistInfoHtml(artist))
            }
            println("</body></html>")
        } catch {
            case e: Exception => e.printStackTrace
        }
    }


}
