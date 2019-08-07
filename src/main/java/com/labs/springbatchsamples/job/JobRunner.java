package com.labs.springbatchsamples.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JobRunner {

	private static final Logger LOG = LoggerFactory.getLogger(JobRunner.class);

	private final JobOperator jobOperator;
	private final JobExplorer jobExplorer;
	private final JobLauncher jobLauncher;
	private final ApplicationContext applicationContext;

	public JobRunner(@Qualifier("simpleJobOperator") JobOperator jobOperator,
					 @Qualifier("asyncLauncher") JobLauncher jobLauncher,
					 JobExplorer jobExplorer,
					 ApplicationContext applicationContext) {
		this.jobOperator = jobOperator;
		this.jobExplorer = jobExplorer;
		this.jobLauncher = jobLauncher;
		this.applicationContext = applicationContext;
	}

	public JobExecution run(JobEnum jobName, JobParameters jobParameters, Long jobId) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

		checkJobAlreadyRunning(jobId);

		jobParameters = new JobParametersBuilder()
				.addJobParameters(jobParameters)
				.addDate("timestamp", new Date())
				.toJobParameters();

		Job job = applicationContext.getBean(jobName.getJobName(), Job.class);
		return jobLauncher.run(job, jobParameters);
	}

	private void checkJobAlreadyRunning(Long jobId) throws JobExecutionAlreadyRunningException {
		BatchStatus jobStatus = getJobStatus(jobId);
		if (BatchStatus.STARTED.equals(jobStatus) ||
				BatchStatus.STARTING.equals(jobStatus) ||
				BatchStatus.STOPPING.equals(jobStatus)) {
			throw new JobExecutionAlreadyRunningException("Job ja em execucao");
		}
	}

	private BatchStatus getJobStatus(Long id) {
		JobExecution jobExecution = jobExplorer.getJobExecution(id);
		return (jobExecution != null) ? jobExecution.getStatus() : BatchStatus.UNKNOWN;
	}

	public void stop(Long id) throws NoSuchJobExecutionException, JobExecutionNotRunningException {
		if (id != null){
			jobOperator.stop(id);
		}
	}
}
