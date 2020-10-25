package com.example.task.service;

import com.example.task.converter.BusinessCardConverter;
import com.example.task.model.Root;
import com.example.task.repository.BusinessCardElasticSaveRepository;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndexDataService {

    @Value("${indexable}")
    private Boolean indexable;

    private final BusinessCardElasticSaveRepository businessCardElasticSaveRepository;
    private final BusinessCardConverter businessCardConverter;

    public void index() {
        try {
            if (indexable) {
                JacksonXmlModule module = new JacksonXmlModule();
                module.setDefaultUseWrapper(false);
                XmlMapper xmlMapper = new XmlMapper(module);
                xmlMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                Root rootDto = xmlMapper.readValue(getDataSet(), Root.class);
                rootDto.getBusinesscard().forEach(x -> businessCardElasticSaveRepository.save(businessCardConverter.apply(x)));
                System.out.println("ALL XML DATA INDEXED TO ELASTICSEARCH");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private String getDataSet() throws IOException {
        File file = ResourceUtils.getFile("classpath:directory-export-business-cards.xml");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }
        // delete the last new line separator
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        reader.close();

        return stringBuilder.toString();
    }
}
