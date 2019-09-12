package com.labs.springbatchsamples.batch;

import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;

public abstract class AbstractJobConfigutation {

	protected final JobBuilderFactory jobBuilderFactory;
	protected final StepBuilderFactory stepBuilderFactory;

	public AbstractJobConfigutation(JobBuilderFactory jobBuilderFactory,
	                                StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}


}
