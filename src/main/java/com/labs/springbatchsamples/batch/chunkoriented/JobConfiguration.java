package com.labs.springbatchsamples.batch.chunkoriented;

import com.labs.springbatchsamples.batch.AbstractJobConfigutation;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JobConfiguration extends AbstractJobConfigutation {

	public JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		super(jobBuilderFactory, stepBuilderFactory);
	}

	@Bean
	@StepScope
	public ItemReader<Integer> itemReader(@Value("#{jobParameters['iteration']}")Integer iterationNumber){
		List<Integer> intList = IntStream.range(0, iterationNumber).boxed().collect(Collectors.toList());
		return new IteratorItemReader<>(intList);
	}

	@Bean
	public ItemWriter<Integer> itemWriter(){
		return System.out::println;
	}

	@Bean
	public Job decisionJob(Step chunkOrientedStep) {
		return jobBuilderFactory.get("CHUNK_ORIENTED_JOB")
				.start(chunkOrientedStep)
				.build();
	}

	@StepScope
	@Bean
	private Step chunkOrientedStep(@Value("${batch.chunk-size}") Integer chunkSize, ItemReader<Integer> itemReader, ItemWriter<Integer> itemWriter) {
		return stepBuilderFactory.get("CHUNK_ORIENTED_STEP")
				.<Integer, Integer>chunk(chunkSize)
				.reader(itemReader)
				.writer(itemWriter)
				.build();
	}
}
