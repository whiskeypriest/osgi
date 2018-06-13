package net.whiskeypriest.aop.internal.proxy;

import java.lang.reflect.Method;

import net.whiskeypriest.aop.InvocationContext;

/**
 * Holds information for about a method invocation.
 *
 * @author flammer
 */
public class DefaultInvocationContext implements InvocationContext {
	private final Object root;
	private final Object proxy;
	private final Method method;
	private final Object[] arguments;

	public DefaultInvocationContext(Object root, Object proxy, Method method, Object[] arguments) {
		this.root = root;
		this.proxy = proxy;
		this.method = method;
		this.arguments = arguments;
	}

	@Override
	public Object proceed() throws Exception {
		return method.invoke(root, arguments);
	}

	@Override
	public Object getRoot() {
		return root;
	}

	@Override
	public Object getProxy() {
		return proxy;
	}

	@Override
	public Method getMethod() {
		return method;
	}

	@Override
	public Object[] getArguments() {
		return arguments;
	}
}
