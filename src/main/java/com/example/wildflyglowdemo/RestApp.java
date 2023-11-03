package com.example.wildflyglowdemo;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "Products API", version = "1.0.0"))
@ApplicationPath("/rest")
public class RestApp extends Application {}
