package io.axiom.gateway.utils;

public final class CommonUtils {

	public static boolean isEmpty(String data) {
		return data == null || data.trim().isEmpty();
	}
	
	public static boolean isNonNullEmpty(String data) {
		return data != null && !data.trim().isEmpty();
	}

	private CommonUtils() {}
}
