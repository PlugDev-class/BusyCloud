package de.plugdev.cloud.lang;

import java.lang.annotation.*;

public final class ApiStatus {

	public ApiStatus() { throw new AssertionError("ApiStatus should not be instantiated"); }

	@Retention(RetentionPolicy.CLASS)
	@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR,
			ElementType.FIELD, ElementType.PACKAGE })
	public @interface Experimental {}

	@Retention(RetentionPolicy.CLASS)
	@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR,
			ElementType.FIELD, ElementType.PACKAGE })
	public @interface Internal {}
	
	@Retention(RetentionPolicy.CLASS)
	@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR,
			ElementType.FIELD, ElementType.PACKAGE })
	public @interface External {}
	
	@Retention(RetentionPolicy.CLASS)
	@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR,
			ElementType.FIELD, ElementType.PACKAGE })
	public @interface Between {}
	
}
