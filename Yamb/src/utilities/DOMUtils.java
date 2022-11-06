/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import hr.algebra.model.Field;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 *
 * @author lukak
 */
public class DOMUtils {

    

    public static void saveSteps(List<Field> steps) {
        try {
            Document document = createDocument("fields");
            steps.forEach(s -> document.getDocumentElement().appendChild(createStepElement(s, document))); 
            saveDocument(document, "dom.xml");
        } catch (TransformerException | ParserConfigurationException ex) {
            Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static Document createDocument(String root) throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        DOMImplementation domImplementation = builder.getDOMImplementation();
        return domImplementation.createDocument(null, root, null);
    }

    private static Node createStepElement(Field field, Document document) {
        Element element = document.createElement("field");
        element.appendChild(createElement(document, "name", field.getName().toString()));
        element.appendChild(createElement(document, "value", field.getValue().toString()));
        element.appendChild(createElement(document, "points", String.valueOf(field.getPoints())));
        return element;
    }
    
    private static Node createElement(Document document, String tagName, String data) {
        Element element = document.createElement(tagName);
        Text text = document.createTextNode(data);
        element.appendChild(text);
        return element;
    }

    private static void saveDocument(Document document, String fileName) throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(document), new StreamResult(new File(fileName)));
    }
    
    private static Document createDocument(File file) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        return document;
    }

    public static List<Field> loadSteps() {        
        List<Field> fields = new ArrayList<Field>();
        try { 
            Document document = createDocument(new File("dom.xml"));
            NodeList nodes = document.getElementsByTagName("field"); 
            for (int i = 0; i < nodes.getLength(); i++) {
                fields.add(processStepNode((Element) nodes.item(i)));
            }      
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            Logger.getLogger(DOMUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         return fields;
    }

    private static Field processStepNode(Element element) { 
        String name = element.getElementsByTagName("name").item(0).getTextContent();
        String value = element.getElementsByTagName("value").item(0).getTextContent();
        int points = Integer.parseInt(element.getElementsByTagName("points").item(0).getTextContent());
        
        return new Field(name, value, points);

    }

    
    

}
