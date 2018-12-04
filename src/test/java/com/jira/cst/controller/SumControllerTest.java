package com.jira.cst.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jira.cst.domain.Fields;
import com.jira.cst.domain.Issue;
import com.jira.cst.domain.SQSMessage;
import com.jira.cst.service.JiraService;
import com.jira.cst.service.SQSService;

@RunWith(SpringRunner.class)
@WebMvcTest(value = SumController.class, secure = false)
@ComponentScan({"com.jira.cst"})
public class SumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private JiraService jiraService;

    @Mock
    private SQSService sqsService;

    @Test
    public void getHomeTest() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

    }

    @Test
    public void issueSumTest() throws Exception {

        List<Issue> issues = getIssueList();

        // Mock JIRA service
        Mockito.when(
                jiraService.queryJira(Mockito.anyString())).thenReturn(issues);

        // Mock SQS service
        Mockito.when(sqsService.push(Mockito.any(SQSMessage.class))).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                        "/api/issue/sum?query=search_query&name=description_name").accept(
                        MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        Assert.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
    }

    @Test
    public void issueSumTestNegative() throws Exception {

        List<Issue> issues = getIssueList();

        // Mock JIRA service
        Mockito.when(
                jiraService.queryJira(Mockito.anyString())).thenReturn(issues);

        // Mock SQS service
        Mockito.when(sqsService.push(Mockito.any(SQSMessage.class))).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/issue/sum?query=search_query&name=").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());

        requestBuilder = MockMvcRequestBuilders.get(
                "/api/issue/sum?query=&name=testName").accept(
                MediaType.APPLICATION_JSON);

        result = mockMvc.perform(requestBuilder).andReturn();

        Assert.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    private List<Issue> getIssueList() {
        List<Issue> issues = new ArrayList<>();

        Fields fieldsItem = new Fields();
        fieldsItem.setStoryPoints(10);

        Issue issue = new Issue();

        issue.setIssueKey("TEST-1");
        issue.setFields(fieldsItem);

        issues.add(issue);
        Issue issue2 = new Issue();
        Fields fieldsItem2 = new Fields();
        fieldsItem2.setStoryPoints(15);

        issue2.setIssueKey("TEST-2");
        issue2.setFields(fieldsItem2);
        issues.add(issue2);

        return issues;
    }
}
