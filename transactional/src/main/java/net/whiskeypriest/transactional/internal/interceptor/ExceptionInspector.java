package net.whiskeypriest.transactional.internal.interceptor;

import net.whiskeypriest.transactional.Transactional;


/**
 * This class figures out the appropriate commit/rollback rules for an
 * exception.
 *
 * @author flammer
 */
public class ExceptionInspector {
	private final Transactional transactional;
	private final Exception exception;

	public ExceptionInspector(Transactional transactional, Exception exception) {
		this.transactional = transactional;
		this.exception = exception;
	}

	public boolean exceptionRequiresRollback() {
		boolean rollback = exception != null;
		if (rollback) {
			rollback = exceptionIsOnBlacklist();
		}
		if (rollback) {
			rollback = exceptionIsNotExemptFromBlacklist();
		}
		return rollback;
	}

	private boolean exceptionIsOnBlacklist() {
		return exceptionIsIncludedType(transactional.rollbackOn());
	}

	private boolean exceptionIsNotExemptFromBlacklist() {
		return !exceptionIsIncludedType(transactional.ignore());
	}

	private boolean exceptionIsIncludedType(Class<? extends Exception>[] exceptionTypes) {
		for (Class<? extends Exception> blacklistException : exceptionTypes) {
			if (blacklistException.isAssignableFrom(exception.getClass())) {
				return true;
			}
		}
		return false;
	}
}
