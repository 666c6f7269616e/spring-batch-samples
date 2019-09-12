package com.labs.springbatchsamples.batch.loopDecision;

import org.junit.Test;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.test.MetaDataInstanceFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class RepeatDeciderTest {

	@Test
	public void decideShouldBeCompleted() {

		// Given
		Integer ITERATION_NUMBER = 0;
		JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution();
		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
		RepeatDecider repeatDecider = new RepeatDecider(ITERATION_NUMBER);

		// When
		FlowExecutionStatus executionStatus = repeatDecider.decide(jobExecution, stepExecution);

		//Then
		assertThat(executionStatus).isEqualTo(FlowExecutionStatus.COMPLETED);
	}

	@Test
	public void decideShouldBeContinue() {

		// Given
		Integer ITERATION_NUMBER = 1;
		JobExecution jobExecution = MetaDataInstanceFactory.createJobExecution();
		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution();
		RepeatDecider repeatDecider = new RepeatDecider(ITERATION_NUMBER);

		// When
		FlowExecutionStatus executionStatus = repeatDecider.decide(jobExecution, stepExecution);

		//Then
		assertThat(executionStatus).isEqualTo(new FlowExecutionStatus("CONTINUE"));
	}
}