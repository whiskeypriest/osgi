package net.whiskeypriest.aop.internal;

import java.util.Collection;
import java.util.Iterator;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.hooks.service.FindHook;

/**
 * Framework class to hide services that have been proxied, so that the proxy
 * shows up instead of the unproxied service.
 *
 * @author flammer
 */
public class ProxyFindHook implements FindHook {
	private BundleContext context;

	@SuppressWarnings("rawtypes")
	@Override
	public void find(BundleContext requestorContext, String name, String filter, boolean allServices,
			Collection references) {
		if (frameworkContext(requestorContext)) {
			return;
		}
		ProxyFindFilter proxyFilter = new ProxyFindFilter(references);
		Iterator<?> iterator = references.iterator();
		while (iterator.hasNext()) {
			ServiceReference service = (ServiceReference) iterator.next();
			if (proxyFilter.serviceIsProxied(service)) {
				iterator.remove();
			}
		}
	}

	private boolean frameworkContext(BundleContext requestorContext) {
		return context.equals(requestorContext) || requestorContext.getBundle().getBundleId() == 0;
	}

	public void activate(BundleContext context) {
		this.context = context;
	}
}
