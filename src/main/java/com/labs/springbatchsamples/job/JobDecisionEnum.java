package com.labs.springbatchsamples.job;

import java.util.Arrays;

public enum JobDecisionEnum implements JobEnum{

    DECISION_FLOW("decisionFlow");


    private String jobName;

    JobDecisionEnum(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public static JobDecisionEnum fromNomeJob(String jobName) {
        return Arrays.stream(values()).
                filter(job -> job.getJobName().equals(jobName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No job for this name"));
    }
}