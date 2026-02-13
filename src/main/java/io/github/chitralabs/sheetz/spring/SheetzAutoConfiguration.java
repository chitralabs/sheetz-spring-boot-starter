package io.github.chitralabs.sheetz.spring;

import io.github.chitralabs.sheetz.Sheetz;
import io.github.chitralabs.sheetz.convert.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ResolvableType;

/**
 * Auto-configuration for Sheetz.
 *
 * <p>Activates when {@code sheetz-core} is on the classpath and
 * {@code sheetz.enabled} is {@code true} (the default). Registers a
 * {@link SheetzTemplate} bean and auto-discovers any {@link Converter}
 * beans in the application context.</p>
 *
 * @see SheetzProperties
 * @see SheetzTemplate
 */
@AutoConfiguration
@ConditionalOnClass(Sheetz.class)
@ConditionalOnProperty(prefix = "sheetz", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(SheetzProperties.class)
public class SheetzAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SheetzAutoConfiguration.class);

    @Bean
    @ConditionalOnMissingBean
    public SheetzTemplate sheetzTemplate(SheetzProperties properties,
                                         ObjectProvider<Converter<?>> converters) {
        SheetzTemplate template = new SheetzTemplate(properties);

        converters.orderedStream().forEach(converter -> {
            Class<?> targetType = resolveConverterType(converter);
            if (targetType != null) {
                registerConverter(targetType, converter);
                log.debug("Registered Sheetz converter for type: {}", targetType.getName());
            }
        });

        log.info("Sheetz auto-configuration initialized");
        return template;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void registerConverter(Class<?> targetType, Converter<?> converter) {
        Sheetz.register((Class) targetType, (Converter) converter);
    }

    private Class<?> resolveConverterType(Converter<?> converter) {
        ResolvableType type = ResolvableType.forClass(converter.getClass())
                .as(Converter.class);
        ResolvableType generic = type.getGeneric(0);
        if (generic != ResolvableType.NONE) {
            Class<?> resolved = generic.resolve();
            if (resolved != null) {
                return resolved;
            }
        }
        log.warn("Could not resolve generic type for converter: {}. "
                + "Register it manually via SheetzTemplate.register().",
                converter.getClass().getName());
        return null;
    }
}
