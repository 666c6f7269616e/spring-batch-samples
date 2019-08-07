package com.labs.springbatchsamples;

import com.labs.springbatchsamples.dao.CustomerRepository;
import com.labs.springbatchsamples.domain.Customer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchSamplesApplication implements CommandLineRunner {

	private final CustomerRepository customerRepository;

	public SpringBatchSamplesApplication(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchSamplesApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		customerRepository.saveAll(
				Arrays.asList(
						Customer.builder().name("Elvis").build(),
						Customer.builder().name("Sam").build(),
						Customer.builder().name("Peter").build(),
						Customer.builder().name("John").build()));
	}
}
