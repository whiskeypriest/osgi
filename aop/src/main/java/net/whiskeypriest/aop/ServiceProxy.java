package net.whiskeypriest.aop;

/**
 * This is an interceptor for Service method invocations.
 *
 * @author flammer
 */
public interface ServiceProxy {
	/**
	 * intercept a service invocation.
	 */
	Object intercept(InvocationContext context) throws Exception;
}
