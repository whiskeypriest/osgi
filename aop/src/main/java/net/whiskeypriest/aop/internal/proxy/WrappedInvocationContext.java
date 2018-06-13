package net.whiskeypriest.aop.internal.proxy;

import java.lang.reflect.Method;

import net.whiskeypriest.aop.InvocationContext;
import net.whiskeypriest.aop.ServiceProxy;

/**
 * Invocation context proxy which will invoke intercepted methods.
 *
 * @author flammer
 */
public class WrappedInvocationContext implements InvocationContext {
	private final ServiceProxy proxy;
	private final InvocationContext context;

	public WrappedInvocationContext(ServiceProxy proxy, InvocationContext context) {
		this.proxy = proxy;
		this.context = context;
	}

	@Override
	public Object proceed() throws Exception {
		return proxy.intercept(context);
	}

	@Override
	public Object getRoot() {
		return context.getRoot();
	}

	@Override
	public Object getProxy() {
		return context.getProxy();
	}

	@Override
	public Method getMethod() {
		return context.getMethod();
	}

	@Override
	public Object[] getArguments() {
		return context.getArguments();
	}
}
