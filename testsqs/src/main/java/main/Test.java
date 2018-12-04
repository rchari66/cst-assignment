package main;

import java.util.List;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.CreateQueueRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {

    private static final String QUEUE_NAME = "cst-test-queue";
    private static class Testing {
        String a;
        int b;

        public String getA() {
            return a;
        }

        public void setA(String a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }
    }

    public static void main(String[] arg) throws Exception {

        AmazonSQS sqs = AmazonSQSClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration("http://localhost:9324", ""))
                .build();

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

        ObjectMapper mapper = new ObjectMapper();
        Testing t = new Testing();
        t.a ="chari";
        t.b = 10;

        String queueUrl = "http://localhost:9324/queue/cst-test-queue";

//        SendMessageRequest send_msg_request = new SendMessageRequest()
//                .withQueueUrl(queueUrl)
//                .withMessageBody(mapper.writeValueAsString(t))
//                .withDelaySeconds(5);
//        sqs.sendMessage(send_msg_request);

        List<Message> messages = sqs.receiveMessage(queueUrl).getMessages();

        while(messages.size() != 0) {
            for (Message message : messages
                    ) {
                System.out.println(message.getBody());
                sqs.deleteMessage(queueUrl, message.getReceiptHandle());
            }
            messages = sqs.receiveMessage(queueUrl).getMessages();
        }

    }

}
