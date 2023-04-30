package gratis.contoh.api.util.annotation;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.http.HttpHeaders;

@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface Authorize {

	String header() default HttpHeaders.AUTHORIZATION;

	String[] roles() default {};
	
	String[] modules() default {};
	
	String[] accessTypes() default {};
	
}
