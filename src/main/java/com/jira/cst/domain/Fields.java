package com.jira.cst.domain;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields {

    private int storyPoints;

    public int getStoryPoints() {
        return storyPoints;
    }

    public void setStoryPoints(int storyPoints) {
        this.storyPoints = storyPoints;
    }
}
