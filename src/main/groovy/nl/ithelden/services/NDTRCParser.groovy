package nl.ithelden.services

import groovy.util.logging.Slf4j
import nl.ithelden.model.ndtrc.*
import nl.ithelden.model.ndtrc.Calendar.When
import org.dom4j.Element
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

@Slf4j
class NDTRCParser {
    static DateTimeFormatter dayFormatter = DateTimeFormat.forPattern('dd/MM/YYYY')

    static TRCItem parseTRCItem(Element trcItemElement) {
        TRCItem trcItem = new TRCItem(
            availablefrom: trcItemElement.attributeValue('availablefrom') ? parseDate(trcItemElement.attributeValue('availablefrom')) : null,
            availableto: trcItemElement.attributeValue('availableto') ? parseDate(trcItemElement.attributeValue('availableto')) : null,
            trcid: trcItemElement.attributeValue('trcid'),
            createdby: trcItemElement.attributeValue('createdby'),
            creationdate: trcItemElement.attributeValue('creationdate') ? parseDate(trcItemElement.attributeValue('creationdate')) : null,
            externalid: trcItemElement.attributeValue('externalid'),
            lastupdated: trcItemElement.attributeValue('lastupdated') ? parseDate(trcItemElement.attributeValue('lastupdated')) : null,
            lastupdatedby: trcItemElement.attributeValue('lastupdatedby'),
            owner: trcItemElement.attributeValue('owner'),
            isprivate: trcItemElement.attributeValue('private') ? Boolean.parseBoolean(trcItemElement.attributeValue('private')) : null,
            validator: trcItemElement.attributeValue('validator'),
            wfstatus: trcItemElement.attributeValue('wfstatus') ? TRCItem.WFStatus.valueOf(trcItemElement.attributeValue('wfstatus')) : null,
            cidn: trcItemElement.attributeValue('cidn'),
            published: trcItemElement.attributeValue('published') ? Boolean.parseBoolean(trcItemElement.attributeValue('published')) : null,
            lastimportedon: trcItemElement.attributeValue('lastimportedon') ? parseDate(trcItemElement.attributeValue('lastimportedon')) : null,
            legalowner: trcItemElement.attributeValue('legalowner'),
            deleted: trcItemElement.attributeValue('deleted') ? Boolean.parseBoolean(trcItemElement.attributeValue('published')): null,
            validatedby: trcItemElement.attributeValue('validatedby'),
            offline: trcItemElement.attributeValue('offline') ? Boolean.parseBoolean(trcItemElement.attributeValue('offline')): null,
            entitytype: trcItemElement.attributeValue('entitytype') ? TRCItem.EntityType.valueOf(trcItemElement.attributeValue('entitytype')): null,
            productiontrcid: trcItemElement.attributeValue('productiontrcid'),
        )

        if (trcItemElement.element('calendar')) {
            trcItem.calendar = parseCalendar(trcItemElement.element('calendar'))
        }

        if (trcItemElement.element('contactinfo')) {
            trcItem.contactinfo = parseContactInfo(trcItemElement.element('contactinfo'))
        }

        if (trcItemElement.element('trcitemcategories')) {
            trcItem.trcItemCategories = parseCategories(trcItemElement.element('trcitemcategories'))
        }

        if (trcItemElement.element('media')) {
            trcItem.files = parseMedia(trcItemElement.element('media'))
        }

        if (trcItemElement.element('trcitemdetails')) {
            trcItem.trcItemDetails = trcItemElement.element('trcitemdetails').selectNodes('*[local-name()="trcitemdetail"]').collect { Element TRCItemDetailElement ->
                return parseTRCItemDetail(TRCItemDetailElement)
            }
        }

        if (trcItemElement.element('priceinformation')) {
            trcItem.priceElements = trcItemElement.selectNodes('*[local-name()="priceinformation"]/*[local-name()="priceelement"]').collect { Element priceElement ->
                return parsePriceElement(priceElement)
            }

            trcItem.extrapriceinformations = trcItemElement.selectNodes('*[local-name()="priceinformation"]/*[local-name()="extrapriceinformations"]/*[local-name()="extrapriceinformation"]').collect { Element extrapriceinformationElement ->
                return parseExtraPriceInformationElement(extrapriceinformationElement)
            }
        }

        if (trcItemElement.element('location')) {
            trcItem.location = parseLocation(trcItemElement.element('location'))
        }

        if (trcItemElement.element('userorganisation')) {
            trcItem.userorganisation = trcItemElement.element('userorganisation')?.text
        }

        return trcItem
    }

