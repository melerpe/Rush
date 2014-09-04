package net.rush.task;

import java.util.LinkedList;
import java.util.Queue;

import net.rush.Server;
import net.rush.model.entity.ai.EntityAI;

/** TODO Consider minor rewrite? */
public class WorldThreadAI extends Thread {

	private Queue<EntityAI> taskQueue = new LinkedList<>();
	private final Server server;

	public WorldThreadAI() {
		this.server = Server.getServer();
	}

	public void addToQueue(EntityAI task) {
		taskQueue.add(task);
	}
	
	@Override
	public void run() {			
		while (server.isRunning) {
			EntityAI task = taskQueue.poll();

			if (task != null) 
				task.pulse();
		}
	}

}
