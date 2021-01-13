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
    String ndtrcItemInstance = '''<trcxml xsi:schemaLocation="http://api.staging.ndtrc.nl/TrcXml/2.1/XSD.xsd"
    xmlns="http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <nofrecords>1</nofrecords>
    <trcitems>
        <trcitem
            externalid="rotterdam_marketing_formulier_ithelden_c93b8297c5555cdb643355b93df3909f"
            owner="Rotterdam Marketing Invoerders" wfstatus="draft" published="false"
            deleted="false" offline="true" entitytype="EVENEMENT">
            <calendar excludeholidays="false" onrequest="false">
                <single date="20/05/2017">
                    <when timestart="11:00:00" timeend="13:00:00"/>
                </single>
            </calendar>
            <contactinfo>
                <mail value="jnijstad@curzusenzo.nl" descriptioncode="CDC-004"/>
                <phone value="010 2518988" descriptioncode="CDC-004"/>
                <url
                    value="https://www.curzusenzo.nl/cursussen/ik-zoek-een-cursus-voor-mijzelf/kunst-creativiteit/kunstlezing"
                    targetLanguage="nl" urlServiceType="booking"/>
                <url value="http://www.curzusenzo.nl" targetLanguage="nl" urlServiceType="general"/>
                <url value="http://www.facebook.com/curzusenzo" targetLanguage="nl"
                    urlServiceType="socialmedia"/>

            </contactinfo>
            <trcitemcategories>
                <types>
                    <type catid="2.3.9"/>

                </types>
            </trcitemcategories>
            <media>
                <file main="true">
                    <filename>nachtwacht.jpg</filename>
                    <filetype>jpg</filetype>
                    <hlink>https://asset.uitagendarotterdam.nl/files/98DAA777/nachtwacht.jpg</hlink>
                    <mediatype>photo</mediatype>
                    <title>
                        <titletranslation lang="nl"
                            label="https://asset.uitagendarotterdam.nl/files/98DAA777/nachtwacht.jpg"
                        />
                    </title>
                </file>
            </media>
            <trcitemdetails>
                <trcitemdetail lang="nl">
                    <longdescription>In de zeventiende eeuw was Nederland één van de machtigste en
                        rijkste landen van Europa. Niet alleen de handel in goederen, maar ook de
                        handel in kunst was destijds enorm. Er zijn waarschijnlijk miljoenen
                        schilderijen gemaakt in die tijd! Daardoor was de prijs van kunst laag en
                        had bijna iedereen thuis wel wat schilderijtjes aan de muur hangen. Beroemde
                        kunstenaars uit deze Gouden Eeuw als Rembrandt en Frans Hals zijn nog altijd
                        bekend over de hele wereld. &lt;br&gt;&lt;br&gt;Kunsthistorica Drs. Patricia
                        Huisman belicht belangrijke invalshoeken van de kunstenaar in relatie tot
                        zijn kunst. Aan de hand van een beeldpresentatie word je meegenomen in de
                        tijd, de stroming, en het persoonlijke leven van de
                        kunstenaars.&lt;br&gt;&lt;br&gt;Meer weten of inschrijven? Kijk op
                        www.curzusenzo.nl of bel met 010 - 251 89 88.</longdescription>
                    <shortdescription>Beroemde kunstenaars uit deze Gouden Eeuw als Rembrandt en
                        Frans Hals zijn nog altijd bekend over de</shortdescription>
                    <title>Kunstlezing over de Gouden Eeuw</title>
                </trcitemdetail>


            </trcitemdetails>
            <keywords>covid_keyword</keywords>
            <markers>covid_marker</markers>
            <location>
                <address>
                    <physical giscoordinateType="points">
                        <city>ROTTERDAM</city>
                        <country>NL</country>
                        <housenr>15</housenr>
                        <street>Remmet van Milplaats</street>
                        <zipcode>3067 AN</zipcode>
                    </physical>
                </address>
                <label>Cursuscentrum CurZus&amp;Zo</label>
                <locationitem trcid="d1d0e1c1-cb51-4863-a408-8dd235925590">Cursuscentrum
                    CurZus&amp;Zo</locationitem>
            </location>
            <userorganisation>Rotterdam Marketing</userorganisation>
            <priceinformation>
                <priceelement>
                    <pricevalues>
                        <pricefrom>17.5</pricefrom>
                    </pricevalues>
                </priceelement>
                <extrapriceinformations>
                    <extrapriceinformation lang="lang349">extrapriceinformation0</extrapriceinformation>
                    <extrapriceinformation lang="lang351">extrapriceinformation1</extrapriceinformation>
                </extrapriceinformations>
            </priceinformation>
        </trcitem>
    </trcitems>
</trcxml>'''

    @Test
    void testTRCItemRoot() {
        TRCItem trcItem = NDTRCParser.parseTRCItem(DocumentHelper.parseText(ndtrcItemInstance).getRootElement().element("trcitems").element("trcitem"))

        Element element = NDTRCSerializer.serializeTRCItem(trcItem)

        String original = prettyPrint(DocumentHelper.parseText(ndtrcItemInstance).getRootElement().element("trcitems").element("trcitem").asXML())
        String serialized = prettyPrint(element).replaceAll(" cancelled=\"false\" soldout=\"false\"", "")

        Assert.assertEquals(original.replaceAll("\\s+",""), serialized.replaceAll("\\s+",""))
    }


    @Test
    void testRelations() {
        String source = this.getClass().getResourceAsStream("/testRelationsItem.xml").getText("UTF-8")

        TRCItem trcItem = NDTRCParser.parseTRCItem(DocumentHelper.parseText(source).getRootElement().element("trcitems").element("trcitem"))

        Element element = NDTRCSerializer.serializeTRCItem(trcItem)

        String original = prettyPrint(DocumentHelper.parseText(source).getRootElement().element("trcitems").element("trcitem").asXML())
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