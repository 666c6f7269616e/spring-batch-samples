package com.labs.springbatchsamples.job.loopDecisionJob;

import com.labs.springbatchsamples.job.JobEnum;
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

    /* ------------------------------ JOB ------------------------------  */


    @Bean
    public Job loopDecisionJob(JobExecutionListener jobListener, @Qualifier("loopDeciderFlow") Flow decisionFlow) {
        return jobBuilderFactory.get(JobEnum.LOOP_DECISION_JOB.getJobName())
                .start(decisionFlow)
                .build()
                .listener(jobListener)
                .build();
    }

    /* ------------------------------ FLOW ------------------------------  */

    @Bean("loopDeciderFlow")
    public Flow loopDecisionFlow(@Qualifier("loopDecider") JobExecutionDecider loopDecider,
                                 @Qualifier("removeResource") Step removeResource) {

        FlowBuilder<Flow> decisionFlow = new FlowBuilder<>("loopDecisionFlow");

        decisionFlow.start(prepareResource())
                .next(printResource())
                .next(loopDecider)
                    .on("CONTINUE")
                    .to(removeResource())
                    .next(printResource())
                .from(loopDecider)
                    .on("COMPLETED")
                    .end();
//                .build();

        return decisionFlow.build();
    }

    /* ------------------------------ STEP ------------------------------  */

    @Bean("removeResource")
    public Step removeResource() {
        return stepBuilderFactory.get("LOOP_DECISION_REMOVE_STEP")
                .tasklet((contribution, chunkContext) -> {
                    parameter = parameter.substring(1);
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step printResource() {
        return stepBuilderFactory.get("LOOP_DECISION_PRINT_STEP")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(parameter);
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step prepareResource() {
        return stepBuilderFactory.get("LOOP_DECISION_PREPARE_STEP")
                .tasklet((contribution, chunkContext) -> {
                    parameter = (String) chunkContext.getStepContext().getJobParameters().getOrDefault("param", "");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    /* ------------------------------ DECIDER ------------------------------  */

    @Bean("loopDecider")
    public JobExecutionDecider loopDecider() {

        return (jobExecution, stepExecution) -> {

            if (parameter.length() == 0){
                return new FlowExecutionStatus("COMPLETED");
            }

            return new FlowExecutionStatus("CONTINUE");
        };
    }

}
