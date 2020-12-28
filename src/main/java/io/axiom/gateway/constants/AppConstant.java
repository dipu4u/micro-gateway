package io.axiom.gateway.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class AppConstant {

	public static final String SERVER_PORT = "server.port";
	public static final String CHARSET_KEY = "request.charset";
	public static final String HTTP_RESPONSE = "http.response";

	public static final int BUFFER_LIMIT = 256;
	public static final Charset UTF_8_CHARSET = StandardCharsets.UTF_8;
}
