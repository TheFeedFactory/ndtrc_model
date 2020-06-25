package nl.ithelden

import nl.ithelden.model.ndtrc.ExtraPriceInformation
import nl.ithelden.model.ndtrc.TRCItem
import nl.ithelden.services.NDTRCParser
import nl.ithelden.services.NDTRCSerializer
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.XMLWriter
import org.junit.Assert
import org.junit.Test

class NDTRCParserTest {

    String ndtrcItemAll = this.getClass().getResourceAsStream("/all.xml").getText("UTF-8")
    String ndtrcItemInstance = this.getClass().getResourceAsStream("/test_sample1.xml").getText("UTF-8")

    @Test
    void testTRCItemRoot() {
        TRCItem trcItem = NDTRCParser.parseTRCItem(DocumentHelper.parseText(ndtrcItemInstance).getRootElement().element("trcitems").element("trcitem"))

        Element element = NDTRCSerializer.serializeTRCItem(trcItem)

        String original = prettyPrint(DocumentHelper.parseText(ndtrcItemInstance).getRootElement().element("trcitems").element("trcitem").asXML())
        String serialized = prettyPrint(element).replaceAll(" cancelled=\"false\" soldout=\"false\"", "")

        Assert.assertEquals(original.replaceAll("\\s+",""), serialized.replaceAll("\\s+",""))
    }


    @Test
    void testExtraPriceInformations() {
        TRCItem trcItem = new TRCItem(
            extrapriceinformations:[
                new ExtraPriceInformation(lang: 'lang349', text: 'extrapriceinformation0')
            ]
        )

        Element element = NDTRCSerializer.serializeTRCItem(trcItem)

        Assert.assertEquals('<trcitem xmlns="http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0"><priceinformation><extrapriceinformations><extrapriceinformation lang="lang349">extrapriceinformation0</extrapriceinformation></extrapriceinformations></priceinformation></trcitem>',
                element.asXML())
    }

    static prettyPrint(String document) {
        return prettyPrint(DocumentHelper.parseText(document).rootElement)
    }

    static prettyPrint(Element element) {
        Writer serialized = new StringWriter()
        OutputFormat format = OutputFormat.createPrettyPrint()
        XMLWriter writer = new XMLWriter( serialized, format )
        writer.write( element )
        writer.close()

        return  serialized.toString()
    }
}