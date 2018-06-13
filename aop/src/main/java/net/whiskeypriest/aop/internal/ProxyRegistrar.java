package net.whiskeypriest.aop.internal;

import static net.whiskeypriest.aop.internal.ProxyConstants.AOP_PROXY;

import java.util.Properties;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

/**
 * This class turns a ServiceProxy class into a bona fide interceptor. Then the
 * proxy is registered as a service and, for all intents and purposes, replaces
 * the original service..
 *
 * @author flammer
 */
public class ProxyRegistrar {
	private final Object proxy;
	private final ServiceReference service;

	public ProxyRegistrar(Object proxy, ServiceReference service) {
		this.proxy = proxy;
		this.service = service;
	}

	public void register() {
		BundleContext context = service.getBundle().getBundleContext();
		context.registerService(getInterfaceNames(), proxy, getProperties());
	}

	private Properties getProperties() {
		Properties properties = new Properties();
		for (String property : service.getPropertyKeys()) {
			properties.put(property, service.getProperty(property));
		}
		int serviceRanking = getServiceRanking();
		properties.put(AOP_PROXY, true);
		properties.put(Constants.SERVICE_RANKING, serviceRanking + 1);
		return properties;
	}

	private int getServiceRanking() {
		Integer serviceRanking = (Integer) service.getProperty(Constants.SERVICE_RANKING);
		if (serviceRanking == null) {
			serviceRanking = 0;
		}
		return serviceRanking;
	}

	private String[] getInterfaceNames() {
		Class<?>[] interfaces = getInterfaces();
		String[] interfaceNames = new String[interfaces.length];
		for (int i = 0; i < interfaces.length; i++) {
			interfaceNames[i] = interfaces[i].getName();
		}
		return interfaceNames;
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
