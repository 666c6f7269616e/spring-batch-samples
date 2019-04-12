package com.labs.springbatchsamples.job;

import java.util.Arrays;

public enum DecisionEnum {

    YES("yes"),
    NO("no");


    private String decision;

    DecisionEnum(String decision) {
        this.decision = decision;
    }

    public String getDecision() {
        return decision;
    }

    public static DecisionEnum fromNomeJob(String jobName) {
        return Arrays.stream(values()).
                filter(decision -> decision.getDecision().equals(jobName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No decision for this name"));
    }
}
