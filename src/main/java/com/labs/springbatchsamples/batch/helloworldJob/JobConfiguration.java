package com.labs.springbatchsamples.batch.helloworldJob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("helloWorldConfiguration")
public class JobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	public JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Job helloJob() {
		return jobBuilderFactory.get("HELLO_WORLD")
				.start(helloStep())
				.build();
	}

	private Step helloStep() {
		return stepBuilderFactory.get("HELLO_WORLD_STEP")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("Hello World!");
					return RepeatStatus.FINISHED;
				}).build();
	}
}
