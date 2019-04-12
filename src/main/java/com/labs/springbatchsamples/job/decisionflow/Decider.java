package com.labs.springbatchsamples.job.decisionflow;

import com.labs.springbatchsamples.job.DecisionEnum;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

//@StepScope
@Component
public class Decider implements JobExecutionDecider {

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

        String decision = jobExecution.getJobParameters().getString("decision", "no");
        return (decision.equals(DecisionEnum.YES.getDecision())) ?
                FlowExecutionStatus.COMPLETED : FlowExecutionStatus.FAILED;
    }
}
