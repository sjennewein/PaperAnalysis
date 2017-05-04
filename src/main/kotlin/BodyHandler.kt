import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler

/**
 * Created by stephan on 4/16/17.
 */
class BodyHandler : DefaultHandler() {
    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {

    }
    override fun characters(ch: CharArray?, start: Int, length: Int) {}

    override fun endElement(uri: String?, localName: String?, qName: String?) {}
}