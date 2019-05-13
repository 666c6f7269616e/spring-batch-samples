package com.labs.springbatchsamples.job.loopDecisionJob;

import com.labs.springbatchsamples.job.JobEnum;
import com.labs.springbatchsamples.job.StepEnum;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("loopDecisionConfiguration")
public class JobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private String parameter;

    public JobConfiguration(JobBuilderFactory jobBuilderFactory,
                            StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Flow loopDecisionFlow(@Qualifier("loopDecider") JobExecutionDecider decider) {

        FlowBuilder<Flow> decisionFlow = new FlowBuilder<>("loopDecisionFlow");

        decisionFlow.start(printResource())
                .next(decider)
                .on("CONTINUE")
                .to(printResource())
                .next(printResource())
                .on("COMPLETED")
                .end()
                .build();

        return decisionFlow.build();
    }

    @Bean
    public Job loopDecisionJob(JobExecutionListener jobListener, Flow decisionFlow) {
        return jobBuilderFactory.get(JobEnum.LOOP_DECISION_JOB.getJobName())
                .start(decisionFlow)
                .build()
                .build();
    }


    @Bean
    public Step prepareResource() {
        return stepBuilderFactory.get(StepEnum.LOOP_DECISION_STEP.getStepName())
                .tasklet((contribution, chunkContext) -> {
                    parameter = parameter.substring(1);
                    return RepeatStatus.FINISHED;
                }).build();
    }


    @Bean
    public Step printResource() {
        return stepBuilderFactory.get(StepEnum.LOOP_DECISION_STEP.getStepName())
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(parameter);
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean("loopDecider")
    public JobExecutionDecider decider() {

        return (jobExecution, stepExecution) -> {

            if (parameter == null) {
                parameter = jobExecution.getJobParameters().getString("param", "");
            }

            if (parameter.length() == 0){
                return new FlowExecutionStatus("COMPLETED");
            }

            return new FlowExecutionStatus("CONTINUE");
        };
    }

}
