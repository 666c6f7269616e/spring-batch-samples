package com.labs.springbatchsamples.batch.loopDecision;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@JobScope
public class RepeatDecider implements JobExecutionDecider {

	private Integer iteration;

	public RepeatDecider(@Value("#{jobParameters['iteration']}")Integer iteration) {
		this.iteration = iteration;
	}

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

		if (iteration-- > 0) {
			return new FlowExecutionStatus("CONTINUE");
		} else {
			jobExecution.setExitStatus(ExitStatus.COMPLETED);
			return FlowExecutionStatus.COMPLETED;
		}
	}
}
