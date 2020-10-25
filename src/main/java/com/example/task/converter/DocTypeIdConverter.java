package com.example.task.converter;

import com.example.task.domain.DocTypeId;
import com.example.task.model.Doctypeid;
import org.springframework.stereotype.Component;

@Component
public class DocTypeIdConverter {

    public DocTypeId apply(Doctypeid doctypeidXmlElement) {
        DocTypeId docTypeId = new DocTypeId();
        docTypeId.setDeprecated(doctypeidXmlElement.getDeprecated());
        docTypeId.setDisplayName(doctypeidXmlElement.getDisplayname());
        docTypeId.setScheme(doctypeidXmlElement.getScheme());
        docTypeId.setValue(doctypeidXmlElement.getValue());
        docTypeId.setNonStandard(doctypeidXmlElement.getNonstandard());
        return docTypeId;
    }
}
