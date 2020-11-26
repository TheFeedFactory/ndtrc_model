package nl.ithelden

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.joda.JodaMapper
import nl.ithelden.model.ConvertedEntry
import nl.ithelden.model.ndtrc.Calendar
import nl.ithelden.model.ndtrc.Contactinfo
import nl.ithelden.model.ndtrc.ExtraPriceInformation
import nl.ithelden.model.ndtrc.GISCoordinate
import nl.ithelden.model.ndtrc.TRCItem
import nl.ithelden.model.ndtrc.TRCItemCategories
import nl.ithelden.services.NDTRCSerializer
import nl.ithelden.services.NDTRCValidator
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.junit.Assert
import org.junit.Test

class NDTRCSerializerTest {

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

        Assert.assertEquals('<trcitemcategories xmlns="http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0"><categories><category catid="2.3.9"><categoryvalues><categoryvalue catid="2.3.9"/></categoryvalues></category></categories></trcitemcategories>', NDTRCSerializer.serializeTRCItemCategories(trcItemCategories).asXML())
    }


    @Test
    void testErrorInProduction() {
        String source = '''{
  "label": "Ruik de herfst in Kardinge (Kardinge, parkeerplaats voor de kerk in Noorddijk)",
  "created": "2018-09-13T09:46:50.463Z",
  "modified": "2018-09-13T09:46:50.463Z",
  "externalId": "ff_natuurmonumenten_v2_70858",
  "trcItem": {
    "trcid": null,
    "creationdate": "2018-09-11T13:15:03.684Z",
    "availablefrom": null,
    "availableto": null,
    "lastupdated": null,
    "lastimportedon": null,
    "createdby": null,
    "lastupdatedby": null,
    "owner": "vvv",
    "legalowner": null,
    "externalid": "ff_natuurmonumenten_v2_70858",
    "validator": null,
    "wfstatus": "readyforvalidation",
    "cidn": null,
    "published": false,
    "deleted": false,
    "validatedby": null,
    "offline": false,
    "isprivate": null,
    "entitytype": "EVENEMENT",
    "productiontrcid": null,
    "calendar": {
      "singleDates": [
        {
          "date": "2019-06-16T22:00:00.000Z",
          "when": [
            {
              "timestart": "20:15",
              "timeend": "22:00",
              "status": null,
              "statustranslations": [],
              "extrainformations": []
            }
          ]
        }
      ],
      "patternDates": [],
      "opens": [],
      "closeds": [],
      "soldouts": [],
      "cancelleds": [],
      "excludeholidays": false,
      "cancelled": false,
      "soldout": false,
      "onrequest": null,
      "alwaysopen": null,
      "comment": null
    },
    "contactinfo": {
      "mail": null,
      "phone": null,
      "fax": null,
      "urls": [
        {
          "url": "http://www.dekleinekomedie.nl/voorstellingen/1044/Een_bang_jongetje_dat_hele_enge_dingen_doet_premiere_/KEES_VAN_AMSTEL/",
          "descriptioncode": null,
          "targetLanguage": "nl",
          "reservations": null,
          "urlServiceType": "general",
          "descriptionTranslations": []
        },
        {
          "url": "http://www.dekleinekomedie.nl/voorstellingen/1044/Een_bang_jongetje_dat_hele_enge_dingen_doet_premiere_/KEES_VAN_AMSTEL/",
          "descriptioncode": null,
          "targetLanguage": "en",
          "reservations": null,
          "urlServiceType": "general",
          "descriptionTranslations": []
        },
        {
          "url": "http://www.dekleinekomedie.nl/voorstellingen/1044/Een_bang_jongetje_dat_hele_enge_dingen_doet_premiere_/KEES_VAN_AMSTEL/",
          "descriptioncode": null,
          "targetLanguage": "nl",
          "reservations": null,
          "urlServiceType": "booking",
          "descriptionTranslations": []
        }
      ],
      "address": null
    },
    "trcItemCategories": {
      "types": [
        {
          "catid": "2.9.1"
        }
      ],
      "categories": [],
      "soldout": null,
      "canceled": null
    },
    "files": [
      {
        "trcid": null,
        "main": null,
        "copyright": null,
        "filename": "img2760_151.jpg",
        "hlink": "http://www.dekleinekomedie.nl/cms_files/system/images/img2760_151.jpg",
        "filetype": "jpg",
        "mediatype": null,
        "targetLanguage": null,
        "title": null
      },
      {
        "trcid": null,
        "main": null,
        "copyright": null,
        "filename": "img2760_153.jpg",
        "hlink": "http://www.dekleinekomedie.nl/cms_files/system/images/img2760_153.jpg",
        "filetype": "jpg",
        "mediatype": null,
        "targetLanguage": null,
        "title": null
      },
      {
        "trcid": null,
        "main": null,
        "copyright": null,
        "filename": "img2760_155.jpg",
        "hlink": "http://www.dekleinekomedie.nl/cms_files/system/images/img2760_155.jpg",
        "filetype": "jpg",
        "mediatype": null,
        "targetLanguage": null,
        "title": null
      }
    ],
    "trcItemDetails": [
      {
        "lang": "nl",
        "longdescription": "Het leven is net een zwembad. Vindt hij. En hij zwemt niet. Kees van Amstel staat maar een beetje bang langs de kant. Toe te kijken. Hoe anderen het hoofd boven water proberen te houden. Althans, dat zegt zijn therapeut. Dan kun je twee dingen doen. Langs de kant blijven staan. Of vol het leven in duiken. Kees koos optie drie. Hij zocht een nieuwe therapeut. En hij maakte een nieuw cabaretprogramma. Met verhalen. En grappen. Heel veel grappen. Kees van Amstel is Een bang jongetje dat hele enge dingen doet. Die comedian is bij de Comedytrain, druktemaker op NPO Radio 1 en een van de breinen achter Dit Was Het Nieuws. Hij ‘vertelt met sjeu’ (de Volkskrant) en ‘laat zijn verhalen glanzen.’ (NRC) Zwemmen wil hij niet, maar zwammen kan hij als de beste. I rest my Kees. Fotograaf:&nbsp;Simone Peerdeman",
        "shortdescription": "Zwemmen wil hij niet, maar zwammen kan hij als de beste. I rest my Kees.",
        "title": "Een bang jongetje dat hele enge dingen doet (première)"
      }
    ],
    "keywords": "performingarts;comedy;comedyevents",
    "markers": "toonopiamsterdamsite",
    "location": {
      "address": {
        "main": null,
        "reservation": null,
        "title": "Leuk weggetje",
        "city": null,
        "citytrcid": null,
        "country": "NL",
        "housenr": "14-16",
        "street": "Noorddijkerweg 14-16",
        "streettrcid": null,
        "zipcode": "9734 AT",
        "gisCoordinates": [
            {
                "xcoordinate": "3",
                "ycoordinate": "3",
                "label": "test"
            }
        ]
      },
      "label": "Kardinge, parkeerplaats voor de kerk in Noorddijk",
      "locationItem": {
        "trcid": "23421w4e234",
        "text": "Kardinge, parkeerplaats voor de kerk in Noorddijk"
      }
    },
    "locationRef": null,
    "userorganisation": "Amsterdam Marketing",
    "priceElements": [],
    "extrapriceinformations": []
  }
}'''

        JsonParser jp = new JsonFactory().createParser(source)
        ConvertedEntry convertedEntry = new JodaMapper().readValue(jp, ConvertedEntry.class)
        NDTRCValidator ndtrcValidator = new NDTRCValidator()

        ndtrcValidator.validate(convertedEntry.trcItem)
        if (ndtrcValidator.errors) {
            println(ndtrcValidator.errors)
        }

        Assert.assertTrue(ndtrcValidator.isValid)
    }
}