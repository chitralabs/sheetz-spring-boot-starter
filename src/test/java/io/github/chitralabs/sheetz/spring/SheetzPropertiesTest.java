package io.github.chitralabs.sheetz.spring;

import io.github.chitralabs.sheetz.SheetzConfig;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

class SheetzPropertiesTest {

    @Test
    void defaultValues() {
        SheetzProperties props = new SheetzProperties();

        assertThat(props.isEnabled()).isTrue();
        assertThat(props.getDateFormat()).isEqualTo("yyyy-MM-dd");
        assertThat(props.getDateTimeFormat()).isEqualTo("yyyy-MM-dd HH:mm:ss");
        assertThat(props.getTimeFormat()).isEqualTo("HH:mm:ss");
        assertThat(props.isTrimValues()).isTrue();
        assertThat(props.isSkipEmptyRows()).isTrue();
        assertThat(props.getDefaultSheetName()).isEqualTo("Sheet1");
        assertThat(props.getHeaderRow()).isZero();
        assertThat(props.getBatchSize()).isEqualTo(1000);
        assertThat(props.isEvaluateFormulas()).isTrue();
        assertThat(props.getStreamingThreshold()).isEqualTo(10000);
        assertThat(props.getCharset()).isEqualTo(StandardCharsets.UTF_8);
    }

    @Test
    void toSheetzConfigMapsAllFields() {
        SheetzProperties props = new SheetzProperties();
        props.setDateFormat("dd/MM/yyyy");
        props.setDateTimeFormat("dd/MM/yyyy HH:mm");
        props.setTimeFormat("HH:mm");
        props.setTrimValues(false);
        props.setSkipEmptyRows(false);
        props.setDefaultSheetName("Data");
        props.setHeaderRow(1);
        props.setBatchSize(500);
        props.setEvaluateFormulas(false);
        props.setStreamingThreshold(5000);
        props.setCharset(StandardCharsets.ISO_8859_1);

        SheetzConfig config = props.toSheetzConfig();

        assertThat(config.dateFormat()).isEqualTo("dd/MM/yyyy");
        assertThat(config.dateTimeFormat()).isEqualTo("dd/MM/yyyy HH:mm");
        assertThat(config.timeFormat()).isEqualTo("HH:mm");
        assertThat(config.trimValues()).isFalse();
        assertThat(config.skipEmptyRows()).isFalse();
        assertThat(config.defaultSheetName()).isEqualTo("Data");
        assertThat(config.headerRow()).isEqualTo(1);
        assertThat(config.batchSize()).isEqualTo(500);
        assertThat(config.evaluateFormulas()).isFalse();
        assertThat(config.streamingThreshold()).isEqualTo(5000);
        assertThat(config.charset()).isEqualTo(StandardCharsets.ISO_8859_1);
    }
}
