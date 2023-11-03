package com.example.wildflyglowdemo;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Path("/products")
@GraphQLApi
@Slf4j
public class Products {
    private static final Map<Integer, Product> products = IntStream.range(1, 6).boxed()
            .collect(toMap(identity(), id -> Product.builder().id(id).name("Product-" + id).build()));

    @GET
    @Query public List<Product> products() {
        log.info("products()");
        return new ArrayList<>(products.values());
    }

    @GET @Path("/{id}")
    @Query public Product product(@PathParam("id") int id) {
        log.info("product({})", id);
        return Optional.ofNullable(products.get(id))
                .orElseThrow(() -> new ProductNotFoundException("id = " + id));
    }

    private static class ProductNotFoundException extends RuntimeException {
        public ProductNotFoundException(String detail) {
            super("no product found with " + detail);
        }
    }
}
