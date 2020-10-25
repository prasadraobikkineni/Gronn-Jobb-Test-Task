package com.example.task.repository;

import com.example.task.domain.BusinessCard;
import com.example.task.domain.DocTypeId;
import com.example.task.domain.Entity;
import com.example.task.domain.Name;
import com.example.task.domain.Participant;
import com.example.task.model.request.BusinessCardFilterRequest;
import com.example.task.model.response.BusinessCardListResult;
import com.example.task.model.response.BusinessCardResult;
import com.example.task.service.IndexDataService;
import com.example.task.testutils.FileLoader;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.PutIndexTemplateRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(initializers = BusinessCardElasticSearchRepositoryTest.Initializer.class)
@Slf4j
public class BusinessCardElasticSearchRepositoryTest {
    public static final String INDEX = "business_card";
    public static final String V1 = "business_card_v1";
    @ClassRule
    public static ElasticsearchContainer es = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:7.4.2").withExposedPorts(9200);

    @Autowired
    private BusinessCardElasticSearchRepository businessCardElasticSearchRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestHighLevelClient elasticSearchClient;

    @MockBean
    private IndexDataService indexDataService;

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues values = TestPropertyValues.of(
                    "elasticsearch.urls=" + es.getContainerIpAddress(),
                    "elasticsearch.port=" + es.getMappedPort(9200)
            );
            values.applyTo(configurableApplicationContext);
        }
    }

    @Before
    public void init() {
        deleteIndex();
        try {
            PutIndexTemplateRequest templateRequest = new PutIndexTemplateRequest("business_card_template");
            String templateSource = FileLoader.read("classpath:elasticsearch.template/business_card_index_template.json");
            templateRequest.source(templateSource, XContentType.JSON);
            Settings settings = Settings.builder()
                    .put(templateRequest.settings())
                    .put("refresh_interval", "1s")
                    .build();
            templateRequest.settings(settings);
            elasticSearchClient.indices().putTemplate(templateRequest, RequestOptions.DEFAULT);

            createIndex(INDEX);
            createAliasForBusinessCard();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while creating index via template", e);
        }
    }

    private void createIndex(String indexName) throws IOException {
        CreateIndexRequest index = new CreateIndexRequest(indexName);
        elasticSearchClient.indices().create(index, RequestOptions.DEFAULT);
    }

    private void deleteIndex() {
        try {
            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(INDEX);
            elasticSearchClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);

        } catch (Exception e) {
            log.error("Error on deleting 'business card' index", e);
        }
    }

    private void indexToBusinessCard(BusinessCard... businessCardList) {
        try {
            for (BusinessCard businessCard : businessCardList) {
                String businessCardId = businessCard.getParticipant().getValue();
                IndexRequest request = new IndexRequest(
                        INDEX
                ).id(businessCardId).source(objectMapper.writeValueAsString(businessCard), XContentType.JSON);
                elasticSearchClient.index(request, RequestOptions.DEFAULT);
            }
            TimeUnit.SECONDS.sleep(2L);
        } catch (Exception ex) {
            throw new RuntimeException("Index failed", ex);
        }
    }

    private void createAliasForBusinessCard() throws IOException {
        IndicesAliasesRequest indicesAliasesRequest = new IndicesAliasesRequest();
        IndicesAliasesRequest.AliasActions aliasAction1 =
                new IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
                        .index(INDEX)
                        .alias(V1);

        indicesAliasesRequest.addAliasAction(aliasAction1);

        elasticSearchClient.indices().updateAliases(indicesAliasesRequest, RequestOptions.DEFAULT);
    }

    @AfterClass
    public static void tearDown() {
        es.stop();
    }

    @Test
    public void it_should_find_by_id() {
        //given
        Participant participant1 = new Participant();
        participant1.setValue("9906:it13039021004");
        participant1.setScheme("participant1Scheme");
        Entity entity1 = new Entity();
        entity1.setCountryCode("countryCode1");
        entity1.setName(new Name("BAXALTA ITALY SRL", "language1"));
        entity1.setRegDate(DateTime.now().toDate());
        DocTypeId docTypeId1 = new DocTypeId();
        docTypeId1.setDeprecated("deprecated1");
        docTypeId1.setDisplayName("displayName1");
        docTypeId1.setScheme("docTypeIdScheme1");
        docTypeId1.setValue("docTypeIdValue1");

        BusinessCard businessCard1 = new BusinessCard();
        businessCard1.setParticipant(participant1);
        businessCard1.setEntityList(Collections.singletonList(entity1));
        businessCard1.setDocTypeIdList(Collections.singletonList(docTypeId1));

        Participant participant2 = new Participant();
        participant2.setValue("0195:sguen199408213d");
        participant2.setScheme("participant2Scheme");
        Entity entity2 = new Entity();
        entity2.setCountryCode("countryCode2");
        entity2.setName(new Name("BAROSA BAR PTE. LTD.", "language2"));
        entity2.setRegDate(DateTime.now().toDate());
        DocTypeId docTypeId2 = new DocTypeId();
        docTypeId2.setDeprecated("deprecated2");
        docTypeId2.setDisplayName("displayName2");
        docTypeId2.setScheme("docTypeIdScheme2");
        docTypeId2.setValue("docTypeIdValue2");

        BusinessCard businessCard2 = new BusinessCard();
        businessCard2.setParticipant(participant2);
        businessCard2.setEntityList(Collections.singletonList(entity2));
        businessCard2.setDocTypeIdList(Collections.singletonList(docTypeId2));

        Participant participant3 = new Participant();
        participant3.setValue("0195:sguen199506857r");
        participant3.setScheme("participant3Scheme");
        Entity entity3 = new Entity();
        entity3.setCountryCode("countryCode3");
        entity3.setName(new Name("THE SUBSTATION LTD", "language3"));
        entity3.setRegDate(DateTime.now().toDate());
        DocTypeId docTypeId3 = new DocTypeId();
        docTypeId3.setDeprecated("deprecated3");
        docTypeId3.setDisplayName("displayName3");
        docTypeId3.setScheme("docTypeIdScheme3");
        docTypeId3.setValue("docTypeIdValue3");

        BusinessCard businessCard3 = new BusinessCard();
        businessCard3.setParticipant(participant3);
        businessCard3.setEntityList(Collections.singletonList(entity3));
        businessCard3.setDocTypeIdList(Collections.singletonList(docTypeId3));

        indexToBusinessCard(businessCard1, businessCard2, businessCard3);

        //when
        BusinessCardResult result = businessCardElasticSearchRepository.getById("0195:sguen199408213d");

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);
    }

    @Test
    public void it_should_search_by_name() {
        //given
        BusinessCardFilterRequest businessCardFilterRequest = new BusinessCardFilterRequest();
        businessCardFilterRequest.setSearchName("BAROSA");

        Participant participant1 = new Participant();
        participant1.setValue("9906:it13039021004");
        participant1.setScheme("participant1Scheme");
        Entity entity1 = new Entity();
        entity1.setCountryCode("countryCode1");
        entity1.setName(new Name("BAXALTA ITALY SRL", "language1"));
        entity1.setRegDate(DateTime.now().toDate());
        DocTypeId docTypeId1 = new DocTypeId();
        docTypeId1.setDeprecated("deprecated1");
        docTypeId1.setDisplayName("displayName1");
        docTypeId1.setScheme("docTypeIdScheme1");
        docTypeId1.setValue("docTypeIdValue1");

        BusinessCard businessCard1 = new BusinessCard();
        businessCard1.setParticipant(participant1);
        businessCard1.setEntityList(Collections.singletonList(entity1));
        businessCard1.setDocTypeIdList(Collections.singletonList(docTypeId1));

        Participant participant2 = new Participant();
        participant2.setValue("0195:sguen199408213d");
        participant2.setScheme("participant2Scheme");
        Entity entity2 = new Entity();
        entity2.setCountryCode("countryCode2");
        entity2.setName(new Name("BAROSA BAR PTE. LTD.", "language2"));
        entity2.setRegDate(DateTime.now().toDate());
        DocTypeId docTypeId2 = new DocTypeId();
        docTypeId2.setDeprecated("deprecated2");
        docTypeId2.setDisplayName("displayName2");
        docTypeId2.setScheme("docTypeIdScheme2");
        docTypeId2.setValue("docTypeIdValue2");

        BusinessCard businessCard2 = new BusinessCard();
        businessCard2.setParticipant(participant2);
        businessCard2.setEntityList(Collections.singletonList(entity2));
        businessCard2.setDocTypeIdList(Collections.singletonList(docTypeId2));

        Participant participant3 = new Participant();
        participant3.setValue("0195:sguen199506857r");
        participant3.setScheme("participant3Scheme");
        Entity entity3 = new Entity();
        entity3.setCountryCode("countryCode3");
        entity3.setName(new Name("THE SUBSTATION LTD", "language3"));
        entity3.setRegDate(DateTime.now().toDate());
        DocTypeId docTypeId3 = new DocTypeId();
        docTypeId3.setDeprecated("deprecated3");
        docTypeId3.setDisplayName("displayName3");
        docTypeId3.setScheme("docTypeIdScheme3");
        docTypeId3.setValue("docTypeIdValue3");

        BusinessCard businessCard3 = new BusinessCard();
        businessCard3.setParticipant(participant3);
        businessCard3.setEntityList(Collections.singletonList(entity3));
        businessCard3.setDocTypeIdList(Collections.singletonList(docTypeId3));

        indexToBusinessCard(businessCard1, businessCard2, businessCard3);

        //when
        BusinessCardListResult result = businessCardElasticSearchRepository.searchByName(businessCardFilterRequest);

        //then
        assertThat(result.getBusinessCardList().size()).isEqualTo(2);
    }

    @Test
    public void it_should_get_by_name() {
        //given
        Participant participant1 = new Participant();
        participant1.setValue("9906:it13039021004");
        participant1.setScheme("participant1Scheme");
        Entity entity1 = new Entity();
        entity1.setCountryCode("countryCode1");
        entity1.setName(new Name("BAXALTA ITALY SRL", "language1"));
        entity1.setRegDate(DateTime.now().toDate());
        DocTypeId docTypeId1 = new DocTypeId();
        docTypeId1.setDeprecated("deprecated1");
        docTypeId1.setDisplayName("displayName1");
        docTypeId1.setScheme("docTypeIdScheme1");
        docTypeId1.setValue("docTypeIdValue1");

        BusinessCard businessCard1 = new BusinessCard();
        businessCard1.setParticipant(participant1);
        businessCard1.setEntityList(Collections.singletonList(entity1));
        businessCard1.setDocTypeIdList(Collections.singletonList(docTypeId1));

        Participant participant2 = new Participant();
        participant2.setValue("0195:sguen199408213d");
        participant2.setScheme("participant2Scheme");
        Entity entity2 = new Entity();
        entity2.setCountryCode("countryCode2");
        entity2.setName(new Name("BAROSA BAR PTE. LTD.", "language2"));
        entity2.setRegDate(DateTime.now().toDate());
        DocTypeId docTypeId2 = new DocTypeId();
        docTypeId2.setDeprecated("deprecated2");
        docTypeId2.setDisplayName("displayName2");
        docTypeId2.setScheme("docTypeIdScheme2");
        docTypeId2.setValue("docTypeIdValue2");

        BusinessCard businessCard2 = new BusinessCard();
        businessCard2.setParticipant(participant2);
        businessCard2.setEntityList(Collections.singletonList(entity2));
        businessCard2.setDocTypeIdList(Collections.singletonList(docTypeId2));

        Participant participant3 = new Participant();
        participant3.setValue("0195:sguen199506857r");
        participant3.setScheme("participant3Scheme");
        Entity entity3 = new Entity();
        entity3.setCountryCode("countryCode3");
        entity3.setName(new Name("THE SUBSTATION LTD", "language3"));
        entity3.setRegDate(DateTime.now().toDate());
        DocTypeId docTypeId3 = new DocTypeId();
        docTypeId3.setDeprecated("deprecated3");
        docTypeId3.setDisplayName("displayName3");
        docTypeId3.setScheme("docTypeIdScheme3");
        docTypeId3.setValue("docTypeIdValue3");

        BusinessCard businessCard3 = new BusinessCard();
        businessCard3.setParticipant(participant3);
        businessCard3.setEntityList(Collections.singletonList(entity3));
        businessCard3.setDocTypeIdList(Collections.singletonList(docTypeId3));

        indexToBusinessCard(businessCard1, businessCard2, businessCard3);

        //when
        BusinessCardResult result = businessCardElasticSearchRepository.getByName("the substation ltd");

        //then
        assertThat(result.getTotalElements()).isEqualTo(1);

    }
}