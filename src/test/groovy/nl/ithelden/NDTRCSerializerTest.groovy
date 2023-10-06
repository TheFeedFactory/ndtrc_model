package nl.ithelden


import nl.ithelden.model.ndtrc.*
import nl.ithelden.services.NDTRCParser
import nl.ithelden.services.NDTRCSerializer
import org.dom4j.DocumentHelper
import org.joda.time.DateTime
import org.junit.Assert
import org.junit.Test

class NDTRCSerializerTest {
    @Test
    public void testTRCItemRoot() {
        String source = '''<trcitem xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://api.ndtrc.nl/TrcXml/2.1/XSD.xsd" availablefrom="2014-02-19T00:00:00" trcid="0b6403aa-b54d-4c50-8c23-840b7865d70b" createdby="import_Leisure_Port@ndtrc.nl" creationdate="2014-12-06T04:07:03" externalid="LP_Webs_508" lastupdated="2017-07-02T04:02:46" lastupdatedby="taskexecutor@ndtrc.nl" owner="Leisure Port Invoerders" validator="Leisure Port Validatoren" wfstatus="approved" published="true" legalowner="info@liefdevoorlimburg.nl" lastimportedon="2017-07-02T04:02:45.037" deleted="false" offline="false" entitytype="LOCATIE" xmlns="http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0"></trcitem>'''

        /**
         <trcitem xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://api.ndtrc.nl/TrcXml/2.1/XSD.xsd" availablefrom="2014-02-19T00:00:00" trcid="0b6403aa-b54d-4c50-8c23-840b7865d70b" createdby="import_Leisure_Port@ndtrc.nl" creationdate="2014-12-06T04:07:03" externalid="LP_Webs_508" lastupdated="2017-07-02T04:02:46" lastupdatedby="taskexecutor@ndtrc.nl" owner="Leisure Port Invoerders" validator="Leisure Port Validatoren" wfstatus="approved" published="true" legalowner="info@liefdevoorlimburg.nl" lastimportedon="2017-07-02T04:02:45.037" deleted="false" offline="false" entitytype="LOCATIE" xmlns="http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0">

         </trcitem>
         */

        TRCItem trcItem = NDTRCParser.parseTRCItem(DocumentHelper.parseText(source).rootElement)

        Assert.assertEquals('<trcitem xmlns="http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0" availablefrom="2014-02-19T00:00:00" trcid="0b6403aa-b54d-4c50-8c23-840b7865d70b" createdby="import_Leisure_Port@ndtrc.nl" creationdate="2014-12-06T04:07:03" externalid="LP_Webs_508" lastupdated="2017-07-02T04:02:46" lastupdatedby="taskexecutor@ndtrc.nl" owner="Leisure Port Invoerders" validator="Leisure Port Validatoren" wfstatus="approved" published="true" lastimportedon="2017-07-02T04:02:45" legalowner="info@liefdevoorlimburg.nl" deleted="false" offline="false" entitytype="LOCATIE"/>', NDTRCSerializer.serializeTRCItem(trcItem).asXML())
    }

    @Test
    public void testCalendarSingleDate() {
        /**
         <calendar excludeholidays="false" onrequest="false">
             <single date="20/05/2017">
                <when timestart="11:00:00" timeend="13:00:00"/>
             </single>
         </calendar>
         */
        Calendar calendar = new Calendar(excludeholidays: false, onrequest: false)
        calendar.singleDates.add(new Calendar.SingleDate(
            date: new DateTime("2017-05-20"),
            when: [new Calendar.When(timestart: "11:00:00", timeend: '13:00:00')]
        ))

        Assert.assertEquals('<calendar xmlns="http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0" excludeholidays="false" cancelled="false" soldout="false" onrequest="false"><single date="20/05/2017"><when timestart="11:00:00" timeend="13:00:00"/></single></calendar>', NDTRCSerializer.serializeCalendar(calendar).asXML())
    }

    @Test
    public void testFile() {
        /**
         <calendar excludeholidays="false" onrequest="false">
         <single date="20/05/2017">
         <when timestart="11:00:00" timeend="13:00:00"/>
         </single>
         </calendar>
     */

        File file = new File(trcid: "7c549a73-03ff-44c7-885c-511359ee306d", main: true, filename: "addc3366-eb83-4fe3-ae47-5289cbde6dfe.jpg", filetype: File.FileType.jpg, hlink: "https://app.thefeedfactory.nl/api/assets/5ff8a0d6de7e8633a4ab7979/addc3366-eb83-4fe3-ae47-5289cbde6dfe.jpg", mediatype: File.MediaType.photo, title: new File.Title(label: "Restaurant Zeezout Rotterdam", titleTranslations: [new File.Title.TitleTranslation(lang: "nl", label: "Restaurant Zeezout Rotterdam")]))

        Assert.assertEquals('<file xmlns="http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0" trcid="7c549a73-03ff-44c7-885c-511359ee306d" main="true"><filename>addc3366-eb83-4fe3-ae47-5289cbde6dfe.jpg</filename><filetype>jpg</filetype><hlink>https://app.thefeedfactory.nl/api/assets/5ff8a0d6de7e8633a4ab7979/addc3366-eb83-4fe3-ae47-5289cbde6dfe.jpg</hlink><mediatype>photo</mediatype><title label="Restaurant Zeezout Rotterdam"><titletranslation lang="nl" label="Restaurant Zeezout Rotterdam"/></title></file>', NDTRCSerializer.serializeFile(file).asXML())
    }