    static Calendar parseCalendar(Element calendarElement) {

        Calendar calendar = new Calendar(
                excludeholidays: Boolean.parseBoolean(calendarElement.attributeValue('excludeholidays')),
                cancelled: Boolean.parseBoolean(calendarElement.attributeValue('cancelled')),
                soldout: Boolean.parseBoolean(calendarElement.attributeValue('soldout')),
                onrequest: calendarElement.attributeValue('onrequest') ? Boolean.parseBoolean(calendarElement.attributeValue('onrequest')) : null,
                alwaysopen: calendarElement.attributeValue('alwaysopen') ? Boolean.parseBoolean(calendarElement.attributeValue('alwaysopen')) : null
        )

        if (calendarElement.selectNodes('*[local-name()="pattern"]')) {
            calendar.patternDates = calendarElement.selectNodes('*[local-name()="pattern"]').collect { Element patternElement ->
                return parsePatternDate(patternElement)
            }
        }

        if (calendarElement.selectNodes('*[local-name()="exceptions"]')) {

            if (calendarElement.selectNodes('*[local-name()="exceptions"]/*[local-name()="open"]')) {
                calendar.opens = calendarElement.selectNodes('*[local-name()="exceptions"]/*[local-name()="open"]').collect { Element patternElement ->
                    return parseExceptionDate(patternElement)
                }
            }
            if (calendarElement.selectNodes('*[local-name()="exceptions"]/*[local-name()="closed"]')) {
                calendar.closeds = calendarElement.selectNodes('*[local-name()="exceptions"]/*[local-name()="closed"]').collect { Element patternElement ->
                    return parseExceptionDate(patternElement)
                }
            }
            if (calendarElement.selectNodes('*[local-name()="exceptions"]/*[local-name()="cancelled"]')) {
                calendar.cancelleds = calendarElement.selectNodes('*[local-name()="exceptions"]/*[local-name()="cancelled"]').collect { Element patternElement ->
                    return parseExceptionDate(patternElement)
                }
            }
            if (calendarElement.selectNodes('*[local-name()="exceptions"]/*[local-name()="soldout"]')) {
                calendar.soldouts = calendarElement.selectNodes('*[local-name()="exceptions"]/*[local-name()="soldout"]').collect { Element patternElement ->
                    return parseExceptionDate(patternElement)
                }
            }
        }

        if (calendarElement.selectNodes('*[local-name()="single"]')) {
            calendar.singleDates = calendarElement.selectNodes('*[local-name()="single"]').collect { Element singleElement ->
                Calendar.SingleDate singleDate = new Calendar.SingleDate(
                    date: singleElement.attributeValue('date') ? dayFormatter.parseDateTime(singleElement.attributeValue('date')) : null,
                )

                singleDate.when = singleElement.selectNodes('*[local-name()="when"]').collect { Element whenElement ->
                    return new When(
                        timeend: whenElement.attributeValue('timeend'),
                        timestart: whenElement.attributeValue('timestart'),
                        status: whenElement.attributeValue('status') ? When.Status.valueOf(whenElement.attributeValue('status')) : null
                    )
                }

                return singleDate
            }
        }

        if (calendarElement.selectNodes('*[local-name()="comment"]')) {

            if (calendarElement.selectNodes('*[local-name()="comment"]')) {
                Element commentElement = (Element) calendarElement.selectSingleNode('*[local-name()="comment"]')

                calendar.comment = new Calendar.Comment(
                    label: commentElement.attributeValue('label'),
                    commentTranslations: commentElement.selectNodes('*[local-name()="commenttranslation"]').collect { Element commentTranslationElement ->
                        return new Calendar.CommentTranslation(
                            lang:  commentTranslationElement.attributeValue('lang'),
                            label: commentTranslationElement.attributeValue('label')
                        )
                    }
                )
            }

            if (calendarElement.selectNodes('*[local-name()="exceptions"]/*[local-name()="closed"]')) {
                calendar.closeds = calendarElement.selectNodes('*[local-name()="exceptions"]/*[local-name()="closed"]').collect { Element patternElement ->
                    return parseExceptionDate(patternElement)
                }
            }
            if (calendarElement.selectNodes('*[local-name()="exceptions"]/*[local-name()="cancelled"]')) {
                calendar.cancelleds = calendarElement.selectNodes('*[local-name()="exceptions"]/*[local-name()="cancelled"]').collect { Element patternElement ->
                    return parseExceptionDate(patternElement)
                }
            }
            if (calendarElement.selectNodes('*[local-name()="exceptions"]/*[local-name()="soldout"]')) {
                calendar.soldouts = calendarElement.selectNodes('*[local-name()="exceptions"]/*[local-name()="soldout"]').collect { Element patternElement ->
                    return parseExceptionDate(patternElement)
                }
            }
        }

        return calendar
    }

