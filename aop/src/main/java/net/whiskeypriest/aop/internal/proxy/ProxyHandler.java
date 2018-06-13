package net.whiskeypriest.aop.internal.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import net.whiskeypriest.aop.InvocationContext;

/**
 * Service-Aware invocation handler which passes the work to the service proxy
 * manager for interception.
 *
 * @author flammer
 */
public class ProxyHandler implements InvocationHandler {
	private final BundleContext context;
	private final ServiceReference service;
	private final ServiceProxyManager proxyManager;

	public ProxyHandler(BundleContext context, ServiceReference service, ServiceProxyManager proxyManager) {
		this.context = context;
		this.service = service;
		this.proxyManager = proxyManager;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object root = context.getService(service);
		InvocationContext context = new DefaultInvocationContext(root, proxy, method, args);
		return proxyManager.handle(context);
	}
}
