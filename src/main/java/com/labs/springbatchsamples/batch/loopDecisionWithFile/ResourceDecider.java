package com.labs.springbatchsamples.batch.loopDecisionWithFile;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResourceDecider implements JobExecutionDecider {

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {

		List<String> files = (List<String>) jobExecution.getExecutionContext().get("resources");
		assert files != null;

		if (files.size() != 0) {
			return new FlowExecutionStatus("CONTINUE");
		} else {
			jobExecution.setExitStatus(ExitStatus.COMPLETED);
			return FlowExecutionStatus.COMPLETED;
		}
	}
}