    static Calendar.PatternDate parsePatternDate(Element patternElement) {
        Calendar.PatternDate patternDate = new Calendar.PatternDate(
                recurrence: patternElement.attributeValue('recurrence') ? Integer.parseInt(patternElement.attributeValue('recurrence')) : null,
                occurrence: patternElement.attributeValue('occurrence') ? Integer.parseInt(patternElement.attributeValue('occurrence')) : null,
                recurrencyType: patternElement.attributeValue('type') ? Calendar.PatternDate.RecurrencyType.valueOf(patternElement.attributeValue('type')) : null,
                startdate: patternElement.attributeValue('startdate') ? dayFormatter.parseDateTime(patternElement.attributeValue('startdate')) : null,
                enddate: patternElement.attributeValue('enddate') ? dayFormatter.parseDateTime(patternElement.attributeValue('enddate')) : null,
        )

        patternDate.opens = patternElement.selectNodes('*[local-name()="open"]').collect { Element openElement ->

            Calendar.PatternDate.Open open = new Calendar.PatternDate.Open(
                    month: openElement.attributeValue('month') ? Integer.parseInt(openElement.attributeValue('month')) : null,
                    weeknumber: openElement.attributeValue('weeknumber') ? Integer.parseInt(openElement.attributeValue('weeknumber')) : null,
                    daynumber: openElement.attributeValue('daynumber') ? Integer.parseInt(openElement.attributeValue('daynumber')) : null,
                    day: openElement.attributeValue('day') ? Integer.parseInt(openElement.attributeValue('day')) : null
            )

            open.whens = openElement.selectNodes('*[local-name()="when"]').collect { Element whenElement ->
                When when = new When(
                        timeend: whenElement.attributeValue('timeend'),
                        timestart: whenElement.attributeValue('timestart'),
                        status: whenElement.attributeValue('status') ? When.Status.valueOf(whenElement.attributeValue('status')) : null
                )

                when.statustranslations = whenElement.selectNodes('.//*[local-name()="statustranslation"]').collect { Element statusTranslationElement ->
                    return new Calendar.StatusTranslation(
                            lang: statusTranslationElement.attributeValue('lang'),
                            text: statusTranslationElement.text,
                    )
                }
                when.extrainformations = whenElement.selectNodes('.//*[local-name()="extrainformation"]').collect { Element extraInformationElement ->
                    return new Calendar.ExtraInformation(
                            lang: extraInformationElement.attributeValue('lang'),
                            text: extraInformationElement.text,
                    )
                }

                return when
            }

            return open
        }

        return patternDate
    }

    static Calendar.ExceptionDate parseExceptionDate(Element exceptionDateElement) {
        Calendar.ExceptionDate exceptionDate = new Calendar.ExceptionDate(
            date: exceptionDateElement.attributeValue('date') ? dayFormatter.parseDateTime(exceptionDateElement.attributeValue('date')) : null
        )

        exceptionDate.whens = exceptionDateElement.selectNodes('*[local-name()="when"]').collect { Element whenElement ->
            When when = new When(
                timeend: whenElement.attributeValue('timeend'),
                timestart: whenElement.attributeValue('timestart'),
                status: whenElement.attributeValue('status') ? When.Status.valueOf(whenElement.attributeValue('status')) : null
            )

            when.statustranslations = whenElement.selectNodes('.//*[local-name()="statustranslation"]').collect { Element statusTranslationElement ->
                return new Calendar.StatusTranslation(
                    lang: statusTranslationElement.attributeValue('lang'),
                    text: statusTranslationElement.text,
                )
            }
            when.extrainformations = whenElement.selectNodes('.//*[local-name()="extrainformation"]').collect { Element extraInformationElement ->
                return new Calendar.ExtraInformation(
                    lang: extraInformationElement.attributeValue('lang'),
                    text: extraInformationElement.text,
                )
            }

            return when
        }

        return exceptionDate
    }

