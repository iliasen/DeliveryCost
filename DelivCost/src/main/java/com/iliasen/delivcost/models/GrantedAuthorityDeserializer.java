package com.iliasen.delivcost.models;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class GrantedAuthorityDeserializer extends JsonDeserializer<Collection<? extends GrantedAuthority>> {

    @Override
    public Collection<? extends GrantedAuthority> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String role = jsonParser.getValueAsString();
        return List.of(new SimpleGrantedAuthority(role));
    }
}