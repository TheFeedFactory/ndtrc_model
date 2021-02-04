package nl.ithelden.services

import nl.ithelden.model.ndtrc.*
import nl.ithelden.model.ndtrc.Calendar.When
import nl.ithelden.model.ndtrc.TRCItemCategories.CategoryValue
import org.dom4j.DocumentHelper
import org.dom4j.Element
import org.dom4j.Namespace
import org.dom4j.QName
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

class NDTRCSerializer {
    static DateTimeFormatter dayFormatter = DateTimeFormat.forPattern('dd/MM/YYYY').withZone(DateTimeZone.forID("CET"))
    static DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis()

    static Element serializeForNDTRC(List<TRCItem> trcItems) {
        Element mainElement = DocumentHelper.createElement(new QName('trcxml', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        mainElement.addAttribute('xsi:schemaLocation', 'http://api.ndtrc.nl/TrcXml/2.1/XSD.xsd')
        mainElement.addAttribute('xmlns:xsd', 'http://www.w3.org/2001/XMLSchema')
        mainElement.addAttribute('xmlns:xsi', 'http://www.w3.org/2001/XMLSchema-instance')

        mainElement.addElement('nofrecords').text = trcItems.size()

        Element trcitems = mainElement.addElement('trcitems')

        trcItems.each {
            trcitems.add(serializeTRCItem(it))
        }
        return mainElement
    }

    static Element serializeTRCItem(TRCItem trcItem) {
        Element mainElement = DocumentHelper.createElement(new QName('trcitem', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (!trcItem) {
            return mainElement
        }

        if (trcItem.calendar) {
            mainElement.add(serializeCalendar(trcItem.calendar))
        }
        if (trcItem.contactinfo) {
            mainElement.add(serializeContactinfo(trcItem.contactinfo))
        }
        if (trcItem.trcItemCategories) {
            mainElement.add(serializeTRCItemCategories(trcItem.trcItemCategories))
        }
        if (trcItem.performers) {
            Element mediaElement = mainElement.addElement('performers')
            trcItem.performers.each { Performer performer ->
                mediaElement.add(serializePerformer(performer))
            }
        }

        if (trcItem.files) {
            Element mediaElement = mainElement.addElement('media')
            trcItem.files.each { File file ->
                mediaElement.add(serializeFile(file))
            }
        }
        if (trcItem.trcItemDetails) {
            Element trcitemdetailsElement = mainElement.addElement('trcitemdetails')
            trcItem.trcItemDetails.each { TRCItemDetail trcItemDetail ->
                trcitemdetailsElement.add(serializeTRCItemDetail(trcItemDetail))
            }
        }

        if (trcItem.trcitemRelation) {
            Element trcitemRelationElement = mainElement.addElement('trcitemrelations')
            trcItem.trcitemRelation.subItemGroups.groupBy {
                return it.type.catid
            }.each { String catId, List<SubItemGroup> subItems ->
                Element groupElement = trcitemRelationElement.addElement("subitemgroup")
                groupElement.addAttribute("catid", catId)
                subItems.each {SubItemGroup subItemGroup ->
                    groupElement.add(serializeSubItemGroup(subItemGroup))
                }
            }
            // TODO serialize trcitemRelation
        }

        if (trcItem.keywords) {
            mainElement.addElement('keywords').setText(trcItem.keywords)
        }

        if (trcItem.markers) {
            mainElement.addElement('markers').setText(trcItem.markers)
        }

        if (trcItem.location) {
            mainElement.add(serializeLocation(trcItem.location))
        }

        if (trcItem.userorganisation != null) {
            mainElement.addElement('userorganisation').setText(trcItem.userorganisation)
        }

        if (trcItem.priceElements || trcItem.extrapriceinformations) {
            Element priceinformationElement = mainElement.addElement('priceinformation')

            trcItem.priceElements.each { PriceElement priceElement ->
                priceinformationElement.add(serializePriceElement(priceElement))
            }

            if (trcItem.extrapriceinformations) {
                Element extrapriceinformationsElement = priceinformationElement.addElement('extrapriceinformations')

                trcItem.extrapriceinformations.each { ExtraPriceInformation extraPriceInformation ->
                    extrapriceinformationsElement.add(serializeExtraPriceInformation(extraPriceInformation))
                }
            }
        }

        if (trcItem.availablefrom) {
            mainElement.addAttribute('availablefrom', dateTimeFormatter.print(trcItem.availablefrom))
        }
        if (trcItem.availableto != null) {
            mainElement.addAttribute('availableto', dateTimeFormatter.print(trcItem.availableto))
        }
        if (trcItem.trcid) {
            mainElement.addAttribute('trcid', trcItem.trcid)
        }
        if (trcItem.createdby) {
            mainElement.addAttribute('createdby', trcItem.createdby)
        }
        if (trcItem.creationdate) {
            mainElement.addAttribute('creationdate', dateTimeFormatter.print(trcItem.creationdate))
        }
        if (trcItem.externalid) {
            mainElement.addAttribute('externalid', trcItem.externalid)
        }
        if (trcItem.lastupdatedby) {
            mainElement.addAttribute('lastupdated', dateTimeFormatter.print(trcItem.lastupdated))
        }
        if (trcItem.lastupdatedby) {
            mainElement.addAttribute('lastupdatedby', trcItem.lastupdatedby)
        }
        if (trcItem.owner) {
            mainElement.addAttribute('owner', trcItem.owner)
        }
        if (trcItem.isprivate != null) {
            mainElement.addAttribute('private', Boolean.toString(trcItem.isprivate))
        }
        if (trcItem.validator) {
            mainElement.addAttribute('validator', trcItem.validator)
        }
        if (trcItem.wfstatus) {
            mainElement.addAttribute('wfstatus', trcItem.wfstatus.toString())
        }
        if (trcItem.cidn) {
            mainElement.addAttribute('cidn', trcItem.cidn)
        }
        if (trcItem.published != null) {
            mainElement.addAttribute('published', Boolean.toString(trcItem.published))
        }
        if (trcItem.lastimportedon) {
            mainElement.addAttribute('lastimportedon', dateTimeFormatter.print(trcItem.lastimportedon))
        }
        if (trcItem.legalowner) {
            mainElement.addAttribute('legalowner', trcItem.legalowner)
        }
        if (trcItem.deleted != null) {
            mainElement.addAttribute('deleted', Boolean.toString(trcItem.deleted))
        }
        if (trcItem.validatedby) {
            mainElement.addAttribute('validatedby', trcItem.validatedby)
        }
        if (trcItem.offline != null) {
            mainElement.addAttribute('offline', Boolean.toString(trcItem.offline))
        }
        if (trcItem.entitytype != null) {
            mainElement.addAttribute('entitytype', trcItem.entitytype.toString())
        }
        if (trcItem.productiontrcid) {
            mainElement.addAttribute('productiontrcid', trcItem.productiontrcid)
        }

        return mainElement
    }

    static Element serializeCalendar(Calendar calendar) {
        Element mainElement = DocumentHelper.createElement(new QName('calendar', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        mainElement.addAttribute('excludeholidays', Boolean.toString(calendar.excludeholidays))
        mainElement.addAttribute('cancelled', Boolean.toString(calendar.cancelled))
        mainElement.addAttribute('soldout', Boolean.toString(calendar.soldout))

        if (calendar.onrequest != null) {
            mainElement.addAttribute('onrequest', Boolean.toString(calendar.onrequest))
        }
        if (calendar.alwaysopen != null) {
            mainElement.addAttribute('alwaysopen', Boolean.toString(calendar.alwaysopen))
        }

        calendar.singleDates?.each { Calendar.SingleDate singleDate ->
            if (!singleDate.date) return

            Element singleElement = mainElement.addElement('single')
            singleElement.addAttribute('date', dayFormatter.print(singleDate.date))
            singleDate.when.each { When when ->
                if (!when.timestart && !when.timeend) return

                singleElement.add(serializeWhen(when))
            }
        }

        calendar.patternDates?.each { Calendar.PatternDate patternDate ->
            Element patternElement = mainElement.addElement('pattern')

            serializePatternElement(patternDate, patternElement)
        }

        if (calendar.opens || calendar.closeds || calendar.soldouts || calendar.cancelleds) {
            Element exceptionsElement = mainElement.addElement('exceptions')

            calendar.opens?.each {
                serializeExceptionDateElement(it, exceptionsElement.addElement('open'))
            }
            calendar.closeds?.each {
                serializeExceptionDateElement(it, exceptionsElement.addElement('closed'))
            }
            calendar.cancelleds?.each {
                serializeExceptionDateElement(it, exceptionsElement.addElement('cancelled'))
            }
            calendar.soldouts?.each {
                serializeExceptionDateElement(it, exceptionsElement.addElement('soldout'))
            }
        }

        if (calendar.comment) {
            Element commentElement = mainElement.addElement('comment')

            if (calendar.comment.label) {
                commentElement.addAttribute('label', calendar.comment.label)
            }
            calendar.comment.commentTranslations.each { Calendar.CommentTranslation commentTranslation ->
                Element commentTranslationElement = commentElement.addElement('commenttranslation')

                commentTranslationElement.addAttribute('lang', commentTranslation.lang)
                commentTranslationElement.addAttribute('label', commentTranslation.label)
            }
        }
        return mainElement
    }

    static void serializePatternElement(Calendar.PatternDate patternDate, Element patternElement) {
        if (patternDate.recurrence != null) {
            patternElement.addAttribute('recurrence', Integer.toString(patternDate.recurrence))
        }
        if (patternDate.occurrence != null) {
            patternElement.addAttribute('occurrence', Integer.toString(patternDate.occurrence))
        }
        if (patternDate.recurrencyType != null) {
            patternElement.addAttribute('type', patternDate.recurrencyType.toString())
        }
        if (patternDate.startdate != null) {
            patternElement.addAttribute('startdate', dayFormatter.print(patternDate.startdate))
        }
        if (patternDate.enddate != null) {
            patternElement.addAttribute('enddate', dayFormatter.print(patternDate.enddate))
        }

        patternDate.opens.each { Calendar.PatternDate.Open open ->
            Element openElement = patternElement.addElement('open')

            if (open.month != null) {
                openElement.addAttribute('month', Integer.toString(open.month))
            }
            if (open.weeknumber != null) {
                openElement.addAttribute('weeknumber', Integer.toString(open.weeknumber))
            }
            if (open.daynumber != null) {
                openElement.addAttribute('daynumber', Integer.toString(open.daynumber))
            }
            if (open.day != null) {
                openElement.addAttribute('day', Integer.toString(open.day))
            }

            open.whens.each { When when ->
                if (!when.timestart && ! when.timeend) return

                openElement.add(serializeWhen(when))
            }
        }
    }

    static void serializeExceptionDateElement(Calendar.ExceptionDate exceptionDate, Element exceptionElement) {
        if (exceptionDate.date != null) {
            exceptionElement.addAttribute('date', dayFormatter.print(exceptionDate.date))
        }

        exceptionDate.whens.each { When when ->
            exceptionElement.add(serializeWhen(when))
        }
    }

    static Element serializeWhen(When when) {
        Element mainElement = DocumentHelper.createElement(new QName('when', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        mainElement.addAttribute('timestart', when.timestart)
        mainElement.addAttribute('timeend', (when.timeend))
        if (when.status != null) {
            mainElement.addAttribute('status', when.status.toString())
        }

        if (when.statustranslations) {
            Element statusTranslationsElement = mainElement.addElement('statustranslations')

            when.statustranslations.each {
                Element e = statusTranslationsElement.addElement('statustranslation')
                e.addAttribute('lang', it.lang)
                e.setText(it.text ?: "")
            }
        }
        if (when.extrainformations) {
            Element extrainformationElement = mainElement.addElement('extrainformations')

            when.extrainformations.each {
                Element e = extrainformationElement.addElement('extrainformation')
                e.addAttribute('lang', it.lang)
                e.setText(it.text ?: "")
            }
        }

        return mainElement
    }

    static Element serializePriceElement(PriceElement priceElement) {
        Element mainElement = DocumentHelper.createElement(new QName('priceelement', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (priceElement.freeentrance != null) {
            mainElement.addElement('freeentrance').setText(Boolean.toString(priceElement.freeentrance).toLowerCase())
        }

        if (priceElement.priceValue != null) {
            Element pricevalueElement = mainElement.addElement('pricevalues')
            if (priceElement.priceValue.from != null) {
                pricevalueElement.addElement('pricefrom').setText(Double.toString(priceElement.priceValue.from))
            }
            if (priceElement.priceValue.until != null) {
                pricevalueElement.addElement('priceuntil').setText(Double.toString(priceElement.priceValue.until))
            }
        }

        if (priceElement.description) {
            Element pricedescriptionElement = mainElement.addElement('pricedescription')
            if (priceElement.description.value) {
                pricedescriptionElement.addAttribute('value', priceElement.description.value.toString())
            }

            if (priceElement.description.descriptionTranslations) {
                Element pd = pricedescriptionElement.addElement('pricedescriptiontranslations')

                priceElement.description.descriptionTranslations.each {
                    pd.addElement('pricedescriptiontranslation').addAttribute('lang', it.lang).setText(it.text ?: "")
                }
            }
        }

        if (priceElement.comments?.any { it?.text?.trim() }) {
            Element pricecommentsElement = mainElement.addElement('pricecomments')

            priceElement.comments.each {
                if (it?.text) {
                    pricecommentsElement.addElement('pricecomment').setText(it.text ?: "")
                }
            }
        }

        return mainElement
    }

    static Element serializeExtraPriceInformation(ExtraPriceInformation extraPriceInformation) {

        Element mainElement =  DocumentHelper.createElement(
            new QName('extrapriceinformation', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        mainElement.addAttribute('lang', extraPriceInformation.lang).setText(extraPriceInformation.text ?: "")

        return mainElement
    }

    static Element serializePerformer(Performer performer) {

        Element mainElement =  DocumentHelper.createElement(
                new QName('performer', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        Element labelElement =  DocumentHelper.createElement(
                new QName('label', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))
        labelElement.setText(performer.label)

        mainElement.add(labelElement)

        Element roleElement =  DocumentHelper.createElement(
                new QName('role', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        roleElement.addAttribute('roleid', performer.roleid)
        roleElement.addAttribute('label', performer.rolelabel)

        mainElement.add(roleElement)

        return mainElement
    }

    static Element serializeLocation(Location location) {
        Element mainElement = DocumentHelper.createElement(new QName('location', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (location.address) {
            mainElement.add(serializeAddress(location.address, location.label))
        }

        if (location.label) {
            mainElement.add(serializeLabel(location.label))
        }
        if (location.locationItem) {
            mainElement.add(serializeLocationItem(location.locationItem))
        }

        return mainElement
    }

    static Element serializeLabel(String label) {
        Element mainElement = DocumentHelper.createElement(new QName('label', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0'))).addText(label ?: "")
        return mainElement
    }

    static Element serializeLocationItem(Location.LocationItem locationItem) {
        Element mainElement = DocumentHelper.createElement(new QName('locationitem', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (locationItem.trcid) {
            mainElement.addAttribute('trcid', locationItem.trcid)
        }
        if (locationItem.text) {
            mainElement.addText(locationItem.text)
        }

        return mainElement
    }

    static Element serializeContactinfo(Contactinfo contactinfo) {
        Element contactinfoElement = DocumentHelper.createElement(new QName('contactinfo', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (contactinfo.address) {
            contactinfoElement.add(serializeAddress(contactinfo.address))
        }
        if (contactinfo.addresses) {
            contactinfo.addresses.each { Address address ->
                contactinfoElement.add(serializeAddress(address))
            }
        }

        if (contactinfo.mail) {
            contactinfoElement.add(serializeMail(contactinfo.mail))
        }
        if (contactinfo.mails) {
            contactinfo.mails.each { Contactinfo.Mail mail ->
                contactinfoElement.add(serializeMail(mail))
            }
        }

        if (contactinfo.phone) {
            contactinfoElement.add(serializePhone(contactinfo.phone))
        }
        if (contactinfo.phones) {
            contactinfo.phones.each { Contactinfo.Phone phone ->
                contactinfoElement.add(serializePhone(phone))
            }
        }

        if (contactinfo.fax) {
            contactinfoElement.add(serializeFax(contactinfo.fax))
        }
        if (contactinfo.faxes) {
            contactinfo.faxes.each { Contactinfo.Fax fax ->
                contactinfoElement.add(serializeFax(fax))
            }
        }

        if (contactinfo.urls) {
            contactinfo.urls.each {Contactinfo.Url url ->
                contactinfoElement.add(serializeUrl(url))
            }
        }
        return contactinfoElement
    }

    static Element serializeGISCoordinate(GISCoordinate gisCoordinate) {
        Element giscoordinateElement = DocumentHelper.createElement(new QName('giscoordinate', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (gisCoordinate.xcoordinate) {
            giscoordinateElement.addElement('xcoordinate').addText(gisCoordinate.xcoordinate ?: "")
        }
        if (gisCoordinate.ycoordinate) {
            giscoordinateElement.addElement('ycoordinate').addText(gisCoordinate.ycoordinate ?: "")
        }
        if (gisCoordinate.label) {
            giscoordinateElement.addElement('label').addText(gisCoordinate.label ?: "")
        }

        return giscoordinateElement
    }

    static Element serializeUrl(Contactinfo.Url url) {
        Element mainElement = DocumentHelper.createElement(new QName('url', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        mainElement.addAttribute('value', url?.url?.toString() ?: "")
        mainElement.addAttribute('targetLanguage', url.targetLanguage)
        mainElement.addAttribute('urlServiceType', url.urlServiceType?.toString() ?: "")

        if (url.reservations != null) { mainElement.addAttribute('reservations', Boolean.toString(url.reservations).toLowerCase()) }

        mainElement.addAttribute('descriptioncode', url.descriptioncode)

        if (url.descriptionTranslations) {
            url.descriptionTranslations.each { Contactinfo.DescriptionTranslation descriptionTranslation ->
                Element descriptiontranslationElement = mainElement.addElement('descriptiontranslation')
                descriptiontranslationElement.addAttribute('lang', descriptionTranslation.lang)
                descriptiontranslationElement.addAttribute('label', descriptionTranslation.label)
            }
        }
        return mainElement
    }

    static Element serializePhone(Contactinfo.Phone phone) {
        Element mainElement = DocumentHelper.createElement(new QName('phone', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        mainElement.addAttribute('value', phone.number)
        if (phone.reservations != null) { mainElement.addAttribute('reservations', Boolean.toString(phone.reservations).toLowerCase()) }
        mainElement.addAttribute('descriptioncode', phone.descriptioncode)

        if (phone.descriptionTranslations) {
            phone.descriptionTranslations.each { Contactinfo.DescriptionTranslation descriptionTranslation ->
                Element descriptiontranslationElement = mainElement.addElement('descriptiontranslation')
                descriptiontranslationElement.addAttribute('lang', descriptionTranslation.lang)
                descriptiontranslationElement.addAttribute('label', descriptionTranslation.label)
            }
        }
        return mainElement
    }

    static Element serializeMail(Contactinfo.Mail mail) {
        Element mainElement = DocumentHelper.createElement(new QName('mail', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        mainElement.addAttribute('value', mail.email)
        if (mail.reservations != null) { mainElement.addAttribute('reservations', Boolean.toString(mail.reservations).toLowerCase()) }
        mainElement.addAttribute('descriptioncode', mail.descriptioncode)

        if (mail.descriptionTranslations) {
            mail.descriptionTranslations.each { Contactinfo.DescriptionTranslation descriptionTranslation ->
                Element descriptiontranslationElement = mainElement.addElement('descriptiontranslation')
                descriptiontranslationElement.addAttribute('lang', descriptionTranslation.lang)
                descriptiontranslationElement.addAttribute('label', descriptionTranslation.label)
            }
        }

        return mainElement
    }

    static Element serializeFax(Contactinfo.Fax fax) {
        Element mainElement = DocumentHelper.createElement(new QName('fax', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        mainElement.addAttribute('value', fax.number)
        if (fax.reservations != null) {
            mainElement.addAttribute('reservations', Boolean.toString(fax.reservations).toLowerCase())
        }
        mainElement.addAttribute('descriptioncode', fax.descriptioncode)

        if (fax.descriptionTranslations) {
            fax.descriptionTranslations.each { Contactinfo.DescriptionTranslation descriptionTranslation ->
                Element descriptiontranslationElement = mainElement.addElement('descriptiontranslation')
                descriptiontranslationElement.addAttribute('lang', descriptionTranslation.lang)
                descriptiontranslationElement.addAttribute('label', descriptionTranslation.label)
            }
        }

        return mainElement
    }

    static Element serializeAddress(Address address, String label = null) {
        Element addressElement = DocumentHelper.createElement(new QName('address', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))
        if (address.reservation != null) { addressElement.addAttribute('reservation', Boolean.toString(address.reservation).toLowerCase())}
        if (address.main != null) { addressElement.addAttribute('main', Boolean.toString(address.main).toLowerCase()) }

        if (address.city) {
            Element physicalElement = addressElement.addElement('physical')
            physicalElement.addAttribute('giscoordinateType', 'points')

            if(address.city != null) {
                Element cityElement = physicalElement.addElement('city').addText(address.city ?: "")
                if (address.citytrcid) {
                    cityElement.addAttribute("trcid", address.citytrcid)
                }

            }
            if(address.country != null) { physicalElement.addElement('country').addText(address.country ?: "") }
            if(address.housenr?.trim()) { physicalElement.addElement('housenr').addText(address.housenr) }

            if(address.street != null) {
                Element streetElement = physicalElement.addElement('street').addText(address.street ?: "")
                if (address.streettrcid) {
                    streetElement.addAttribute("trcid", address.streettrcid)
                }
            }

            if(address.zipcode?.trim()) { physicalElement.addElement('zipcode').addText(address.zipcode.trim()) }
            if(address.province?.trim()) { physicalElement.addElement('province').addText(address.province.trim()) }
            if (address.gisCoordinates) {
                Element giscoordinatesElement = physicalElement.addElement('giscoordinates')

                address.gisCoordinates.each { GISCoordinate gisCoordinate ->
                    giscoordinatesElement.add(serializeGISCoordinate(gisCoordinate))
                }
                if (address.gisCoordinates.size() > 0) {
                    physicalElement.addElement("xcoordinate").setText(address.gisCoordinates[0].xcoordinate)
                    physicalElement.addElement("ycoordinate").setText(address.gisCoordinates[0].ycoordinate)
                }
            }
        } else {
            Element virtualElement = addressElement.addElement('virtual')
            virtualElement.addElement('title').addText(address.title?.trim() ?: label)

            Element giscoordinatesElement = virtualElement.addElement('giscoordinates')

            address.gisCoordinates.each { GISCoordinate gisCoordinate ->
                giscoordinatesElement.add(serializeGISCoordinate(gisCoordinate))
            }
        }

        return addressElement
    }

    static Element serializeTRCItemCategories(TRCItemCategories trcItemCategories) {
        Element mainElement = DocumentHelper.createElement(new QName('trcitemcategories', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (trcItemCategories.types) {
            Element typesElement = mainElement.addElement('types')

            trcItemCategories.types.each { TRCItemCategories.Type type ->
                typesElement.add(serializeType(type))
            }
        }

        if (trcItemCategories.categories) {
            Element categoriesElement = mainElement.addElement('categories')

            trcItemCategories.categories.each { TRCItemCategories.Category category ->
                categoriesElement.add(serializeCategory(category))
            }
        }

        if (trcItemCategories.soldout != null) {
            mainElement.addElement('soldout').setText("${trcItemCategories.soldout}")
        }
        if (trcItemCategories.canceled != null) {
            mainElement.addElement('canceled').setText("${trcItemCategories.canceled}")
        }
        return mainElement
    }

    static Element serializeSubItemGroup(SubItemGroup subItemGroup) {
        Element subItemElement = DocumentHelper.createElement(new QName('subitem', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))
        subItemElement.addAttribute("trcid", subItemGroup.trcid)

        if (subItemGroup.type) {
            subItemElement.add(serializeSubItemType(subItemGroup.type))
        }
        if (subItemGroup.subItemTranslations) {
            Element subitemdetailsElement = subItemElement.addElement('subitemdetails')
            subItemGroup.subItemTranslations.each {
                SubItemGroup.SubItemTranslation subItemTranslation ->
                    subitemdetailsElement.add(serializeSubItemDetailsTranslation(subItemTranslation))
            }
        }
        if (subItemGroup.categories) {
            Element categoriesElement = subItemElement.addElement('categories')

            subItemGroup.categories.each { SubItemGroup.Category category ->
                categoriesElement.add(serializeSubItemGroupCategory(category))
            }
        }

        if (subItemGroup.media) {
            Element mediaElement = subItemElement.addElement('media')

            subItemGroup.media.each { File file ->
                mediaElement.add(serializeFile(file))
            }
        }
        return subItemElement
    }

    static Element serializeType(TRCItemCategories.Type type) {
        Element mainElement = DocumentHelper.createElement(new QName('type', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (type.catid != null) mainElement.addAttribute('catid', type.catid)
        if (type.isDefault != null) mainElement.addAttribute('default', Boolean.toString(type.isDefault).toLowerCase())
        if (type.categoryTranslations) {
            if (type.categoryTranslations.any  { it.lang == 'nl'}) {
                mainElement.addText(type.categoryTranslations.find  { it.lang == 'nl'}.label)
            }
            type.categoryTranslations.each { TRCItemCategories.CategoryTranslation categoryTranslation ->
                mainElement.add(serializeCategoryTranslation(categoryTranslation, 'categorytranslation'))
            }
        }
        return mainElement
    }

    static Element serializeSubItemType(SubItemGroup.Type type) {
        Element mainElement = DocumentHelper.createElement(new QName('type', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (type.catid != null) mainElement.addAttribute('catid', type.catid)
        if (type.isDefault != null) mainElement.addAttribute('default', Boolean.toString(type.isDefault).toLowerCase())
        if (type.categoryTranslations) {
            if (type.categoryTranslations.any  { it.lang == 'nl'}) {
                mainElement.addText(type.categoryTranslations.find  { it.lang == 'nl'}.label)
            }
            type.categoryTranslations.each { SubItemGroup.CategoryTranslation categoryTranslation ->
                mainElement.add(serializeSubItemGroupCategoryTranslation(categoryTranslation, 'categorytranslation'))
            }
        }
        return mainElement
    }

    static Element serializeSubItemDetailsTranslation(SubItemGroup.SubItemTranslation subItemTranslation) {
        Element mainElement = DocumentHelper.createElement(new QName('subitemdetail', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (subItemTranslation.lang != null) mainElement.addAttribute('lang', subItemTranslation.lang)
        if (subItemTranslation.title) {
            mainElement.addElement("title").setText(subItemTranslation.title)
        }
        return mainElement
    }

    static Element serializeCategory(TRCItemCategories.Category category) {
        Element mainElement = DocumentHelper.createElement(new QName('category', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (category.catid != null) mainElement.addAttribute('catid', category.catid)
        if (category.valueid != null) mainElement.addAttribute('valueid', category.valueid)
        if (category.value != null) mainElement.addAttribute('value', category.value)
        if (category.datatype != null) mainElement.addAttribute('datatype', category.datatype.toString())

        if(category.categoryvalues) {
            Element categoriesElement = mainElement.addElement('categoryvalues')
            category.categoryvalues.each { CategoryValue categoryvalue ->
                categoriesElement.add(serializeCategoryValue(categoryvalue))
            }
        }

        if(category.categoryTranslations) {
            category.categoryTranslations.each { TRCItemCategories.CategoryTranslation categoryTranslation ->
                mainElement.add(serializeCategoryTranslation(categoryTranslation, 'categorytranslation'))
            }
        }
        if(category.parentCategoryTranslations) {
            category.parentCategoryTranslations.each { TRCItemCategories.CategoryTranslation categoryTranslation ->
                mainElement.add(serializeCategoryTranslation(categoryTranslation, 'parentcategorytranslation'))
            }
        }
        if(category.valueCategoryTranslations) {
            category.valueCategoryTranslations.each { TRCItemCategories.CategoryTranslation categoryTranslation ->
                mainElement.add(serializeCategoryTranslation(categoryTranslation, 'valuecategorytranslation'))
            }
        }

        return mainElement
    }

    static Element serializeSubItemGroupCategory(SubItemGroup.Category category) {
        Element mainElement = DocumentHelper.createElement(new QName('category', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (category.catid != null) mainElement.addAttribute('catid', category.catid)
        if (category.valueid != null) mainElement.addAttribute('valueid', category.valueid)
        if (category.value != null) mainElement.addAttribute('value', category.value)
        if (category.datatype != null) mainElement.addAttribute('datatype', category.datatype.toString())

        if (category.categoryvalues) {
            Element categoriesElement = mainElement.addElement('categoryvalues')
            category.categoryvalues.each { SubItemGroup.CategoryValue categoryvalue ->
                categoriesElement.add(serializeSubItemGroupCategoryValue(categoryvalue))
            }
        }

        if (category.categoryTranslations) {
            category.categoryTranslations.each { SubItemGroup.CategoryTranslation categoryTranslation ->
                mainElement.add(serializeSubItemGroupCategoryTranslation(categoryTranslation, 'categorytranslation'))
            }
        }
        if (category.parentCategoryTranslations) {
            category.parentCategoryTranslations.each { SubItemGroup.CategoryTranslation categoryTranslation ->
                mainElement.add(serializeSubItemGroupCategoryTranslation(categoryTranslation, 'parentcategorytranslation'))
            }
        }
        if (category.valueCategoryTranslations) {
            category.valueCategoryTranslations.each { SubItemGroup.CategoryTranslation categoryTranslation ->
                mainElement.add(serializeSubItemGroupCategoryTranslation(categoryTranslation, 'valuecategorytranslation'))
            }
        }

        return mainElement
    }

    static Element serializeCategoryValue(CategoryValue categoryValue) {
        Element mainElement = DocumentHelper.createElement(new QName('categoryvalue', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (categoryValue.catid != null) mainElement.addAttribute('catid', categoryValue.catid)
        if (categoryValue.categorytranslations) {
            categoryValue.categorytranslations.each { TRCItemCategories.CategoryTranslation categoryTranslation ->
                mainElement.add(serializeCategoryTranslation(categoryTranslation, 'categorytranslation'))
            }
        }
        return mainElement
    }

    static Element serializeSubItemGroupCategoryValue(SubItemGroup.CategoryValue categoryValue) {
        Element mainElement = DocumentHelper.createElement(new QName('categoryvalue', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (categoryValue.catid != null) mainElement.addAttribute('catid', categoryValue.catid)
        if (categoryValue.categorytranslations) {
            categoryValue.categorytranslations.each { SubItemGroup.CategoryTranslation categoryTranslation ->
                mainElement.add(serializeSubItemGroupCategoryTranslation(categoryTranslation, 'categorytranslation'))
            }
        }

        return mainElement
    }

    static Element serializeCategoryTranslation(TRCItemCategories.CategoryTranslation categoryTranslation, String elementName = 'categorytranslation') {
        Element mainElement = DocumentHelper.createElement(new QName(elementName, new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (categoryTranslation.lang != null) mainElement.addAttribute('lang', categoryTranslation.lang)
        if (categoryTranslation.label != null) mainElement.addAttribute('label', categoryTranslation.label)
        if (categoryTranslation.unit != null) mainElement.addAttribute('unit', categoryTranslation.unit)
        if (categoryTranslation.explanation != null) mainElement.addAttribute('explanation', categoryTranslation.explanation)
        if (categoryTranslation.catid != null) mainElement.addAttribute('catid', categoryTranslation.catid)
        if (categoryTranslation.value != null) mainElement.addAttribute('value', categoryTranslation.value)

        return mainElement
    }

    static Element serializeSubItemGroupCategoryTranslation(SubItemGroup.CategoryTranslation categoryTranslation, String elementName = 'categorytranslation') {
        Element mainElement = DocumentHelper.createElement(new QName(elementName, new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (categoryTranslation.lang != null) mainElement.addAttribute('lang', categoryTranslation.lang)
        if (categoryTranslation.label  != null) mainElement.addAttribute('label', categoryTranslation.label)
        if (categoryTranslation.unit != null) mainElement.addAttribute('unit', categoryTranslation.unit)
        if (categoryTranslation.explanation != null) mainElement.addAttribute('explanation', categoryTranslation.explanation)
        if (categoryTranslation.catid != null) mainElement.addAttribute('catid', categoryTranslation.catid)
        if (categoryTranslation.value != null) mainElement.addAttribute('value', categoryTranslation.value)

        return mainElement
    }

    static Element serializeTRCItemDetail(TRCItemDetail trcItemDetail) {
        Element mainElement = DocumentHelper.createElement(new QName('trcitemdetail', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (trcItemDetail.lang != null) mainElement.addAttribute('lang', trcItemDetail.lang)

        if (trcItemDetail.longdescription != null)
            mainElement.addElement('longdescription').setText(trcItemDetail.longdescription)
        if (trcItemDetail.shortdescription != null)
            mainElement.addElement('shortdescription').setText(trcItemDetail.shortdescription)
        if (trcItemDetail.title != null)
            mainElement.addElement('title').setText(trcItemDetail.title)

        return mainElement
    }

    static Element serializeFile(File file) {
        Element mainElement = DocumentHelper.createElement(new QName('file', new Namespace(null, 'http://www.vvvnederland.nl/XMLSchema/TrcXml/2.0')))

        if (file.trcid != null) mainElement.addAttribute('trcid', file.trcid)
        if (file.main != null) mainElement.addAttribute('main', Boolean.toString(file.main).toLowerCase())

        if (file.copyright != null) {
            mainElement.addElement('copyright').setText(file.copyright)
        }
        if (file.filename != null) {
            mainElement.addElement('filename').setText(file.filename)
        }
        if (file.filetype != null) {
            mainElement.addElement('filetype').setText(file.filetype.toString())
        }
        if (file.hlink != null) {
            mainElement.addElement('hlink').setText(file.hlink)
        }
        if (file.mediatype != null) {
            mainElement.addElement('mediatype').setText(file.mediatype.toString())
        }

        if (file.title) {
            Element titleElement = mainElement.addElement('title')

            if (file.title.label) {
                titleElement.addAttribute('label', file.title.label)
            }

            file.title.titleTranslations.each { File.Title.TitleTranslation titleTranslation ->
                Element titletranslationElement = titleElement.addElement('titletranslation')
                if (titleTranslation.lang != null) {
                    titletranslationElement.addAttribute('lang', titleTranslation.lang)
                }
                if (titleTranslation.label != null) {
                    titletranslationElement.addAttribute('label', titleTranslation.label)
                }
            }
        }

        return mainElement
    }

}
