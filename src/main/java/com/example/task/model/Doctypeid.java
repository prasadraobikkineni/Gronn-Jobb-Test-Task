package com.example.task.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Doctypeid {
    private String scheme;
    private String value;
    private String displayname;
    private String deprecated;
    @JacksonXmlProperty(localName = "non-standard")
    private String nonstandard;
}
