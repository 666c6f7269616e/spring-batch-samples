package com.labs.springbatchsamples.batch.loopDecisionJob;

import com.labs.springbatchsamples.batch.BatchTestConfiguration;
import com.labs.springbatchsamples.batch.listenerJob.SimpleJobListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
		StepScopeTestExecutionListener.class})
@ContextConfiguration(classes = {
		BatchTestConfiguration.class,
		RepeatDecider.class,
		SimpleJobListener.class,
		JobConfiguration.class})
public class JobConfigurationTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void testInjections() {
		assertThat(jobLauncherTestUtils).isNotNull();
	}

	@Test
	public void loopDecisionJob5Iteration() throws Exception {

		// Given
		Integer ITERATION_NUMBER = 5;
		JobParameters param = new JobParametersBuilder().addLong("iteration", ITERATION_NUMBER.longValue()).toJobParameters();

		// When
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(param);
		Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();

		// Then
		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
		assertThat(stepExecutions).hasSize(ITERATION_NUMBER + 1);
	}

	@Test
	public void loopDecisionJob0Iteration() throws Exception {

		// Given
		Integer ITERATION_NUMBER = 0;
		JobParameters param = new JobParametersBuilder().addLong("iteration", ITERATION_NUMBER.longValue()).toJobParameters();

		// When
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(param);
		Collection<StepExecution> stepExecutions = jobExecution.getStepExecutions();

		// Then
		assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
		assertThat(stepExecutions).hasSize(ITERATION_NUMBER + 1);

	}
}