    @Test
    public void testCalendarPatternDate() {
        /**
         <calendar excludeholidays="false" onrequest="false">
            <pattern recurrence="1" enddate="01/07/2017" startdate="12/05/2017" type="weekly">
                <open day="6"/>
                <open day="7"/>
            </pattern>
         </calendar>
         */
        Calendar calendar = new Calendar(excludeholidays: false, onrequest: false)
        calendar.patternDates.add(new Calendar.PatternDate(
                recurrence: 1,
                enddate: new DateTime('2017-07-01'),
                startdate: new DateTime('2017-05-12'),
                recurrencyType: Calendar.PatternDate.RecurrencyType.weekly,
                opens: [
                        new Calendar.PatternDate.Open(day: 6),
                        new Calendar.PatternDate.Open(day: 7)
                ]
        ))

        Assert.assertEquals('<calendar xmlns="http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0" excludeholidays="false" cancelled="false" soldout="false" onrequest="false"><pattern recurrence="1" type="weekly" startdate="12/05/2017" enddate="01/07/2017"><open day="6"/><open day="7"/></pattern></calendar>', NDTRCSerializer.serializeCalendar(calendar).asXML())
    }

    @Test
    void testContactInfo() {
        /**
         <contactinfo>
             <mail value="giuseppe@revolt.nl" descriptioncode="CDC-004"/>
             <phone value="0655059554" descriptioncode="CDC-004"/>
             <url value="http://www.annabel.nu" targetLanguage="nl" urlServiceType="general"/>
             <url value="https://www.facebook.com/events/1669237990049173/" targetLanguage="nl" urlServiceType="socialmedia"/>
         </contactinfo>
         */
        Contactinfo contactinfo = new Contactinfo(
                mails: [new Contactinfo.Mail(email: 'giuseppe@revolt.nl', descriptioncode: 'CDC-004')],
                phones: [new Contactinfo.Phone(number: '0655059554', descriptioncode: 'CDC-004')],
                urls: [
                    new Contactinfo.Url(
                        url: new URL('http://www.annabel.nu'),
                        targetLanguage: 'nl',
                        urlServiceType: Contactinfo.Url.URLServiceType.general
                    ),
                    new Contactinfo.Url(
                            url: new URL('https://www.facebook.com/events/1669237990049173/'),
                            targetLanguage: 'nl',
                            urlServiceType: Contactinfo.Url.URLServiceType.socialmedia
                    )
                ]
        )

        Assert.assertEquals('<contactinfo xmlns="http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0">' +
                '<mail value="giuseppe@revolt.nl" descriptioncode="CDC-004"/>' +
                    '<phone value="0655059554" descriptioncode="CDC-004"/>' +
                    '<url value="http://www.annabel.nu" targetLanguage="nl" urlServiceType="general"/>' +
                    '<url value="https://www.facebook.com/events/1669237990049173/" targetLanguage="nl" urlServiceType="socialmedia"/>' +
                '</contactinfo>', NDTRCSerializer.serializeContactinfo(contactinfo).asXML())
    }

    @Test
    void testGISCoordinate() {
        GISCoordinate gisCoordinate = new GISCoordinate(
            xcoordinate: '52.2524706',
            ycoordinate: '6.1521041',
            label: 'Thuis'
        )

        Assert.assertEquals('<giscoordinate xmlns="http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0">' +
                '<xcoordinate>52.2524706</xcoordinate>' +
                '<ycoordinate>6.1521041</ycoordinate>' +
                '<label>Thuis</label>' +
                '</giscoordinate>', NDTRCSerializer.serializeGISCoordinate(gisCoordinate).asXML())

    }

    @Test
    void testExtraPriceInformations() {
        List<ExtraPriceInformation> extraPriceInformations = [
                new ExtraPriceInformation(lang: 'lang349', text: 'extrapriceinformation0')
        ]

        Assert.assertEquals('<extrapriceinformation xmlns="http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0" lang="lang349">extrapriceinformation0</extrapriceinformation>', NDTRCSerializer.serializeExtraPriceInformation(extraPriceInformations[0]).asXML())

    }
    
    @Test
    void testTRCItemCategoriesTypes() {
        /**
         <trcitemcategories>
             <types>
                 <type catid="2.3.9"/>
             </types>
         </trcitemcategories>
         */

        TRCItemCategories trcItemCategories = new TRCItemCategories(
                types: [
                   new TRCItemCategories.Type(catid: "2.3.9")
                ],
                categories: []
        )

        Assert.assertEquals('<trcitemcategories xmlns="http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0">' +
                '<types>' +
                '<type catid="2.3.9"/>' +
                '</types>' +
                '<categories/>' +
                '</trcitemcategories>', NDTRCSerializer.serializeTRCItemCategories(trcItemCategories).asXML())
    }

    @Test
    void testTRCItemCategories() {
        /**
         <trcitemcategories>
             <categories>
                 <category catid="48.3">
                     <categoryvalues>
                         <categoryvalue catid="48.3.2"></categoryvalue>
                     </categoryvalues>
                 </category>
             </categories>
         </trcitemcategories>
         */

        TRCItemCategories trcItemCategories = new TRCItemCategories(
                types: [],
                categories: [
                        new TRCItemCategories.Category(
                                catid: "2.3.9",
                                categoryvalues: [
                                        new TRCItemCategories.CategoryValue(catid: "2.3.9")
                                ])
                ]
        )

        Assert.assertEquals('<trcitemcategories xmlns="http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0"><types/><categories><category catid="2.3.9"><categoryvalues><categoryvalue catid="2.3.9"/></categoryvalues></category></categories></trcitemcategories>', NDTRCSerializer.serializeTRCItemCategories(trcItemCategories).asXML())
    }
}