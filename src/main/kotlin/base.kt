import XMLHandler.State.*
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.io.File
import javax.lang.model.type.NullType
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory

/**
 * Created by stephan on 4/15/17.
 */

fun main(args: Array<String>) {
    val input = File("/home/stephan/IdeaProjects/04700000/tagged/10.1016_s0378-7206%2898%2900067-6.xml")
    val xmlFact = SAXParserFactory.newInstance()
    val parser = xmlFact.newSAXParser()
    val handler = XMLHandler(parser)
    parser.parse(input, handler)
}

class XMLHandler(parser: SAXParser) : DefaultHandler() {
    val my_parser = parser

    enum class State {Title, Author, Department, Unknown, FileDesc,
        BiblStruct, Forename, Surname, Middlename, Affiliation,
        Institution, Settlement, Region, Country, ProfileDesc,
        Keywords, Term, Abstract, Body
    }

    var state_list = mutableListOf<State>()
    var skip = false

    override fun startElement(uri: String?, localName: String?, qName: String?, attributes: Attributes?) {
        if (state_list == listOf(Body)) {
            my_parser.
        } else {
            var state = mapping(qName, attributes)
            if (state != State.Unknown) {
                state_list.add(state)
            }
        }

    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        when (state_list) {
            listOf(FileDesc, Title) -> println("Title: " + java.lang.String(ch, start, length))
            listOf(FileDesc, BiblStruct, Author, Forename) -> println("First: " + java.lang.String(ch, start, length))
            listOf(FileDesc, BiblStruct, Author, Middlename) -> println("Middle: " + java.lang.String(ch, start, length))
            listOf(FileDesc, BiblStruct, Author, Surname) -> println("Last: " + java.lang.String(ch, start, length))
            listOf(FileDesc, BiblStruct, Author, Affiliation, Department) -> println("Department: " + java.lang.String(ch, start, length))
            listOf(FileDesc, BiblStruct, Author, Affiliation, Institution) -> println("Institution: " + java.lang.String(ch, start, length))
            listOf(FileDesc, BiblStruct, Author, Affiliation, Settlement) -> println("Settlement: " + java.lang.String(ch, start, length))
            listOf(FileDesc, BiblStruct, Author, Affiliation, Region) -> println("Region: " + java.lang.String(ch, start, length))
            listOf(FileDesc, BiblStruct, Author, Affiliation, Country) -> println("Country: " + java.lang.String(ch, start, length))
            listOf(ProfileDesc, Keywords, Term) -> println("Keyword: " + java.lang.String(ch, start, length))
            listOf(ProfileDesc, Abstract) -> {
                if (skip) {
                    println("Abstract: " + java.lang.String(ch, start, length))
                }
            }
            listOf(Body) -> println("Body: " + java.lang.String(ch, start, length))
        }
//        if(state_list.contains(State.Affiliation)){
//            println(state_list)null
//        }

    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {

        var state = mapping(qName, null)
        if (state_list.isEmpty())
            return

        if (state_list.last() == state) {
            val last = state_list.lastIndex
            state_list.removeAt(last)
        }
    }

    fun mapping(qName: String?, attributes: Attributes?): State {
        var state = State.Unknown
        when (qName?.toLowerCase()) {
            "body" -> state = State.Body
            "p" -> skip = (attributes != null)
            "abstract" -> {
                state = State.Abstract
                skip = false
            }
            "profiledesc" -> state = State.ProfileDesc
            "keywords" -> state = State.Keywords
            "term" -> state = State.Term
            "title" -> state = State.Title
            "filedesc" -> state = State.FileDesc
            "author" -> state = State.Author
            "biblstruct" -> state = State.BiblStruct
            "affiliation" -> state = State.Affiliation
            "settlement" -> state = State.Settlement
            "region" -> state = State.Region
            "country" -> state = State.Country
            "orgname" -> {
                if (attributes == null) {
                    when (state_list.last()) {
                        State.Department -> state = State.Department
                        State.Institution -> state = State.Institution
                    }
                }
                when (attributes?.getValue("type")?.toLowerCase()) {
                    "department" -> state = State.Department
                    "institution" -> state = State.Institution
                }
            }
            "forename" -> {
                if (attributes == null) {
                    when (state_list.last()) {
                        State.Middlename -> state = State.Middlename
                        State.Forename -> state = State.Forename
                    }
//                    return state
                }
                when (attributes?.getValue("type")?.toLowerCase()) {
                    "first" -> state = State.Forename
                    "middle" -> state = State.Middlename
                }
            }
            "surname" -> state = State.Surname
        }
        return state
    }

}