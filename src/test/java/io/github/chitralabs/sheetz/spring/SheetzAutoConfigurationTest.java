package io.github.chitralabs.sheetz.spring;

import io.github.chitralabs.sheetz.Sheetz;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

class SheetzAutoConfigurationTest {

    private final ApplicationContextRunner runner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(SheetzAutoConfiguration.class));

    @Test
    void autoConfigurationRegistersTemplate() {
        runner.run(context -> {
            assertThat(context).hasSingleBean(SheetzTemplate.class);
            assertThat(context).hasSingleBean(SheetzProperties.class);
        });
    }

    @Test
    void disabledWhenPropertyIsFalse() {
        runner.withPropertyValues("sheetz.enabled=false")
                .run(context -> assertThat(context).doesNotHaveBean(SheetzTemplate.class));
    }

    @Test
    void customBeanOverridesAutoConfigured() {
        runner.withUserConfiguration(CustomTemplateConfig.class)
                .run(context -> {
                    assertThat(context).hasSingleBean(SheetzTemplate.class);
                    assertThat(context.getBean(SheetzTemplate.class))
                            .isSameAs(context.getBean("customSheetzTemplate"));
                });
    }

    @Test
    void propertiesBindCorrectly() {
        runner.withPropertyValues(
                        "sheetz.date-format=dd/MM/yyyy",
                        "sheetz.trim-values=false",
                        "sheetz.streaming-threshold=5000",
                        "sheetz.default-sheet-name=Data"
                )
                .run(context -> {
                    SheetzProperties props = context.getBean(SheetzProperties.class);
                    assertThat(props.getDateFormat()).isEqualTo("dd/MM/yyyy");
                    assertThat(props.isTrimValues()).isFalse();
                    assertThat(props.getStreamingThreshold()).isEqualTo(5000);
                    assertThat(props.getDefaultSheetName()).isEqualTo("Data");
                });
    }

    @Configuration(proxyBeanMethods = false)
    static class CustomTemplateConfig {
        @Bean
        SheetzTemplate customSheetzTemplate() {
            return new SheetzTemplate(new SheetzProperties());
        }
    }
}
