package gratis.contoh.api.util.web;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;

public class HttpRequest {

	public static String getHeader(ServerHttpRequest request, String headerName) {
		HttpHeaders headers = request.getHeaders();
		List<String> headersStr = headers.get(headerName);
		
		if (headersStr == null || headersStr.size() == 0) {
			return null;
		}
		
		return headersStr.get(0);
	}
}
