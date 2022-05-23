package ru.netology.config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Configuration
public class WebConfig {
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        final var bean = new RequestMappingHandlerAdapter();

        var gsonBuilder = new GsonBuilder();

        gsonBuilder.addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                Expose expose = fieldAttributes.getAnnotation(Expose.class);
                return expose != null && !expose.serialize();
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        });

        gsonBuilder.addDeserializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                Expose expose = fieldAttributes.getAnnotation(Expose.class);
                return expose != null && !expose.deserialize();
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        });

        var gsonMessageConverter = new GsonHttpMessageConverter();
        gsonMessageConverter.setGson(gsonBuilder.create());

        bean.getMessageConverters().add(gsonMessageConverter);
        return bean;
    }
}
