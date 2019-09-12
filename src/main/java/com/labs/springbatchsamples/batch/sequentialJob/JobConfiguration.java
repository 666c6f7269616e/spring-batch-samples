package com.labs.springbatchsamples.batch.sequentialJob;

import com.labs.springbatchsamples.batch.AbstractJobConfigutation;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("sequentialJobConfiguration")
public class JobConfiguration extends AbstractJobConfigutation {

	public JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		super(jobBuilderFactory, stepBuilderFactory);
	}

	@Bean
	public Job sequentialJob(Step sequentialJob1, Step sequentialJob2, Step sequentialJob3) {
		return jobBuilderFactory.get("SEQUENTIAL_FLOW")
				.start(sequentialJob1)
				.next(sequentialJob2)
				.next(sequentialJob3)
				.build();
	}

	@Bean
	public Step sequentialJob3() {
		return stepBuilderFactory.get("SEQUENTIAL_STEP_3")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("sequentialStep3");
					return RepeatStatus.FINISHED;
				})
				.build();
	}

	@Bean
	public Step sequentialJob2() {
		return stepBuilderFactory.get("SEQUENTIAL_STEP_2")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("sequentialStep2");
					return RepeatStatus.FINISHED;
				})
				.build();
	}

	@Bean
	public Step sequentialJob1() {
		return stepBuilderFactory.get("SEQUENTIAL_STEP_1")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("sequentialStep1");
					return RepeatStatus.FINISHED;
				})
				.build();
	}

}
