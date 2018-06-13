package net.whiskeypriest.aop.internal;


/**
 * Wrapper for service with some extra methods specific to aop.
 *
 * @author flammer
 */
final class ProxyConstants {
	/** no instances allowed. */
	private ProxyConstants() {}

	/** identifier property for service proxies. */
	static final String AOP_PROXY = "aop-proxy";
}
