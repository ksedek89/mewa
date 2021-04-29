package com.pru.mewa.config.mapper;

import com.pru.mewa.util.mapper.OrikaMapperFactoryBuilderConfigurer;
import lombok.val;
import ma.glasnost.orika.CustomConverter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OrikaConfiguration {
    @Bean
    public MapperFacade orikaMapperFactory(
        List<OrikaMapperFactoryBuilderConfigurer> mappersConfigurations,
        List<CustomConverter<?,?>> converters
    ) {
        val mapperFactory = new DefaultMapperFactory.Builder().build();

        mappersConfigurations.forEach(mapperConfig -> mapperConfig.configure(mapperFactory));

        converters.forEach(converter -> mapperFactory.getConverterFactory().registerConverter(converter));

        return mapperFactory.getMapperFacade();
    }
}
