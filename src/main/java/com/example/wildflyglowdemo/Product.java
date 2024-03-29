package com.example.wildflyglowdemo;

import io.smallrye.graphql.api.federation.FieldSet;
import io.smallrye.graphql.api.federation.Key;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
@Key(fields = @FieldSet("id"))
public class Product {
    private int id;
    //@Deprecated
    private String name;
}
