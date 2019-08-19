package com.labs.springbatchsamples.batch.job.sequentialJob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("sequentialJobConfiguration")
public class JobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	public JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Job sequentialJob(JobExecutionListener listener) {
		return this.jobBuilderFactory.get("SEQUENTIAL_FLOW")
				.start(sequentialJob1())
				.next(sequentialJob2())
				.next(sequentialJob3())
				.listener(listener)
				.build();
	}

	private Step sequentialJob3() {
		return stepBuilderFactory.get("SEQUENTIAL_STEP_3")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("sequentialStep3");
					return RepeatStatus.FINISHED;
				}).build();
	}

	private Step sequentialJob2() {
		return stepBuilderFactory.get("SEQUENTIAL_STEP_2")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("sequentialStep2");
					return RepeatStatus.FINISHED;
				}).build();
	}

	private Step sequentialJob1() {
		return stepBuilderFactory.get("SEQUENTIAL_STEP_1")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("sequentialStep1");
					return RepeatStatus.FINISHED;
				}).build();

	}

}
