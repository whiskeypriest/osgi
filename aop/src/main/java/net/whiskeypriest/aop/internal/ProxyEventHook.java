package net.whiskeypriest.aop.internal;

import static net.whiskeypriest.aop.internal.ProxyConstants.AOP_PROXY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.hooks.service.EventHook;
import net.whiskeypriest.aop.ServiceProxy;
import net.whiskeypriest.aop.internal.proxy.ServiceProxyManager;

/**
 * This is a framework class to automatically register interceptors and apply
 * the registered interceptors to services that have been deployed since.
 *
 * This is done by registering a second service which is a proxy to the first.
 * The proxy has a higher priority, so it will be used instead of the original.
 *
 * @author flammer
 */
public class ProxyEventHook implements EventHook {
	private final List<ServiceProxy> proxies = new ArrayList<ServiceProxy>();
	private final ServiceProxyManager proxyManager = new ServiceProxyManager(proxies);
	private BundleContext context;

	@SuppressWarnings("rawtypes")
	@Override
	public void event(ServiceEvent event, Collection contexts) {
		if (event.getType() == ServiceEvent.REGISTERED) {
			registerProxyIfNecessary(event);
		}
	}

	private void registerProxyIfNecessary(ServiceEvent event) {
		ServiceReference service = event.getServiceReference();
		if (proxyShouldBeCreated(service)) {
			Object proxy = createProxy(service);
			registerProxy(service, proxy);
		}
	}

	private void registerProxy(ServiceReference service, Object proxyToRegister) {
		if (proxyToRegister != null) {
			new ProxyRegistrar(proxyToRegister, service).register();
		}
	}

	private Object createProxy(ServiceReference service) {
		ProxyCreator proxyCreator = new ProxyCreator(context, service, proxyManager);
		return proxyCreator.create();
	}

	private boolean proxyShouldBeCreated(ServiceReference service) {
		return notProxied(service) && eventNotFromProxyBundle(service) && notProxy(service);
	}

	private boolean notProxied(ServiceReference service) {
		return service.getProperty(AOP_PROXY) == null;
	}

	private boolean eventNotFromProxyBundle(ServiceReference service) {
		BundleContext serviceBundle = service.getBundle().getBundleContext();
		return context != serviceBundle;
	}

	private boolean notProxy(ServiceReference service) {
		Class<?> serviceClass = context.getService(service).getClass();
		return !ServiceProxy.class.isAssignableFrom(serviceClass);
	}

	public void activate(BundleContext context) {
		this.context = context;
	}

	public void addProxy(ServiceProxy proxy) {
		proxies.add(proxy);
	}

	public void removeProxy(ServiceProxy proxy) {
		proxies.remove(proxy);
	}
}
