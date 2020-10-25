package com.example.task.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DocTypeId {

    private String scheme;
    private String value;
    @JacksonXmlProperty(localName = "displayname")
    private String displayName;
    private String deprecated;
    @JacksonXmlProperty(localName = "non-standard")
    private String nonStandard;
}
