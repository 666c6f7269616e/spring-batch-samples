package com.labs.springbatchsamples.batch.loopDecisionWithFile;

import com.labs.springbatchsamples.batch.AbstractJobConfigutation;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collector;

@Configuration("loopDecisionConfiguration")
public class JobConfiguration extends AbstractJobConfigutation {

    public JobConfiguration(JobBuilderFactory jobBuilderFactory,
                            StepBuilderFactory stepBuilderFactory) {
        super(jobBuilderFactory, stepBuilderFactory);
    }

    /* ------------------------------ JOB ------------------------------  */
    @Bean
    public Job loopDecisionJob(Flow loopDeciderFlow) {
        return jobBuilderFactory.get("LOOP_DECISION_JOB")
		        .start(loopDeciderFlow)
                .build()
                .build();
    }

    /* ------------------------------ FLOW ------------------------------  */

    @Bean("loopDeciderFlow")
    public Flow loopDecisionFlow(@Qualifier("repeatDecider") JobExecutionDecider repeatDecider,
                                 @Qualifier("loadResource") Step loadResource) {

        FlowBuilder<Flow> decisionFlow = new FlowBuilder<>("loopDecisionFlow");

        decisionFlow.start(loadResource)
                .next(repeatDecider)
                .on("CONTINUE")
                .to(loadResource)
                .from(repeatDecider)
                .on("COMPLETED")
                .end();
        return decisionFlow.build();
    }

    @Bean
    public Step loadResource() {
        return stepBuilderFactory.get("LOOP_DECISION_PRINT_STEP")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println("ok");
                    return RepeatStatus.FINISHED;
                }).build();
    }
    
    private Resource[] getRessource(@Value("${batch.chunk-size}") Integer chunkSize, 
                                   ApplicationContext applicationContext) throws IOException {
        return Arrays
                .stream(applicationContext.getResources("file/*"))
                .limit(chunkSize)
                .toArray(Resource[]::new);
    }
}
