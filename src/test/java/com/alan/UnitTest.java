package com.alan;

import com.alan.entity.schemas.Person;
import com.alan.schemas.Book;
import com.alan.schemas.ObjectFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.bind.util.JAXBSource;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

public class UnitTest {

    private final static String BOOK_XML_PATH = "D:\\code\\icode\\jaxb-demo\\src\\main\\resources\\schemas\\xml\\book.xml";

    private final static String BOOK_XSD_PATH = "D:\\code\\icode\\jaxb-demo\\src\\main\\resources\\schemas\\xsd\\book.xsd";

    @Test
    void unmarshalDocument() throws JAXBException, JsonProcessingException {
        //JAXBContext jaxbContext = JAXBContext.newInstance("com.alan.schemas");
        //JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        JAXBContext jaxbContext = JAXBContext.newInstance(Book.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Book book = (Book) unmarshaller.unmarshal(new File(BOOK_XML_PATH));
        System.out.println(new ObjectMapper().writeValueAsString(book));
    }

    @Test
    void createEntityViaObjectFactory() throws JsonProcessingException {
        Book book = new ObjectFactory().createBook();
        book.setName("n");
        book.setAuth("a");
        System.out.println(new ObjectMapper().writeValueAsString(book));
    }

    @Test
    void marshalObject() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        Book book = new Book();
        book.setName("123456");
        book.setAuth("aaaaaaa");
        StringWriter stringWriter = new StringWriter();
        marshaller.marshal(book, stringWriter);
        System.out.println(stringWriter);
    }

    @Test
    void generateSchema() throws JAXBException, IOException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Person.class);
        jaxbContext.generateSchema(new SchemaOutputResolver() {
            @Override
            public Result createOutput(String namespaceUri, String suggestedFileName) {
                return new StreamResult(new File(suggestedFileName));
            }
        });
    }

    @Test
    void validator() throws JAXBException, SAXException, IOException {
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new File(BOOK_XSD_PATH));
        Validator validator = schema.newValidator();
        JAXBContext jaxbContext = JAXBContext.newInstance(Book.class);
        Book book = new Book();
        book.setName("123456");
        validator.validate(new JAXBSource(jaxbContext, book));
    }

    @Test
    void validatorWithErrorHandle() throws JAXBException, SAXException, IOException {
        Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(BOOK_XSD_PATH));
        Validator validator = schema.newValidator();
        JAXBContext jaxbContext = JAXBContext.newInstance(Book.class);
        Book book = new Book();
        book.setName("123456");
        validator.setErrorHandler(new ErrorHandler() {
            @Override
            public void warning(SAXParseException exception) throws SAXException {
                System.out.println("My warning");
                exception.printStackTrace();
            }

            @Override
            public void error(SAXParseException exception) throws SAXException {
                System.out.println("My error");
                exception.printStackTrace();
            }

            @Override
            public void fatalError(SAXParseException exception) throws SAXException {
                System.out.println("My fatal error");
                exception.printStackTrace();
            }
        });
        validator.validate(new JAXBSource(jaxbContext, book));
    }

    @Test
    void validateWhenUnmarshal() throws JAXBException, SAXException, JsonProcessingException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Book.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(BOOK_XSD_PATH));
        unmarshaller.setSchema(schema);
        unmarshaller.setEventHandler(event -> {
            System.out.println(event.getMessage());
            return true;
        });
        Object unmarshal = unmarshaller.unmarshal(new File(BOOK_XML_PATH));
        System.out.println(new ObjectMapper().writeValueAsString(unmarshal));
    }

    @Test
    void validateWhenMarshal() throws JAXBException, SAXException, JsonProcessingException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Book.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        Schema schema = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI).newSchema(new File(BOOK_XSD_PATH));
        marshaller.setSchema(schema);
        marshaller.setEventHandler(event -> {
            System.out.println(event.getMessage());
            return true;
        });
        Book book = new Book();
        book.setAuth("aaa");
        marshaller.marshal(book, System.out);
    }
}
