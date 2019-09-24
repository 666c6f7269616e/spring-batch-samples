package com.labs.springbatchsamples.batch.loopDecisionWithFile;

import com.labs.springbatchsamples.batch.AbstractJobConfigutation;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Configuration("loopDecisionConfiguration")
public class JobConfiguration extends AbstractJobConfigutation {
    
    private final ApplicationContext applicationContext;

    public JobConfiguration(JobBuilderFactory jobBuilderFactory,
                            StepBuilderFactory stepBuilderFactory, ApplicationContext applicationContext) {
        super(jobBuilderFactory, stepBuilderFactory);
        this.applicationContext = applicationContext;
    }

    @Bean
    public Job loopDecisionJob(@Qualifier("loader") Step loader, @Qualifier("consumer") Step consumer, JobExecutionDecider decider) {
        return jobBuilderFactory.get("job")
                .start(loader)
                .next(decider)
                .on("CONTINUE")
                .to(consumer)
                .next(loader)
                .from(decider)
                .on("COMPLETED")
                .end()
                .build()
                .build();
    }

    @Bean
    public Step consumer() {
        return stepBuilderFactory.get("consumer")
                .tasklet((contribution, chunkContext) -> executeConsume(chunkContext)).build();
    }

    private RepeatStatus executeConsume(ChunkContext chunkContext) throws IOException {
        List<String> resources = (List<String>) chunkContext.getStepContext().getJobExecutionContext().get("resources");
        System.out.println("Consuming " + resources);
        deleteResource(resources);
        return RepeatStatus.FINISHED;
    }

    private void deleteResource(List<String> resources) throws IOException {
        for (String resource: resources){
            Files.delete(Paths.get(resource));
        }
    }

    @Bean
    public Step loader() {
        return stepBuilderFactory.get("loader")
                .tasklet((contribution, chunkContext) ->  executeLoad(chunkContext)).build();
    }

    private RepeatStatus executeLoad(ChunkContext chunkContext) throws IOException {
        List<String> resource = getResource();
        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().put("resources", resource);
        System.out.println("Loading " + resource);
        return RepeatStatus.FINISHED;
    }

    @Bean
    public JobExecutionDecider decider(){
        return ((jobExecution, stepExecution) -> decide(jobExecution));
    }

    private FlowExecutionStatus decide(JobExecution jobExecution) {
        List<String> resources = (List<String>) jobExecution.getExecutionContext().get("resources");
        return (resources == null || resources.isEmpty()) ? FlowExecutionStatus.COMPLETED : new FlowExecutionStatus("CONTINUE");
    }

    private List<String> getResource() throws IOException {

        return Arrays
                .stream(applicationContext.getResources("file:file/*"))
                .map(this::getFile)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(File::getPath)
                .limit(1L)
                .collect(Collectors.toList());
    }

    private Optional<File> getFile(Resource resource) {
        Optional<File> file;
        try {
            file = Optional.of(resource.getFile());
        } catch (IOException e) {
            file = Optional.empty();
        }
        return file;
    }
}
