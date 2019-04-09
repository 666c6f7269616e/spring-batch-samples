package com.labs.springbatchsamples.web;

import com.labs.springbatchsamples.job.JobEnum;
import com.labs.springbatchsamples.util.SpringContextUtil;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.BeansException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/execucao")
@Api(value = "execucao", description = "API for launching batch processing")
public class BatchExecutor {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	private final
	JobLauncher launcher;

	public BatchExecutor(JobLauncher launcher) {
		this.launcher = launcher;
	}

	@GetMapping(value = "/{jobName}")
	public ResponseEntity<?> executarJob(@PathVariable("jobName") JobEnum jobName) {

		try {
			Job job = SpringContextUtil.getBean(jobName.getJobName(), Job.class);
			LOG.info("\n==============================\n" +
					"LAUNCHING JOB: " + jobName.getJobName() +
					"\n==============================\n");
			JobExecution jobExecution = launcher.run(job, new JobParameters());
			return ResponseEntity.ok(jobExecution.getId());

		} catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException | JobRestartException e) {
			LOG.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Job " + jobName.name() + " não disponível pelo momento: " + e.getMessage());

		} catch (BeansException e) {
			LOG.error(e.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Job " + jobName.name() + " não cadastrado. " + e.getMessage());

		} catch (Exception e) {
			LOG.warn("Erro não gerenciado: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}
}
