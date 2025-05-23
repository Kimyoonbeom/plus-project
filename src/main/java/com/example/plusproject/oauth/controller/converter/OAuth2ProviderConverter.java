package com.example.plusproject.oauth.controller.converter;

import com.example.plusproject.common.type.OAuth2Provider;
import org.springframework.core.convert.converter.Converter;

public class OAuth2ProviderConverter implements Converter<String, OAuth2Provider> {

    @Override
    public OAuth2Provider convert(String source) {
        try {
            return OAuth2Provider.valueOf(source.toUpperCase());
        } catch (Exception ex) {
            throw new IllegalArgumentException();
        }
    }
}
