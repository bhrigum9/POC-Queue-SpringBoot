package com.example.queue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReadWriteQueue {
	@RequestMapping("/test")
	@ResponseBody
	public static String readWrite() throws InterruptedException, ExecutionException {

		LinkedList<String> data = new LinkedList<>();

		ReadQueue reader = new ReadQueue(data);
		WriteQueue writer = new WriteQueue(data);

		try {
			File resource = new ClassPathResource("queueDataTest.json").getFile();
			String text = new String(Files.readAllBytes(resource.toPath()));
			List<String> list = Arrays.asList(text.replace("[", "").replace("]", ""));
			writer.put(list);
		} catch (IOException e1) {
			System.out.println(e1.getMessage());
		}

		try {
			List<String> list = reader.getAll();
			list.stream().forEach(System.out::println);
			return list.toString();

		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		} catch (ExecutionException e) {
			System.out.println(e.getMessage());

		} finally {
			reader.shutdown();
		}
		return null;

	}
}
