package com.labs.springbatchsamples.batch.job;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
//@PropertySource("classpath:application-test.properties")
public class BatchTestConfiguration {

	@Bean
	public JobLauncherTestUtils getJobLauncherTestUtils(){
		return new JobLauncherTestUtils();
	}

}
