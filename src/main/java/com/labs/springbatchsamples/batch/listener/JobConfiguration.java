package com.labs.springbatchsamples.batch.listener;

import com.labs.springbatchsamples.batch.AbstractJobConfigutation;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.core.listener.StepListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

@Configuration("listenerJobConfiguration")
public class JobConfiguration extends AbstractJobConfigutation {

	public JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		super(jobBuilderFactory, stepBuilderFactory);
	}

	@Bean
	public Job listenerJob(SimpleJobListener simpleJobListener, Step listenerStepTasklet, Step listenerStepChunkOriented) {
		return jobBuilderFactory.get("LISTENER_JOB")
				.start(listenerStepTasklet)
				.next(listenerStepChunkOriented)
				.listener(simpleJobListener)
				.build();
	}

	@Bean
	public Step listenerStepTasklet(StepListener stepListener) {
		return stepBuilderFactory.get("LISTENER_STEP_TASKLET")
				.tasklet((contribution, chunkContext) -> {
					System.out.println("Running tasklet");
					return RepeatStatus.FINISHED;
				})
				.listener(stepListener)
				.build();
	}

	@Bean
	public Step listenerStepChunkOriented(ItemReadListener<Integer> integerItemReadListener){
		return stepBuilderFactory.get("LISTENER_STEP_CHUNK_ORIENTED")
				.<Integer, Integer>chunk(2)
				.reader(new ItemReader<Integer>() {
					Queue<Integer> integerQueue = new ArrayDeque<>(Arrays.asList(1,2,3));
					@Override
					public Integer read() {
						System.out.println("Reading...");
						return integerQueue.poll();
					}
				})
				.writer(System.out::println)
				.listener(integerItemReadListener)
				.build();
	}

	@Bean
	public ItemReadListener<Integer> integerItemReadListener(){
		return new ItemListenerSupport<Integer, Integer>(){
			@Override
			public void beforeRead() {
				System.out.println("Before read");
			}

			@Override
			public void afterRead(Integer item) {
				System.out.println("After read");
			}

			@Override
			public void afterWrite(List<? extends Integer> item) {
				System.out.println("After write, chunk size " + item.size());
			}

			@Override
			public void beforeWrite(List<? extends Integer> item) {
				System.out.println("before write, chunk size " + item.size());
			}
		};
	}

	@Bean
	public StepListener stepListener(){
		return new StepListenerSupport<Integer, Integer>(){
			@Override
			public void beforeStep(StepExecution stepExecution) {
				System.out.println("Before Step");
			}

			@Override
			public ExitStatus afterStep(StepExecution stepExecution) {
				System.out.println("After Step");
				return stepExecution.getExitStatus();
			}

			@Override
			public void afterChunk(ChunkContext context) {
				System.out.println("After chunk");
			}

			@Override
			public void beforeChunk(ChunkContext context) {
				System.out.println("Before chunk");			}
		};
	}
}
