package net.whiskeypriest.transactional.internal;

import java.lang.reflect.Method;

import javax.transaction.TransactionManager;

import net.whiskeypriest.aop.InvocationContext;
import net.whiskeypriest.aop.ServiceProxy;
import net.whiskeypriest.transactional.Transactional;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import net.whiskeypriest.transactional.internal.interceptor.TransactionalInvocation;

/**
 * Service proxy that creates a transaction.
 *
 * @author flammer
 */
@Component
@Service
public class TransactionalProxy implements ServiceProxy {
	@Reference(bind = "setTransactionManager")
	private TransactionManager transactionManager;

	@Override
	public Object intercept(InvocationContext context) throws Exception {
		Transactional transactional = readTransactional(context);
		TransactionalInvocation transactionalInvocation = new TransactionalInvocation(context,
				transactional, transactionManager);
		transactionalInvocation.execute();
		return transactionalInvocation.getInvocationResults();
	}

	private Transactional readTransactional(InvocationContext context) throws Exception {
		Transactional transactional = readTransactionalFromMethod(context);
		if (transactional == null) {
			transactional = readTransactionalFromClass(context);
		}
		return transactional;
	}

	private Transactional readTransactionalFromMethod(InvocationContext context) throws Exception {
		Method method = context.getMethod();
		Object root = context.getRoot();
		Method rootMethod = root.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
		return rootMethod.getAnnotation(Transactional.class);
	}

	private Transactional readTransactionalFromClass(InvocationContext context) {
		Object root = context.getRoot();
		return root.getClass().getAnnotation(Transactional.class);
	}

	public void setTransactionManager(TransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
}
