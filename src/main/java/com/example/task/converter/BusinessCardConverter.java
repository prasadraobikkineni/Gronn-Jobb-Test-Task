package com.example.task.converter;

import com.example.task.domain.BusinessCard;
import com.example.task.model.Businesscard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BusinessCardConverter {

    private final DocTypeIdConverter docTypeIdConverter;

    public BusinessCard apply(Businesscard businessCardXmlModel) {
        BusinessCard businessCard = new BusinessCard();
        businessCard.setBusinessCardId(businessCardXmlModel.getParticipant().getValue());
        businessCard.setDocTypeIdList(Objects.nonNull(businessCardXmlModel.getDoctypeid()) ? businessCardXmlModel.getDoctypeid().stream().map(docTypeIdConverter::apply).collect(Collectors.toList()) : Collections.emptyList());
        businessCard.setParticipant(businessCardXmlModel.getParticipant());
        businessCard.setEntityList(businessCardXmlModel.getEntity());
        return businessCard;
    }
}
