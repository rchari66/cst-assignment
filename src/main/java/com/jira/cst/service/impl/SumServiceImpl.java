package com.jira.cst.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jira.cst.domain.Issue;
import com.jira.cst.domain.SQSMessage;
import com.jira.cst.service.JiraService;
import com.jira.cst.service.SQSService;
import com.jira.cst.service.SumService;

@Service
public class SumServiceImpl implements SumService {

    private static final Logger log = LoggerFactory.getLogger(SumServiceImpl.class);

    @Autowired
    private JiraService jiraService;

    @Autowired
    private SQSService sqsService;

    @Override
    public void processQuery(String query, String descriptionName) throws Exception {

        List<Issue> issues = jiraService.queryJira(query);

        SQSMessage sqsMessage = new SQSMessage(descriptionName);
        int total = 0;

        for (Issue issue: issues
             ) {
            total = total + issue.getFields().getStoryPoints();
        }

        sqsMessage.setTotalPoints(total);

        try{
            sqsService.push(sqsMessage);
        } catch (Exception e){
            log.error("Unable to process request. Error : " + e.getMessage());
            throw e;
        }
    }

}
