package net.rush.protocol;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;

/**
 * A list of all the sessions which provides a convenient {@link #pulse()}
 * method to pulse every session in one operation.

 */
public final class SessionRegistry {

	/**
	 * An array of sessions that have not yet been added.
	 */
	private final Queue<Session> pending = new ArrayDeque<Session>();

	/**
	 * A list of the sessions.
	 */
	private final HashSet<Session> sessions = new HashSet<Session>();

	/**
	 * Pulses all the sessions.
	 */
	public int pulse() {
		long now = System.currentTimeMillis();
		synchronized (pending) {
			Session session;
			while ((session = pending.poll()) != null)
				sessions.add(session);
			
		}

		for (final Iterator<Session> it = sessions.iterator(); it.hasNext(); ) {
			Session session = it.next();
			
			if (!session.pulse()) {
				it.remove();
				session.dispose();
			}
		}
		return (int) (System.currentTimeMillis() - now);
	}

	/**
	 * Adds a new session.
	 * @param session The session to add.
	 */
	void add(Session session) {
		synchronized (pending) {
			pending.add(session);
		}
	}

	/**
	 * Removes a session.
	 * @param session The session to remove.
	 */
	void remove(Session session) {
		session.flagForRemoval();
	}

}

