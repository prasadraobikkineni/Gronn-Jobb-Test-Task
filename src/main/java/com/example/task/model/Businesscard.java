package com.example.task.model;

import com.example.task.domain.Entity;
import com.example.task.domain.Participant;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@XmlRootElement
public class Businesscard {
    private Participant participant;
    private List<Entity> entity;
    private List<Doctypeid> doctypeid;

}