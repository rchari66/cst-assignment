package com.jira.cst.service;

import com.jira.cst.domain.SQSMessage;

public interface SQSService {
    boolean push(SQSMessage sqsMessage) throws Exception;
}
