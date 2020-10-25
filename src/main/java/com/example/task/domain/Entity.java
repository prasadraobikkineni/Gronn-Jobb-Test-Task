package com.example.task.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Entity {

    @JacksonXmlProperty(localName = "countrycode")
    private String countryCode;
    private Name name;
    @JacksonXmlProperty(localName = "regdate")
    private Date regDate;
    private Contact contact;
    private Id id;
    @JacksonXmlProperty(localName = "geoinfo")
    private String geoInfo;
    @JacksonXmlProperty(localName = "website")
    private String webSite;
    @JacksonXmlProperty(localName = "additionalinfo")
    private String additionalInfo;
}