    static TRCItemDetail parseTRCItemDetail(Element TRCItemDetailElement) {
        TRCItemDetail trcItemDetail = new TRCItemDetail(
            lang: TRCItemDetailElement.attributeValue("lang"),
            longdescription: TRCItemDetailElement.element("longdescription")?.text,
            shortdescription: TRCItemDetailElement.element("shortdescription")?.text,
            title: TRCItemDetailElement.element("title")?.text
        )

        return trcItemDetail
    }

    static Location parseLocation(Element locationElement) {
        Location location = new Location(
            address: parseAddress((Element) locationElement.selectSingleNode('*[local-name()="address"]')),
            label: locationElement.element('label')?.getText()
        )

        if (locationElement.element('locationitem')) {
            Element locationItemElement = locationElement.element('locationitem')

            location.locationItem = new Location.LocationItem(
                trcid: locationItemElement.attributeValue('trcid'),
                text: locationItemElement.getText()
            )
        }
        return location
    }

    static Contactinfo parseContactInfo(Element contactInfoElement) {
        Address address = parseAddress(contactInfoElement.element('address'))
        Contactinfo.Mail mail = parseMail(contactInfoElement.element('mail'))
        Contactinfo.Phone phone = parsePhone(contactInfoElement.element('phone'))
        Contactinfo.Fax fax = parseFax(contactInfoElement.element('fax'))
        List<Contactinfo.Url> urls = contactInfoElement.selectNodes('*[local-name()="url"]').collect { Element urlElement ->
            return parseUrl(urlElement)
        }

        return new Contactinfo(
            mail: mail,
            phone: phone,
            fax: fax,
            address: address,
            urls: urls
        )
    }

    static List<File> parseMedia(Element mediaElement) {
        if (!mediaElement) return null

        mediaElement.selectNodes('*[local-name()="file"]').collect { Element fileElement ->
            File.Title title = null
            if (fileElement.element('title')) {
                title = new File.Title(
                    label: fileElement.attributeValue("label"),
                    titleTranslations: fileElement.selectNodes('*[local-name()="title"]/*[local-name()="titletranslation"]').collect { Element translationElement ->
                        return new File.Title.TitleTranslation(
                            label: translationElement.attributeValue('label'),
                            lang: translationElement.attributeValue('lang')
                        )
                    }
                )
            }

            return new File(
                trcid: fileElement.attributeValue('trcid'),
                main: fileElement.attributeValue('main') ? Boolean.parseBoolean(fileElement.attributeValue('main')) : null,
                copyright: fileElement.element('copyright')?.getText(),
                filename: fileElement.element('filename')?.getText(),
                hlink: fileElement.element('hlink')?.getText(),
                targetLanguage: fileElement.element('targetLanguage')?.getText(),
                filetype: fileElement.element('filetype') ? File.FileType.valueOf(fileElement.element('filetype')?.getText()) : null,
                mediatype: fileElement.element('mediatype') ? File.MediaType.valueOf(fileElement.element('mediatype')?.getText()) : null,
                title: title
            )
        }
    }

