package com.jira.cst.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.jira.cst.domain.Issue;
import com.jira.cst.exception.JiraExceptoin;
import com.jira.cst.service.JiraService;

/**
 * Service for connecting to JIRA
 */
@Service
public class JiraServiceImpl implements JiraService {

    private static final Logger log = LoggerFactory.getLogger(JiraServiceImpl.class);

    private static final String JIRA_ENV_VARIABLE = "JIRA_BASE_URL";
    private static final String JIRA_SEARCH_ENDPOINT = "/rest/api/2/search";

    @Autowired
    private Environment environment;

    private String jiraBaseUrl;
    private String jiraSearchEndpointUrl;

    @PostConstruct
    public void init() {
        this.jiraBaseUrl = this.environment.getRequiredProperty(JIRA_ENV_VARIABLE);
        this.jiraSearchEndpointUrl =  this.jiraBaseUrl + JIRA_SEARCH_ENDPOINT;

        log.info("jira_search_endpoint url : "+ this.jiraSearchEndpointUrl);

    }

    @Override
    public List<Issue> queryJira(String query) throws JiraExceptoin {
        RestTemplate restTemplate = new RestTemplate();


        // Create headers for restTemplate
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        List<Issue> issues;

        try {
            log.info("Retrieving data from jira service..");
            ResponseEntity<List<Issue>> rateResponse =
                    restTemplate.exchange(jiraSearchEndpointUrl + "?q=" + query,
                            HttpMethod.GET, entity, new ParameterizedTypeReference<List<Issue>>() {
                            });
            issues = rateResponse.getBody();

            log.info("Received from jira service " + rateResponse.getBody());

        } catch (Exception e) {
            log.error("Unable to retrieve data from JIRA service");
            throw new JiraExceptoin("Error : Unable to retrieve data from JIRA service");
        }


        return issues != null ? issues : new ArrayList<Issue>();
    }

}
