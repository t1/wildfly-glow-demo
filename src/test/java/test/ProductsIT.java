package test;

import io.smallrye.graphql.client.typesafe.api.GraphQLClientApi;
import io.smallrye.graphql.client.typesafe.api.TypesafeGraphQLClientBuilder;
import lombok.Data;
import org.eclipse.microprofile.graphql.Query;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.BDDAssertions.then;

class ProductsIT {
    private static final HttpClient HTTP = HttpClient.newHttpClient();

    static {
        configure("base-uri", "http://localhost:8080");
        configure("products/mp-graphql/url", System.getProperty("base-uri") + "/graphql");
    }

    private static void configure(String key, String value) {
        if (System.getProperty(key) == null) System.setProperty(key, value);
    }

    @GraphQLClientApi(configKey = "products")
    interface Api {
        @Query Product product(int id);
    }

    @Data
    static class Product {
        String name;
    }

    Api api = TypesafeGraphQLClientBuilder.newBuilder().build(Api.class);

    @Test
    void shouldGetProductOne() {
        var product = api.product(1);

        then(product.getName()).isEqualTo("Product-1");
    }

    @Test
    void shouldGetProductOne_REST() throws Exception {
        var response = http(GET("/rest/products/1").header("Accept", APPLICATION_JSON));

        then(response.statusCode()).isEqualTo(200);
        then(response.body()).isEqualTo("{\"id\":1,\"name\":\"Product-1\"}");
    }

    @Test
    void shouldHaveUnchangedSchema() throws Exception {
        var response = http(GET("/graphql/schema.graphql"));

        then(response.statusCode()).isEqualTo(200);
        then(response.body()).isEqualTo(Files.readString(Path.of("schema.graphql")));
    }

    private static HttpResponse<String> http(HttpRequest.Builder request) throws IOException, InterruptedException {
        return HTTP.send(request.build(), BodyHandlers.ofString());
    }

    private static HttpRequest.Builder GET(String url) {
        return HttpRequest.newBuilder().GET().uri(URI.create(System.getProperty("base-uri") + url));
    }
}