    static TRCItemCategories parseCategories(Element categoriesElement) {
        TRCItemCategories trcItemCategories = new TRCItemCategories(
            soldout: categoriesElement.attributeValue('soldout') ? Boolean.parseBoolean(categoriesElement.attributeValue('soldout')) : null,
            canceled: categoriesElement.attributeValue('canceled') ? Boolean.parseBoolean(categoriesElement.attributeValue('canceled')) : null,
        )

        trcItemCategories.categories = categoriesElement.selectNodes('*[local-name()="categories"]/*[local-name()="category"]').collect { Element categoryElement ->
            return new TRCItemCategories.Category(
                catid: categoryElement.attributeValue('catid'),
                valueid: categoryElement.attributeValue('valueid'),
                value: categoryElement.attributeValue('value'),
                datatype: categoryElement.attributeValue('datatype') ? TRCItemCategories.Category.DataType.valueOf(categoryElement.attributeValue('datatype')) : null,
                categoryTranslations: categoryElement.selectNodes('*[local-name()="categorytranslation"]').collect { Element categoryTranslationElement ->
                    return new TRCItemCategories.CategoryTranslation(
                        lang: categoryTranslationElement.attributeValue('lang'),
                        label: categoryTranslationElement.attributeValue('label '),
                        unit: categoryTranslationElement.attributeValue('unit'),
                        explanation: categoryTranslationElement.attributeValue('explanation'),
                        value: categoryTranslationElement.attributeValue('value')
                    )
                }
            )
        }
        trcItemCategories.types = categoriesElement.selectNodes('*[local-name()="types"]/*[local-name()="type"]').collect { Element typeElement ->
            return new TRCItemCategories.Type(
                catid: typeElement.attributeValue('catid'),
                isDefault: typeElement.attributeValue('default') ? Boolean.parseBoolean(typeElement.attributeValue('isDefault')) : null
            )
        }
        return trcItemCategories
    }

    static PriceElement parsePriceElement(Element priceElement) {
        Element priceValueElement = priceElement.element('pricevalues')
        Element priceDescriptionElement = priceElement.element('pricedescription')
        Element priceCommentsElement = priceElement.element('pricecomments')

        PriceElement newPriceElement = new PriceElement(
            freeentrance: priceElement.element('freeentrance')?.getText()
        )

        if (priceValueElement) {
            newPriceElement.priceValue = new PriceElement.PriceValue(
                from: priceValueElement.element('pricefrom')?.getText() ? Double.parseDouble(priceValueElement.element('pricefrom')?.getText()) : null,
                until: priceValueElement.element('priceuntil')?.getText() ? Double.parseDouble(priceValueElement.element('priceuntil')?.getText()) : null
            )
        }

        if (priceDescriptionElement) {
            newPriceElement.description = new PriceElement.Description(
                value: priceDescriptionElement.attributeValue("value") ? PriceElement.Description.PriceDescriptionValue.valueOf(priceDescriptionElement.attributeValue("value")) : null,
                descriptionTranslations: priceDescriptionElement.selectNodes('*[local-name()="pricedescriptiontranslation"]').collect { Element descriptionTranslation ->
                    return new PriceElement.DescriptionTranslation(
                        lang: descriptionTranslation.attributeValue('lang'),
                        text: descriptionTranslation.getText()
                    )
                }
            )
        }

        if (priceCommentsElement) {
            newPriceElement.comments = priceCommentsElement.selectNodes('*[local-name()="pricecomment"]').collect { Element pricecommentElement ->
                return new PriceElement.Comment(
                    lang: pricecommentElement.attributeValue('lang'),
                    text: pricecommentElement.getText()
                )
            }
        }

        return newPriceElement
    }

    static ExtraPriceInformation parseExtraPriceInformationElement(Element extraPriceInformationElement) {
        return new ExtraPriceInformation(
                lang: extraPriceInformationElement.attributeValue('lang'),
                text: extraPriceInformationElement.getText()
        )
    }

    static Address parseAddress(Element element) {
        if (!element) return null

        return new Address(
            main: element.attributeValue('main') ? Boolean.parseBoolean(element.attributeValue('main')) : null,
            reservation: element.attributeValue('reservation') ? Boolean.parseBoolean(element.attributeValue('reservation')) : null,
            city: element.selectSingleNode('*[local-name()="physical"]/*[local-name()="city"]')?.getText(),
            citytrcid: element.selectSingleNode('*[local-name()="physical"]/*[local-name()="city"]/@trcid')?.getText(),
            country: element.selectSingleNode('*[local-name()="physical"]/*[local-name()="country"]')?.getText(),
            housenr: element.selectSingleNode('*[local-name()="physical"]/*[local-name()="housenr"]')?.getText(),
            street: element.selectSingleNode('*[local-name()="physical"]/*[local-name()="street"]')?.getText(),
            streettrcid: element.selectSingleNode('*[local-name()="physical"]/*[local-name()="street"]/@trcid')?.getText(),
            zipcode: element.selectSingleNode('*[local-name()="physical"]/*[local-name()="zipcode"]')?.getText()
        )
    }

