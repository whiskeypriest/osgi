package net.whiskeypriest.aop.internal.proxy;

import net.whiskeypriest.aop.InvocationContext;
import net.whiskeypriest.aop.ServiceProxy;

/**
 * A pass-through interceptor.
 *
 * @author flammer
 */
public class DefaultProxy implements ServiceProxy {
	@Override
	public Object intercept(InvocationContext context) throws Exception {
		return context.proceed();
	}

}
