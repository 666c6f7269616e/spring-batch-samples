package com.labs.springbatchsamples.job.sequentialflow;

import com.labs.springbatchsamples.job.SimpleJobEnum;
import com.labs.springbatchsamples.job.StepEnum;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("sequentialFlowConfiguration")
public class JobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	public JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Job sequentialFlow() {
		return this.jobBuilderFactory.get(SimpleJobEnum.SEQUENTIAL_FLOW.getJobName())
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
