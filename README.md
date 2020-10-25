# XML TASK
> Version: 0.0.1  <br/>
> Audience: Backend

### Intro

I present you the XML Task API

Technologies that I used for this challenged are listed below:

- Java 8
- Spring Boot
- Elasticsearch
- JUnit

Before you begin there are some steps you have to attend.

First run the init.sh after that you will have an elasticsearch instance. 


- If you don't have any elasticsearch UI, I recommend you the ElasticSearch Head extension for chrome.
- Go to localhost:9200 and click Indexes tab
- create a new index called business card and then press ok.
- Go to main page and click options and create an alias name called business_card_v1
- Go to special query tab you have to create a new template for the business_card index
- Copy the json from business_card_index_template.json, it's under the src/main/resources/elasticsearch.template
- Make a PUT request to _template/business_card_template with the body you've copied from business_card_index_template.json
- Now you are ready to run the programme.
- If your elasticsearch business_card index is empty, make sure to change the value indexable to true inside on application.yml
- When the application is up, go to localhost:8080 and test the APIs.



