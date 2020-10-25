package com.example.task;

import com.example.task.service.IndexDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RequiredArgsConstructor
public class TaskApplication {

	private final IndexDataService indexDataService;

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}

	@PostConstruct
	void started() {
		indexDataService.index();
	}

}