    static Contactinfo.Mail parseMail(Element element) {
        if (!element) return null

        return new Contactinfo.Mail(
            email: element.attributeValue('value'),
            descriptioncode: element.attributeValue('descriptioncode'),
            reservations: element.attributeValue('reservations') ? Boolean.parseBoolean(element.attributeValue('reservations')) : null,
            descriptionTranslations: element.selectNodes('*[local-name()="descriptiontranslation"]').collect { Element descriptionTranslationElement ->
                return new Contactinfo.DescriptionTranslation(
                    label: descriptionTranslationElement.attributeValue('label'),
                    lang: descriptionTranslationElement.attributeValue('lang')
                )
            }
        )
    }

    static Contactinfo.Phone parsePhone(Element element) {
        if (!element) return null

        return new Contactinfo.Phone(
            number: element.attributeValue('value'),
            descriptioncode: element.attributeValue('descriptioncode'),
            reservations: element.attributeValue('reservations') ? Boolean.parseBoolean(element.attributeValue('reservations')) : null,
            descriptionTranslations: element.selectNodes('*[local-name()="descriptiontranslation"]').collect { Element descriptionTranslationElement ->
                return new Contactinfo.DescriptionTranslation(
                        label: descriptionTranslationElement.attributeValue('label'),
                        lang: descriptionTranslationElement.attributeValue('lang')
                )
            }
        )
    }

    static Contactinfo.Fax parseFax(Element element) {
        if (!element) return null

        return new Contactinfo.Fax(
            number: element.attributeValue('value'),
            descriptioncode: element.attributeValue('descriptioncode'),
            reservations: element.attributeValue('reservations') ? Boolean.parseBoolean(element.attributeValue('reservations')) : null,
            descriptionTranslations: element.selectNodes('*[local-name()="descriptiontranslation"]').collect { Element descriptionTranslationElement ->
                return new Contactinfo.DescriptionTranslation(
                        label: descriptionTranslationElement.attributeValue('label'),
                        lang: descriptionTranslationElement.attributeValue('lang')
                )
            }
        )
    }

    static Contactinfo.Url parseUrl(Element element) {
        if (!element) return null

        return new Contactinfo.Url(
            url: element.attributeValue('value') ? new URL(element.attributeValue('value')) : null,
            descriptioncode: element.attributeValue('descriptioncode'),
            reservations: element.attributeValue('reservations') ? Boolean.parseBoolean(element.attributeValue('reservations')) : null,
            targetLanguage: element.attributeValue('targetLanguage'),
            urlServiceType: element.attributeValue('urlServiceType') ? Contactinfo.Url.URLServiceType.valueOf(element.attributeValue('urlServiceType')) : null,
            descriptionTranslations: element.selectNodes('*[local-name()="descriptiontranslation"]').collect { Element descriptionTranslationElement ->
                return new Contactinfo.DescriptionTranslation(
                        label: descriptionTranslationElement.attributeValue('label'),
                        lang: descriptionTranslationElement.attributeValue('lang')
                )
            }
        )
    }

    static DateTimeFormatter dateTimeFormatterNoTimeZone = DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss")
    static DateTimeFormatter dateTimeMillisFormatterNoTimeZone = DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss.SSS")

    static DateTimeFormatter dateTimeFormatterWithTimeZone = ISODateTimeFormat.dateTimeNoMillis()
    static DateTimeFormatter dateTimeMillisFormatterWithTimeZone = ISODateTimeFormat.dateTime()

    private static DateTime parseDate(String date) {
        if (!date?.trim()) {
            throw new IllegalArgumentException("Date is empty")
        }

        DateTime parsedDate = [dateTimeFormatterNoTimeZone, dateTimeMillisFormatterNoTimeZone, dateTimeFormatterWithTimeZone, dateTimeMillisFormatterWithTimeZone].find { DateTimeFormatter dateTimeFormatter ->
            try {
                if(dateTimeFormatter.parseDateTime(date)) {
                    return dateTimeFormatter
                }
                return null
            } catch (IllegalArgumentException iae) {
                return null
            }
        }?.parseDateTime(date)

        if (!parsedDate) {
            log.warn("Cannot parse date: ${date}")
//            throw new RuntimeException()
        }
        return parsedDate
    }
}