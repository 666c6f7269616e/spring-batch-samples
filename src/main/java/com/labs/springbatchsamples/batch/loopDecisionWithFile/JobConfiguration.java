package com.labs.springbatchsamples.batch.loopDecisionWithFile;

import com.labs.springbatchsamples.batch.AbstractJobConfigutation;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration("resourceLoopDecisionConfiguration")
public class JobConfiguration extends AbstractJobConfigutation {

    public JobConfiguration(@Value("${batch.chunk-size}") Long chunkSize,
                            ApplicationContext applicationContext,
                            JobBuilderFactory jobBuilderFactory,
                            StepBuilderFactory stepBuilderFactory) {
        super(jobBuilderFactory, stepBuilderFactory);
    }

    /* ------------------------------ JOB ------------------------------  */
    @Bean
    public Job resourceLoopDecisionJob(Flow loopDeciderFlow) {
        return jobBuilderFactory.get("RESOURCE_LOOP_DECISION_JOB")
		        .start(loopDeciderFlow)
                .build()
                .build();
    }


    /* ------------------------------ FLOW ------------------------------  */

    @Bean("resourceLoopDeciderFlow")
    public Flow resourceLoopDeciderFlow(@Qualifier("loadResourcestep") Step loadResourcestep,
                                 @Qualifier("resourceConsumer") Step resourceConsumer,
                                 @Qualifier("resourceDecider") JobExecutionDecider decider) {

        FlowBuilder<Flow> decisionFlow = new FlowBuilder<>("resourceLoopDecisionFlow");

        decisionFlow.start(loadResourcestep)
                .next(decider)
                .on("CONTINUE")
                .to(resourceConsumer)
                .next(loadResourcestep)
                .from(decider)
                .on("COMPLETED")
                .end();

        return decisionFlow.build();
    }

    private void deleteFile(Path file){
        try {
            Files.delete(file);
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    @Bean
    public Step resourceConsumer() {
        return stepBuilderFactory.get("resourceConsumer")
                .tasklet((contribution, chunkContext) -> {
                            ExecutionContext executionContext = getExecutionContext(chunkContext);

                            List<String> files = (List<String>) executionContext.get("resources");
                            files.stream().map(Paths::get).forEach(this::deleteFile);
                            executionContext.put("resources", new ArrayList<String>());
                            return RepeatStatus.FINISHED;
                        }
                ).build();
    }

    private List<String> getRessource(ApplicationContext applicationContext, Long chunkSize) throws IOException {
        return Arrays
                .stream(applicationContext.getResources("file/*"))
                .filter(Resource::exists)
                .limit(chunkSize)
                .map(this::getFile)
                .collect(Collectors.toList());
    }

    private ExecutionContext getExecutionContext(ChunkContext chunkContext) {
        return chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
    }

    @Bean("loadResourcestep")
    public Step loadResourcestep(@Value("${batch.chunk-size}") Long chunkSize, ApplicationContext applicationContext) {
        return stepBuilderFactory.get("loadResourcestep")
                .tasklet((contribution, chunkContext) -> {
                    ExecutionContext executionContext = getExecutionContext(chunkContext);

                    List<String> resources = (List<String>) executionContext.get("resources");

                    if (resources == null || resources.size() == 0){
                        resources = getRessource(applicationContext, chunkSize);
                    }

                    executionContext.put("resources", resources);
                    return RepeatStatus.FINISHED;
                }).build();
    }

    String getFile(Resource resource) {
        try {
            return resource.getFile().getPath();
        } catch (IOException e) {
            return null;
        }
    }
}
