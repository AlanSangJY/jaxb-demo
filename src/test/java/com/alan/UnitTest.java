package com.alan;

import com.alan.entity.schemas.Person;
import com.alan.schemas.Book;
import com.alan.schemas.ObjectFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import javax.xml.bind.*;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

public class UnitTest {

    @Test
    void unmarshalDocument() throws JAXBException, JsonProcessingException {
        //JAXBContext jaxbContext = JAXBContext.newInstance("com.alan.schemas");
        //JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
        JAXBContext jaxbContext = JAXBContext.newInstance(Book.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        Book book = (Book) unmarshaller.unmarshal(new File("D:\\code\\icode\\jaxb-demo\\src\\main\\resources\\schemas\\xml\\book.xml"));
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

}
