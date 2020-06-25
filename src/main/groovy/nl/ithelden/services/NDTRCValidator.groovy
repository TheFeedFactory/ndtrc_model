package nl.ithelden.services

import nl.ithelden.model.ndtrc.TRCItem
import org.dom4j.Element
import org.dom4j.io.OutputFormat
import org.dom4j.io.XMLWriter
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXException
import org.xml.sax.SAXParseException

import javax.xml.transform.Source
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.Schema
import javax.xml.validation.SchemaFactory
import javax.xml.validation.Validator

class NDTRCValidator implements ErrorHandler {
    List<String> errors = []
    boolean isValid = true

    boolean validate(TRCItem trcItem) {
        StringWriter stringWriter = new StringWriter()

        Element content = NDTRCSerializer.serializeTRCItem(trcItem)

        if (!content) return false

        OutputFormat format = OutputFormat.createPrettyPrint()
        XMLWriter writer = new XMLWriter( stringWriter, format )
        writer.write( content )
        writer.close()

        return validate(stringWriter.toString())
    }

    boolean validate(String xmlString) {
        if (!xmlString) return false

        SchemaFactory factory =
                SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema")

        String schemaStr = NDTRCValidator.class.getResourceAsStream("/schema/ndtrc.xsd").getText("UTF-16")
        Schema schema = factory.newSchema(
            new StreamSource(
                new StringReader(schemaStr)
            )
        )

        Validator validator = schema.newValidator()

        Source source = new StreamSource(new StringReader(xmlString))

        try {
            validator.setErrorHandler(this)
            validator.validate(source)
            return this.isValid
        } catch (SAXException ex) {
            errors.add("ERROR: ${ex.getMessage()}")
            this.isValid = false
            return this.isValid
        }
    }

    @Override
    void warning(SAXParseException exception) throws SAXException {
//        errors.add("Warning: line ${exception.lineNumber}, ${exception.message}")
    }

    @Override
    void error(SAXParseException exception) throws SAXException {
        errors.add("Error: line ${exception.lineNumber}, ${exception.message}")
        isValid = false
    }

    @Override
    void fatalError(SAXParseException exception) throws SAXException {
        errors.add("Fatal: line ${exception.lineNumber}, ${exception.message}")
        isValid = false
    }
}