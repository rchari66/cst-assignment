package com.jira.cst.service.impl;


import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jira.cst.domain.SQSMessage;
import com.jira.cst.exception.SQSException;
import com.jira.cst.service.SQSService;

/**
 * Service for connecting to Amazon SQS(local)
 */

@Service
public class SQSServiceImpl implements SQSService {

    private static final Logger log = LoggerFactory.getLogger(SQSServiceImpl.class);

    private static final String QUEUE_NAME = "cst-test-queue";
    private static final String QUEUE_URL = "QUEUE_URL";

    @Autowired
    private Environment environment;

    private String queueUrl;
    private AmazonSQS sqs;
    private ObjectMapper mapper;

    @Value("${amazon.sqs.prod}")
    private boolean isProd;

    @PostConstruct
    public void init() throws Exception {
        String baseUrl = this.environment.getRequiredProperty(QUEUE_URL);
        queueUrl = baseUrl + "/queue/" + QUEUE_NAME;
        mapper = new ObjectMapper();

        // If production flag is enabled create default sqs client; Otherwise connect to local amazon sqs
        if (isProd) {
            log.debug("Running in production Mode");
            sqs = AmazonSQSClientBuilder.defaultClient();
        } else {
            log.debug("Running in testing Mode");
            sqs = AmazonSQSClientBuilder.standard()
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(baseUrl, ""))
                    .build();
        }


        CreateQueueRequest create_request = new CreateQueueRequest(QUEUE_NAME)
                .addAttributesEntry("DelaySeconds", "60")
                .addAttributesEntry("MessageRetentionPeriod", "86400");

        try {
            sqs.createQueue(create_request);
        } catch (AmazonSQSException e) {
            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
                throw e;
            }
        }

    }

    @Override
    public boolean push(SQSMessage sqsMessage) throws SQSException {

        try {
            String data = mapper.writeValueAsString(sqsMessage);

            SendMessageRequest send_msg_request = new SendMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMessageBody(data)
                    .withDelaySeconds(5);

            sqs.sendMessage(send_msg_request);
        } catch (Exception e) {
            throw new SQSException("Error: Unable to publish message");
        }

        return true;
    }


}
