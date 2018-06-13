package net.whiskeypriest.aop.internal;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import net.whiskeypriest.aop.internal.proxy.ProxyHandler;
import net.whiskeypriest.aop.internal.proxy.ServiceProxyManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

/**
 * Responsible for generating a proxy instance to register as a service.
 *
 * @author flammer
 */
public class ProxyCreator {
	private final BundleContext context;
	private final ServiceReference service;
	private final ServiceProxyManager proxyManager;

	public ProxyCreator(BundleContext context, ServiceReference service, ServiceProxyManager proxyManager) {
		this.context = context;
		this.service = service;
		this.proxyManager = proxyManager;
	}

	/**
	 * Create a proxy instance for a service.
	 */
	public Object create() {
		InvocationHandler proxy = generateProxyHandler();
		return implementProxy(proxy);
	}

	private InvocationHandler generateProxyHandler() {
		return new ProxyHandler(context, service, proxyManager);
	}

	private Object implementProxy(InvocationHandler proxy) {
		return Proxy.newProxyInstance(getClassLoader(), getInterfaces(), proxy);
	}


	private ClassLoader getClassLoader() {
		return context.getService(service).getClass().getClassLoader();
	}

	private Class<?>[] getInterfaces() {
		Bundle bundle = service.getBundle();
		String[] interfaceClasses = (String[]) service.getProperty(Constants.OBJECTCLASS);
		Class<?>[] interfaces = new Class<?>[interfaceClasses.length];
		for (int i = 0; i < interfaceClasses.length; i++) {
			try {
				interfaces[i] = bundle.loadClass(interfaceClasses[i]);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return interfaces;
	}
}
