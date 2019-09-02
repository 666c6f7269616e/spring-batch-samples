package com.labs.springbatchsamples.batch.listenerJob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("listenerJobConfiguration")
public class JobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	public JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Job helloJob(SimpleJobListener simpleJobListener, Step helloStep) {
		return jobBuilderFactory.get("LISTENER_JOB")
				.start(helloStep)
				.listener(simpleJobListener)
				.build();
	}

	@Bean
	public Step helloStep(SimpleStepListener simpleStepListener) {
		return stepBuilderFactory.get("LISTENER_STEP")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("Running step");
					return RepeatStatus.FINISHED;
				})
				.listener(simpleStepListener)
				.build();
	}
}
