package com.labs.springbatchsamples.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

@EnableBatchProcessing
@PropertySource("classpath:application-test.properties")
public class BatchTestConfiguration {

	@Bean
	public JobLauncherTestUtils getJobLauncherTestUtils(){
		return new JobLauncherTestUtils();
	}

}
