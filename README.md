# OSGI Libraries


### AOP

Include this OSGI library in your webserver to add AOP into your services.

To get started, implement the ServiceProxy interface in your interceptor.
```
@Component
@Service
public class LoggingProxy implements ServiceProxy {
   @Override
   public Object intercept(InvocationContext context) {
      // logging goes here
      return context.proceed();
   }
}
```
Now when you call any service methods, they will be intercepted by LoggingProxy.

To see a more complete example, take a look at the Transactional project.

### Transactional
This library provides an annotation interceptor called `@Transactional`. This annotation provides transaction management with the JTA TransactionManager which would normally be explicitly called in the OSGI context.

```
public class ExampleService {
   @Transactional(rollbackOn = Exception.class)
   public saveExampleObject(ExampleObject object) {
      ...
   }
}
```
The transaction management boilerplate is now reduced to the annotation, as in the Java EE context.