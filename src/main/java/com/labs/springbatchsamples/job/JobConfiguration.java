package com.labs.springbatchsamples.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
public class JobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	public JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	/********************************************HELLO JOB*************************************************************/

	@Bean
	public Job helloJob() {
		return jobBuilderFactory.get(JobEnum.HELLO_WORLD.getJobName())
				.start(helloStep())
				.build();
	}

	public Step helloStep() {
		return stepBuilderFactory.get(StepEnum.HELLO_WORLD.getStepName())
				.tasklet((contribution, chunkContext) -> {
					System.out.println("Hello World!");
					return RepeatStatus.FINISHED;
				}).build();
	}

	/********************************************SEQUENTIAL FLOW*******************************************************/

	@Bean
	public Job sequentialFlow() {
		return this.jobBuilderFactory.get(JobEnum.SEQUENTIAL_FLOW.getJobName())
				.start(sequentialFlow1())
				.next(sequentialFlow2())
				.next(sequentialFlow3())
				.build();
	}

	private Step sequentialFlow3() {
		return stepBuilderFactory.get(StepEnum.SEQUENTIAL_FLOW_3.getStepName())
				.tasklet((contribution, chunkContext) -> {
					System.out.println("sequentialFlow3");
					return RepeatStatus.FINISHED;
				}).build();
	}

	private Step sequentialFlow2() {
		return stepBuilderFactory.get(StepEnum.SEQUENTIAL_FLOW_2.getStepName())
				.tasklet((contribution, chunkContext) -> {
					System.out.println("sequentialFlow2");
					return RepeatStatus.FINISHED;
				}).build();
	}

	private Step sequentialFlow1() {
		return stepBuilderFactory.get(StepEnum.SEQUENTIAL_FLOW_1.getStepName())
				.tasklet((contribution, chunkContext) -> {
					System.out.println("sequentialFlow1");
					return RepeatStatus.FINISHED;
				}).build();

	}

}
