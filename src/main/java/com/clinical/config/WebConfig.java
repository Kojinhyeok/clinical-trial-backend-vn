package com.clinical.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/admin", "/admin/index.html");
        registry.addRedirectViewController("/admin/", "/admin/index.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/admin/**")
                .addResourceLocations("classpath:/static/admin/")
                .resourceChain(true)
                .addResolver(new HtmlExtensionResourceResolver());
    }

    private static class HtmlExtensionResourceResolver implements ResourceResolver {

        private final PathResourceResolver defaultResolver = new PathResourceResolver();

        @Override
        public Resource resolveResource(HttpServletRequest request, String requestPath,
                                        List<? extends Resource> locations, ResourceResolverChain chain) {
            Resource resource = chain.resolveResource(request, requestPath, locations);
            if (resource != null) {
                return resource;
            }

            if (!requestPath.contains(".")) {
                resource = chain.resolveResource(request, requestPath + ".html", locations);
            }

            return resource;
        }

        @Override
        public String resolveUrlPath(String resourcePath, List<? extends Resource> locations,
                                     ResourceResolverChain chain) {
            String resolved = chain.resolveUrlPath(resourcePath, locations);
            if (resolved != null) {
                return resolved;
            }

            if (!resourcePath.contains(".")) {
                resolved = chain.resolveUrlPath(resourcePath + ".html", locations);
            }

            return resolved;
        }
    }
}