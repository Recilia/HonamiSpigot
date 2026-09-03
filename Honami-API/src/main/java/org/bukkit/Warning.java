package org.bukkit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

@Target({ ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Warning {

	public enum WarningState {

		ON,

		OFF,

		DEFAULT;

		private static final Map<String, WarningState> values = ImmutableMap.<String, WarningState>builder()
				.put("off", OFF).put("false", OFF).put("f", OFF).put("no", OFF).put("n", OFF).put("on", ON)
				.put("true", ON).put("t", ON).put("yes", ON).put("y", ON).put("", DEFAULT).put("d", DEFAULT)
				.put("default", DEFAULT).build();

		public boolean printFor(Warning warning) {
			if (this == DEFAULT) {
				return warning == null || warning.value();
			}
			return this == ON;
		}

		public static WarningState value(final String value) {
			if (value == null) {
				return DEFAULT;
			}
			WarningState state = values.get(value.toLowerCase());
			if (state == null) {
				return DEFAULT;
			}
			return state;
		}
	}

	boolean value() default false;

	String reason() default "";
}
