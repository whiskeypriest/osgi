package net.whiskeypriest.aop;

import java.lang.reflect.Method;

/**
 * Contains the invocation information for an intercepted method.
 *
 * @author flammer
 */
public interface InvocationContext {
	/**
	 * Executes the intercepted method.
	 */
	Object proceed() throws Exception;

	/**
	 * @return the original instance containing the method which was intercepted
	 */
	Object getRoot();

	/**
	 * @return the proxy instance which the method is being called on now
	 */
	Object getProxy();

	/**
	 * @return the method that has been intercepted
	 */
	Method getMethod();

	/**
	 * @return the arguments passed to the intercepted method
	 */
	Object[] getArguments();
}
