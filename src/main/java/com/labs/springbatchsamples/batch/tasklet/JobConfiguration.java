//package com.labs.springbatchsamples.batch.tasklet;
//
//import com.labs.springbatchsamples.batch.AbstractJobConfigutation;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.repeat.RepeatStatus;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration("taskletConfiguration")
//public class JobConfiguration extends AbstractJobConfigutation {
//
//	public JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
//		super(jobBuilderFactory, stepBuilderFactory);
//	}
//
//	@Bean
//	public Job helloJob(Step taskletStep) {
//		return jobBuilderFactory.get("TASKLET")
//				.start(taskletStep)
//				.build();
//	}
//
//	@Bean
//	public Step taskletStep() {
//		return stepBuilderFactory.get("TASKLET_STEP")
//				.tasklet((contribution, chunkContext) -> {
//					System.out.println("Hello TASKLET!");
//					return RepeatStatus.FINISHED;
//				}).build();
//	}
//}
