package com.example.task.model;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@XmlRootElement
public class Root {

    @XmlElement
    private List<Businesscard> businesscard;

    @XmlAttribute
    private String version;

    @XmlAttribute
    private String creationdt;
}
