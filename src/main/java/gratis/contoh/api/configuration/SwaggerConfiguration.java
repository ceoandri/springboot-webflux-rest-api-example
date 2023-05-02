package gratis.contoh.api.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfiguration {
	
	@Bean
	public GroupedOpenApi mstLanguangeMappingOpenApi(
			@Value("${springdoc.version}") String appVersion) {
		String[] paths = { "/api/v1/mst-language-mapping/**" };
		return GroupedOpenApi.builder().
				group("mst-language-mapping v1")
				.addOpenApiCustomizer(openApi -> {
					final String securitySchemeName = "Access Token";
					openApi.info(new Info()
							.title("Master Language Mapping API Documentation").version(appVersion))
					.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
					.getComponents().addSecuritySchemes(securitySchemeName, new SecurityScheme()
			            .type(SecurityScheme.Type.APIKEY)
			            .in(SecurityScheme.In.HEADER)
			            .name(HttpHeaders.AUTHORIZATION));
				})
				.pathsToMatch(paths)
				.build();
	}
	
	@Bean
	public GroupedOpenApi authOpenApi(
			@Value("${springdoc.version}") String appVersion) {
		String[] paths = { "/api/v1/auth/**" };
		return GroupedOpenApi.builder().
				group("auth v1")
				.addOpenApiCustomizer(openApi -> openApi.info(new Info()
						.title("Authentication API Documentation").version(appVersion)))
				.pathsToMatch(paths)
				.build();
	}
	
	@Bean
	public GroupedOpenApi accountOpenApi(
			@Value("${springdoc.version}") String appVersion) {
		String[] paths = { "/api/v1/account/**" };
		return GroupedOpenApi.builder().
				group("account v1")
				.addOpenApiCustomizer(openApi -> {
					final String securitySchemeName = "Access Token";
					openApi.info(new Info()
							.title("Account API Documentation").version(appVersion))
					.addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
					.getComponents().addSecuritySchemes(securitySchemeName, new SecurityScheme()
			            .type(SecurityScheme.Type.APIKEY)
			            .in(SecurityScheme.In.HEADER)
			            .name(HttpHeaders.AUTHORIZATION));
				})
				.pathsToMatch(paths)
				.build();
	}

}
