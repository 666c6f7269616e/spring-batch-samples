package com.labs.springbatchsamples.job.decisionflow;

import com.labs.springbatchsamples.job.JobDecisionEnum;
import com.labs.springbatchsamples.job.StepEnum;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("decisionFlowConfiguration")
public class JobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final Decider decider;

	public JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, Decider decider) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.decider = decider;
	}

	@Bean
	public Job decisionFlow() {
		return this.jobBuilderFactory.get(JobDecisionEnum.DECISION_FLOW.getJobName())
				.start(decisionFlow1())
				.next(decider)
				.from(decider)
					.on("COMPLETED")
					.to(decisionFlow2())
					.next(decisionFlow4())
				.from(decider)
					.on("FAILED")
					.to(decisionFlow3())
					.next(decisionFlow4())
				.from(decisionFlow4()).next(decisionFlow5())
				.end()
				.build();
	}

	private Step decisionFlow5() {
		return stepBuilderFactory.get(StepEnum.DECISION_FLOW_END.getStepName())
				.tasklet((contribution, chunkContext) -> {
					System.out.println("Job end");
					return RepeatStatus.FINISHED;
				}).build();
	}

	private Step decisionFlow4() {
		return stepBuilderFactory.get(StepEnum.DECISION_FLOW_CONTINUE.getStepName())
				.tasklet((contribution, chunkContext) -> {
					System.out.println("Step continue");
					return RepeatStatus.FINISHED;
				}).build();
	}

	private Step decisionFlow3() {
		return stepBuilderFactory.get(StepEnum.DECISION_FLOW_FAIL.getStepName())
				.tasklet((contribution, chunkContext) -> {
					System.out.println("Step no");
					return RepeatStatus.FINISHED;
				}).build();
	}

	private Step decisionFlow2() {
		return stepBuilderFactory.get(StepEnum.DECISION_FLOW_SUCCESS.getStepName())
				.tasklet((contribution, chunkContext) -> {
					System.out.println("Step yes");
					return RepeatStatus.FINISHED;
				}).build();
	}

	private Step decisionFlow1() {
		return stepBuilderFactory.get(StepEnum.DECISION_FLOW_START.getStepName())
				.tasklet((contribution, chunkContext) -> {
					System.out.println("Starting job");
					return RepeatStatus.FINISHED;
				}).listener(new StepExecutionListenerSupport(){

				}).build();

	}

}
