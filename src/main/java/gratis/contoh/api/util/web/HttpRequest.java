package gratis.contoh.api.util.web;

import org.springframework.http.server.reactive.ServerHttpRequest;

public class HttpRequest {

	public static String getHeader(ServerHttpRequest request, String headerName) {
		return request.getHeaders().getFirst(headerName);
	}
}
