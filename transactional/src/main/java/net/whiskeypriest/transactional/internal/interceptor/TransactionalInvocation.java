package net.whiskeypriest.transactional.internal.interceptor;

import javax.transaction.Status;
import javax.transaction.TransactionManager;

import net.whiskeypriest.aop.InvocationContext;
import net.whiskeypriest.transactional.Transactional;


/**
 * Handles an invocation with a transaction if necessary. Closes the transaction
 * after the transaction, committing or rolling back according to @Transactional
 * rules.
 *
 * @author flammer
 */
public class TransactionalInvocation {
	private final InvocationContext context;
	private final Transactional transactional;
	private final TransactionManager transactionManager;
	private boolean beginning;
	private Object results;
	private Exception exception;

	public TransactionalInvocation(InvocationContext context, Transactional transactional,
			TransactionManager transactionManager) {
		this.context = context;
		this.transactional = transactional;
		this.transactionManager = transactionManager;
	}

	public void execute() throws Exception {
		if (transactional()) {
			proceedWithTransaction();
		} else {
			proceedWithoutTransaction();
		}
	}

	private void proceedWithoutTransaction() {
		proceed();
	}

	private void proceedWithTransaction() throws Exception {
		beginTransactionIfNecessary();
		proceed();
		commitOrRollback();
	}

	public Object getInvocationResults() throws Exception {
		if (exception != null) {
			throw exception;
		}
		return results;
	}

	private void beginTransactionIfNecessary() throws Exception {
		if (transactionManager.getStatus() == Status.STATUS_NO_TRANSACTION) {
			transactionManager.begin();
			beginning = true;
		}
	}

	private void proceed() {
		try {
			results = context.proceed();
		} catch (Exception e) {
			exception = e;
		}
	}

	private void commitOrRollback() throws Exception {
		boolean rollback = requireRollback();
		if (rollback) {
			rollback();
		} else {
			commit();
		}
	}

	private void rollback() throws Exception {
		if (beginning) {
			transactionManager.rollback();
		} else {
			transactionManager.setRollbackOnly();
		}
	}

	private void commit() throws Exception {
		if (beginning) {
			transactionManager.commit();
		}
	}

	private boolean requireRollback() {
		ExceptionInspector inspector = new ExceptionInspector(transactional, exception);
		return inspector.exceptionRequiresRollback();
	}

	private boolean transactional() {
		return transactional != null;
	}

}
