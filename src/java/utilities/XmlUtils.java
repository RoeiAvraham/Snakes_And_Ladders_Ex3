/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.io.File;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;
import xmlPackage.Snakesandladders;

/**
 *
 * @author Anat
 */
public class XmlUtils
{
    public static Snakesandladders loadGameFromXml(File file, ServletContext servletContext) throws JAXBException, SAXException, IOException
    {
        Source xmlFile = new StreamSource(file);
        JAXBContext jc = JAXBContext.newInstance(Snakesandladders.class);
        Unmarshaller u = jc.createUnmarshaller();
        SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sFactory.newSchema(new File(servletContext.getRealPath("/") + "snakesandladders.xsd"));
        Validator validator = schema.newValidator();
        validator.validate(xmlFile);
        Snakesandladders sal_xml = (Snakesandladders) u.unmarshal(xmlFile);
        return sal_xml;
    }
}
