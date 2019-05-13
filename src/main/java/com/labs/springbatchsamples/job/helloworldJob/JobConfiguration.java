package com.labs.springbatchsamples.job.helloworldJob;

import com.labs.springbatchsamples.job.JobEnum;
import com.labs.springbatchsamples.job.StepEnum;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
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
	public Job helloJob(JobExecutionListener jobListener) {
		return jobBuilderFactory.get(JobEnum.HELLO_WORLD.getJobName())
				.start(helloStep())
				.listener(jobListener)
				.build();
	}

	private Step helloStep() {
		return stepBuilderFactory.get(StepEnum.HELLO_WORLD_STEP.getStepName())
				.tasklet((contribution, chunkContext) -> {
					System.out.println("Hello World!");
					return RepeatStatus.FINISHED;
				}).build();
	}
}
