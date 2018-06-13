package net.whiskeypriest.aop.internal.proxy;

import java.util.List;

import net.whiskeypriest.aop.InvocationContext;
import net.whiskeypriest.aop.ServiceProxy;

/**
 * Responsible for managing interceptors and passing an invocation through the
 * available interceptors.
 *
 * @author flammer
 */
public class ServiceProxyManager {
	private final List<ServiceProxy> serviceProxies;

	public ServiceProxyManager(List<ServiceProxy> serviceProxies) {
		this.serviceProxies = serviceProxies;
	}

	/**
	 * Adds interceptors to a method.
	 */
	public Object handle(InvocationContext context) throws Exception {
		InvocationContext wrappedContext = context;
		ServiceProxy lastProxy = new DefaultProxy();
		for (ServiceProxy proxy : serviceProxies) {
			wrappedContext = wrapContext(lastProxy, wrappedContext);
			lastProxy = proxy;
		}
		return lastProxy.intercept(wrappedContext);
	}

	private InvocationContext wrapContext(ServiceProxy proxy, InvocationContext context) {
		return new WrappedInvocationContext(proxy, context);
	}
}
