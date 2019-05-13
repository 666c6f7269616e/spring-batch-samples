package com.labs.springbatchsamples.job.sequentialJob;

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
		return this.jobBuilderFactory.get(JobEnum.SEQUENTIAL_FLOW.getJobName())
				.start(sequentialJob1())
				.next(sequentialJob2())
				.next(sequentialJob3())
				.listener(listener)
				.build();
	}

	private Step sequentialJob3() {
		return stepBuilderFactory.get(StepEnum.SEQUENTIAL_STEP_3.getStepName())
				.tasklet((contribution, chunkContext) -> {
					System.out.println("sequentialStep3");
					return RepeatStatus.FINISHED;
				}).build();
	}

	private Step sequentialJob2() {
		return stepBuilderFactory.get(StepEnum.SEQUENTIAL_STEP_2.getStepName())
				.tasklet((contribution, chunkContext) -> {
					System.out.println("sequentialStep2");
					return RepeatStatus.FINISHED;
				}).build();
	}

	private Step sequentialJob1() {
		return stepBuilderFactory.get(StepEnum.SEQUENTIAL_STEP_1.getStepName())
				.tasklet((contribution, chunkContext) -> {
					System.out.println("sequentialStep1");
					return RepeatStatus.FINISHED;
				}).build();

	}

}
