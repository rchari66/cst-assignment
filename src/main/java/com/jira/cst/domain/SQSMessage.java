package com.jira.cst.domain;

/**
 * POJO for sqs message
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SQSMessage {

    private String name;
    private int totalPoints;

    public SQSMessage(String name) {
        this.name = name;
        this.totalPoints = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }
}
