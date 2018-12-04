package com.jira.cst.service;

import java.util.List;

import com.jira.cst.domain.Issue;
import com.jira.cst.exception.JiraExceptoin;

public interface JiraService {
    List<Issue> queryJira(String query) throws JiraExceptoin;
}
