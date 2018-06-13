package net.whiskeypriest.aop.internal;

import static net.whiskeypriest.aop.internal.ProxyConstants.AOP_PROXY;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

/**
 * Filters services from the ProxyFindHook to get all proxied services.
 *
 * @author flammer
 */
class ProxyFindFilter {
	private final Collection<?> services;
	private final Set<ServiceReference> proxyServices = new HashSet<ServiceReference>();
	private final Set<ServiceReference> proxiedServices = new HashSet<ServiceReference>();

	public ProxyFindFilter(Collection<?> services) {
		this.services = services;
		locateProxiedServices();
	}

	public boolean serviceIsProxied(ServiceReference service) {
		return proxiedServices.contains(service);
	}

	private void locateProxiedServices() {
		captureProxyServices();
		captureProxiedServices();
	}

	private void captureProxyServices() {
		for (Object serviceObject : services) {
			ServiceReference service = (ServiceReference) serviceObject;
			if (serviceIsProxy(service)) {
				proxyServices.add(service);
			}
		}
	}

	private void captureProxiedServices() {
		for (Object serviceObject : services) {
			ServiceReference service = (ServiceReference) serviceObject;
			if (proxiesContainService(service)) {
				proxiedServices.add(service);
			}
		}
	}

	private boolean serviceIsProxy(ServiceReference service) {
		return Boolean.TRUE == service.getProperty(AOP_PROXY);
	}

	private boolean proxiesContainService(ServiceReference service) {
		String servicePid = (String) service.getProperty(Constants.SERVICE_PID);
		if (servicePid != null) {
			return servicesAreRelated(service, servicePid);
		}
		return false;
	}

	private boolean servicesAreRelated(ServiceReference service, String servicePid) {
		for (ServiceReference proxyService : proxyServices) {
			String proxyPid = (String) proxyService.getProperty(Constants.SERVICE_PID);
			if (servicePid.equals(proxyPid) && service != proxyService) {
				return true;
			}
		}
		return false;
	}
}
