package net.rush.task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.rush.Server;

/**
 * A class which schedules {@link Task}s.

 */
public final class TaskScheduler {

	/**
	 * The logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(TaskScheduler.class.getName());

	/**
	 * The number of milliseconds between pulses.
	 */
	private static final int PULSE_EVERY = 50;

	/**
	 * The server.
	 */
	private final Server server;

	/**
	 * The scheduled executor service which backs this scheduler.
	 */
	private final ScheduledExecutorService scheduleExecutor = Executors.newSingleThreadScheduledExecutor();
	
	private final ExecutorService asyncExecutor = Executors.newWorkStealingPool();

	/**
	 * A list of new tasks to be added.
	 */
	private final List<Task> newTasks = new ArrayList<Task>();

	/**
	 * A list of active tasks.
	 */
	private final List<Task> tasks = new ArrayList<Task>();
	
    /**
     * The primary scheduler thread in which pulse() is called.
     */
    private Thread primaryThread;

	/**
	 * Creates a new task scheduler.
	 * @param server The server.
	 */
	public TaskScheduler(Server server) {
		this.server = server;
	}

	long time = 0;
	long lastWarning = 0;
	
	/**
	 * Starts the task scheduler.
	 */
	public void start() {
		scheduleExecutor.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				int[] lags = {0, 0};
				try {
					time = System.currentTimeMillis();
					lags = pulse();
				} catch (StackOverflowError err) {
					logger.log(Level.SEVERE, "Stack overflow error in task scheduler, server shutting down.", err);
					System.exit(0);
				} catch (Throwable t) {
					logger.log(Level.SEVERE, "Uncaught exception in task scheduler.", t);
					// TODO in the future consider shutting down the server at this point?
				} finally {
					long localTime = System.currentTimeMillis();
					long lag = localTime - time;

					// TODO Revert to 2000
					if (lag > 20L && (time - lastWarning) >= 15000) {
						// prints the lag from session registry and then from world pulsing.
						logger.warning("Can\'t keep up! Lag[session=" + lags[0] + "ms,world=" + lags[1] + "ms]");
						lag = 20L;
						lastWarning = time;
					}

					if (lag < 0L) {
						logger.warning("Time ran backwards! Did the system time change?");
						lag = 0L;
					}
					time = localTime;
				}

			}
		}, 0, PULSE_EVERY, TimeUnit.MILLISECONDS);
	}

	/**
	 * Schedules the specified task 
	 * till the server shutdown.
	 * @param task The task.
	 */
	public void schedule(Task task) {
		synchronized (newTasks) {
			newTasks.add(task);
		}
	}

	/**
	 * Runs specified task once asynchronously.
	 */
	public void runTaskAsync(Runnable task) {
		asyncExecutor.submit(task);
	}
	
	/**
	 * Runs the specified task once after the ticks in dealy.
	 * @param delayTicks how long to postpone the task
	 */
	public void runTaskSyncLater(Runnable task, int delayTicks) {
		scheduleExecutor.schedule(task, delayTicks * 50, TimeUnit.MILLISECONDS);
	}

    /**
     * Returns true if the current {@link Thread} is the server's primary thread.
     */
    public boolean isPrimaryThread() {
        return Thread.currentThread() == primaryThread;
    }
	
	/**
	 * Adds new tasks and updates existing tasks, removing them if necessary.
	 * @return 2 integers. One is lag from session registry pulsing, second from world pulsing.
	 */
	private int[] pulse() {
        if (primaryThread == null) {
            primaryThread = Thread.currentThread();
            primaryThread.setName("Rush Core Thread");
        }
		
		int[] lag = new int[2];
		// handle incoming messages
		lag[0] = server.getSessionRegistry().pulse();

		// handle tasks
		synchronized (newTasks) {
			for (Task task : newTasks)
				tasks.add(task);

			newTasks.clear();
		}

		for (Iterator<Task> it = tasks.iterator(); it.hasNext(); ) {
			Task task = it.next();
			if (!task.pulse()) {
				it.remove();
			}
		}

		// handle general game logic
		lag[1] = server.getWorld().pulse();

		return lag;
	}

}

