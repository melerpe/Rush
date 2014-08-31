package net.rush.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.rush.model.entity.ai.EntityAI;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

/** TODO Consider minor rewrite? */
public class WorldTaskAI {

	private static ExecutorService executor = Executors.newFixedThreadPool(1, new ThreadFactoryBuilder().setNameFormat("Entity AI IO YOLO").build());

	public static void addTask(final EntityAI task) {
		executor.submit(new Runnable() {
			
			@Override
			public void run() {
				task.pulse();
			}
		});
	}
}
