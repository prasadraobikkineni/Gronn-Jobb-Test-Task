package com.example.task.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Contact {

    private String email;
    private String name;
    @JacksonXmlProperty(localName = "phonenumber")
    private String phoneNumber;
    private String type;
}
