package gratis.contoh.api.configuration;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfiguration {
	
	@Bean
	public GroupedOpenApi mstLanguangeMappingOpenApi(
			@Value("${springdoc.version}") String appVersion) {
		String[] paths = { "/api/v1/**" };
		return GroupedOpenApi.builder().
				group("v1")
				.addOpenApiCustomizer(openApi -> openApi.info(new Info()
						.title("Springboot Webflux").version(appVersion)))
				.pathsToMatch(paths)
				.build();
	}

}
