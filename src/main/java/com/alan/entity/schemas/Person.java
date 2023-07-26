package com.alan.entity.schemas;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "name",
        "age"
})
@XmlRootElement(name = "Person", namespace = "http:www.alan.com/schemas")
public class Person {

    @XmlElement(name = "Name", namespace = "http:www.alan.com/schemas", required = true)
    protected String name;
    @XmlElement(name = "Age", namespace = "http:www.alan.com/schemas")
    protected Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
