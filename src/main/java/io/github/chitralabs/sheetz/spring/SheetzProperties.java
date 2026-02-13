package io.github.chitralabs.sheetz.spring;

import io.github.chitralabs.sheetz.SheetzConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Configuration properties for Sheetz, mapped from {@code sheetz.*} in
 * {@code application.properties} or {@code application.yml}.
 *
 * <p>Example:</p>
 * <pre>
 * sheetz.date-format=dd/MM/yyyy
 * sheetz.trim-values=false
 * sheetz.streaming-threshold=5000
 * </pre>
 *
 * @see SheetzConfig
 */
@ConfigurationProperties(prefix = "sheetz")
public class SheetzProperties {

    /** Whether Sheetz auto-configuration is enabled. */
    private boolean enabled = true;

    /** Date format pattern for LocalDate fields. */
    private String dateFormat = "yyyy-MM-dd";

    /** Date-time format pattern for LocalDateTime fields. */
    private String dateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    /** Time format pattern for LocalTime fields. */
    private String timeFormat = "HH:mm:ss";

    /** Whether to trim whitespace from string values. */
    private boolean trimValues = true;

    /** Whether to skip empty rows during reading. */
    private boolean skipEmptyRows = true;

    /** Default sheet name for writing. */
    private String defaultSheetName = "Sheet1";

    /** Header row index, 0-based. */
    private int headerRow = 0;

    /** Batch size for batch processing. */
    private int batchSize = 1000;

    /** Whether to evaluate formulas when reading. */
    private boolean evaluateFormulas = true;

    /** Row count threshold for auto-enabling streaming writes. */
    private int streamingThreshold = 10000;

    /** Character encoding for CSV files. */
    private Charset charset = StandardCharsets.UTF_8;

    /**
     * Converts these properties into a {@link SheetzConfig} instance.
     *
     * @return a new SheetzConfig built from these properties
     */
    public SheetzConfig toSheetzConfig() {
        return SheetzConfig.builder()
                .dateFormat(dateFormat)
                .dateTimeFormat(dateTimeFormat)
                .timeFormat(timeFormat)
                .trimValues(trimValues)
                .skipEmptyRows(skipEmptyRows)
                .defaultSheetName(defaultSheetName)
                .headerRow(headerRow)
                .batchSize(batchSize)
                .evaluateFormulas(evaluateFormulas)
                .streamingThreshold(streamingThreshold)
                .charset(charset)
                .build();
    }

    // --- Getters and Setters ---

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getDateFormat() { return dateFormat; }
    public void setDateFormat(String dateFormat) { this.dateFormat = dateFormat; }

    public String getDateTimeFormat() { return dateTimeFormat; }
    public void setDateTimeFormat(String dateTimeFormat) { this.dateTimeFormat = dateTimeFormat; }

    public String getTimeFormat() { return timeFormat; }
    public void setTimeFormat(String timeFormat) { this.timeFormat = timeFormat; }

    public boolean isTrimValues() { return trimValues; }
    public void setTrimValues(boolean trimValues) { this.trimValues = trimValues; }

    public boolean isSkipEmptyRows() { return skipEmptyRows; }
    public void setSkipEmptyRows(boolean skipEmptyRows) { this.skipEmptyRows = skipEmptyRows; }

    public String getDefaultSheetName() { return defaultSheetName; }
    public void setDefaultSheetName(String defaultSheetName) { this.defaultSheetName = defaultSheetName; }

    public int getHeaderRow() { return headerRow; }
    public void setHeaderRow(int headerRow) { this.headerRow = headerRow; }

    public int getBatchSize() { return batchSize; }
    public void setBatchSize(int batchSize) { this.batchSize = batchSize; }

    public boolean isEvaluateFormulas() { return evaluateFormulas; }
    public void setEvaluateFormulas(boolean evaluateFormulas) { this.evaluateFormulas = evaluateFormulas; }

    public int getStreamingThreshold() { return streamingThreshold; }
    public void setStreamingThreshold(int streamingThreshold) { this.streamingThreshold = streamingThreshold; }

    public Charset getCharset() { return charset; }
    public void setCharset(Charset charset) { this.charset = charset; }
}
