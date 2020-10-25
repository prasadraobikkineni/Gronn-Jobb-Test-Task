package com.example.task.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessCard {

    @Id
    private String businessCardId;
    private Participant participant;
    private List<Entity> entityList;
    private List<DocTypeId> docTypeIdList;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BusinessCard payment = (BusinessCard) o;
        return Objects.equals(businessCardId, payment.businessCardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessCardId);
    }
}

