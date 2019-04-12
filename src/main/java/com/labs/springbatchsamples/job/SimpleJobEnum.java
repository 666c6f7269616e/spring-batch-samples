package com.labs.springbatchsamples.job;

import java.util.Arrays;

public enum SimpleJobEnum implements JobEnum{

    HELLO_WORLD("helloJob"),
    SEQUENTIAL_FLOW("sequentialFlow");


    private String jobName;

    SimpleJobEnum(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public static SimpleJobEnum fromNomeJob(String jobName) {
        return Arrays.stream(values()).
                filter(job -> job.getJobName().equals(jobName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No job for this name"));
    }
}