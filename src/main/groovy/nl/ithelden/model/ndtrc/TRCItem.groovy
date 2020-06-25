package nl.ithelden.model.ndtrc

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.ToString
import org.joda.time.DateTime

/**
 * Sample NDTRCItem:
 *     <?xml version="1.0" encoding="utf-8"?>
<trcxml xsi:schemaLocation="http://api.staging.ndtrc.nl/TrcXml/2.1/XSD.xsd"
    xmlns="http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0"
    xmlns:xsd="http://www.w3.org/2001/XMLSchema"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <nofrecords>1</nofrecords>
    <trcitems>
        <trcitem creationdate="2017-04-21T12:13:47"
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
            </priceinformation>
        </trcitem>
    </trcitems>
</trcxml>
 */
@ToString(includeNames = true)
class TRCItem {
    @JsonProperty String trcid
    @JsonProperty DateTime creationdate
    @JsonProperty DateTime availablefrom
    @JsonProperty DateTime availableto
    @JsonProperty DateTime lastupdated
    @JsonProperty DateTime lastimportedon

    @JsonProperty String createdby
    @JsonProperty String lastupdatedby
    @JsonProperty String owner
    @JsonProperty String legalowner

    @JsonProperty String externalid
    @JsonProperty String validator

    @JsonProperty WFStatus wfstatus

    @JsonProperty String cidn
    @JsonProperty Boolean published
    @JsonProperty Boolean deleted
    @JsonProperty String validatedby
    @JsonProperty Boolean offline
    @JsonProperty Boolean isprivate

    @JsonProperty EntityType entitytype
    @JsonProperty String productiontrcid

    @JsonProperty Calendar calendar
    @JsonProperty Contactinfo contactinfo
    @JsonProperty TRCItemCategories trcItemCategories
    @JsonProperty List<Performer> performers = []
    @JsonProperty List<File> files = []
    @JsonProperty List<TRCItemDetail> trcItemDetails = []
    @JsonProperty String keywords
    @JsonProperty String markers
    @JsonProperty Location location
    @JsonProperty String locationRef
    @JsonProperty String userorganisation
    @JsonProperty List<PriceElement> priceElements = []
    @JsonProperty List<ExtraPriceInformation> extrapriceinformations = []

    enum WFStatus { draft, readyforvalidation, approved, rejected, deleted }
    enum EntityType { EVENEMENT, LOCATIE }

    // below are workflow related fields, they are not part of the XSD of TRItem
    @JsonProperty Boolean forceoverwrite
    @JsonProperty String vvvPartnerApiAccessKeyId           // set if the converter want to provide the KEY
    @JsonProperty String vvvPartnerApiSecretAccessKeyId     // set if the converter want to provide the KEY
}