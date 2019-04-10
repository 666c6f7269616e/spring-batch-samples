package com.labs.springbatchsamples.job;

import java.util.Arrays;

public enum JobEnum {

    HELLO_WORLD("helloJob"),
    SEQUENTIAL_FLOW("sequentialFlow");


    private String jobName;

    JobEnum(String jobName) {
        this.jobName = jobName;
    }

    public String getJobName() {
        return jobName;
    }

    public static JobEnum fromNomeJob(String jobName) {
        return Arrays.stream(values()).
                filter(job -> job.getJobName().equals(jobName))
                .findFirst()
                .get();
    }
}