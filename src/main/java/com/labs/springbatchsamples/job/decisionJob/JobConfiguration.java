package com.labs.springbatchsamples.job.decisionJob;

import com.labs.springbatchsamples.job.JobEnum;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("decisionJobConfiguration")
public class JobConfiguration {

	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	public JobConfiguration(JobBuilderFactory jobBuilderFactory,
							StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Flow decisionFlow(@Qualifier("decider") JobExecutionDecider decider) {

		FlowBuilder<Flow> decisionFlow = new FlowBuilder<>("decisionFlow");

		decisionFlow.start(decider)
					.on("EVEN")
					.to(decisionStepEven())
				.from(decider)
					.on("ODD")
					.to(decisionStepOdd())
				.end();

		return decisionFlow.build();
	}

	@Bean
	public Job decisionJob(JobExecutionListener jobListener, Flow decisionFlow) {
		return jobBuilderFactory.get(JobEnum.DECISION_JOB.getJobName())
				.start(decisionFlow)
				.build()
				.listener(jobListener)
				.build();
	}

	@Bean
	public Step decisionStepOdd() {
		return stepBuilderFactory.get("DECISION_STEP_ODD")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("Step ODD");
					return RepeatStatus.FINISHED;
				}).build();
	}

	@Bean
	public Step decisionStepEven() {
		return stepBuilderFactory.get("DECISION_STEP_EVEN")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("Step EVEN");
					return RepeatStatus.FINISHED;
				}).build();
	}


    @Bean("decider")
    public JobExecutionDecider decider() {

		return (jobExecution, stepExecution) -> {

			String jobParam = jobExecution.getJobParameters().getString("param", "");

			if (jobParam.length() % 2 == 0) {
				return new FlowExecutionStatus("EVEN");
			}
			return new FlowExecutionStatus("ODD");
		};
    }
}