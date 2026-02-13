package io.github.chitralabs.sheetz.spring;

import io.github.chitralabs.sheetz.Format;
import io.github.chitralabs.sheetz.Sheetz;
import io.github.chitralabs.sheetz.SheetzConfig;
import io.github.chitralabs.sheetz.ValidationResult;
import io.github.chitralabs.sheetz.convert.Converter;
import io.github.chitralabs.sheetz.reader.StreamingReader;
import io.github.chitralabs.sheetz.writer.WorkbookBuilder;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

/**
 * Spring-managed wrapper around the static {@link Sheetz} API.
 *
 * <p>Inject this bean instead of calling {@code Sheetz.*} directly to benefit from
 * externalized configuration via {@code application.properties}.</p>
 *
 * <pre>{@code
 * @Autowired
 * private SheetzTemplate sheetz;
 *
 * List<Product> products = sheetz.read("products.xlsx", Product.class);
 * sheetz.write(products, "output.csv");
 * }</pre>
 *
 * @see SheetzProperties
 * @see SheetzAutoConfiguration
 */
public class SheetzTemplate {

    private final SheetzConfig config;

    public SheetzTemplate(SheetzProperties properties) {
        this.config = properties.toSheetzConfig();
        Sheetz.configure(this.config);
    }

    // ==================== READ ====================

    public <T> List<T> read(String path, Class<T> type) {
        return Sheetz.read(path, type);
    }

    public <T> List<T> read(Path path, Class<T> type) {
        return Sheetz.read(path, type);
    }

    public <T> List<T> read(InputStream input, Class<T> type, Format format) {
        return Sheetz.read(input, type, format);
    }

    // ==================== READ MAPS ====================

    public List<Map<String, Object>> readMaps(String path) {
        return Sheetz.readMaps(path);
    }

    public List<Map<String, Object>> readMaps(Path path) {
        return Sheetz.readMaps(path);
    }

    public List<Map<String, Object>> readMaps(InputStream input, Format format) {
        return Sheetz.readMaps(input, format);
    }

    // ==================== READ RAW ====================

    public List<String[]> readRaw(String path) {
        return Sheetz.readRaw(path);
    }

    public List<String[]> readRaw(Path path) {
        return Sheetz.readRaw(path);
    }

    public List<String[]> readRaw(InputStream input, Format format) {
        return Sheetz.readRaw(input, format);
    }

    // ==================== READ FIRST ====================

    public <T> List<T> readFirst(String path, Class<T> type, int n) {
        return Sheetz.readFirst(path, type, n);
    }

    public <T> List<T> readFirst(Path path, Class<T> type, int n) {
        return Sheetz.readFirst(path, type, n);
    }

    // ==================== WRITE ====================

    public <T> void write(List<T> data, String path) {
        Sheetz.write(data, path);
    }

    public <T> void write(List<T> data, Path path) {
        Sheetz.write(data, path);
    }

    public <T> void write(List<T> data, OutputStream output, Format format) {
        Sheetz.write(data, output, format);
    }

    // ==================== STREAM ====================

    public <T> StreamingReader<T> stream(String path, Class<T> type) {
        return Sheetz.stream(path, type);
    }

    public <T> StreamingReader<T> stream(Path path, Class<T> type) {
        return Sheetz.stream(path, type);
    }

    // ==================== VALIDATE ====================

    public <T> ValidationResult<T> validate(String path, Class<T> type) {
        return Sheetz.validate(path, type);
    }

    public <T> ValidationResult<T> validate(Path path, Class<T> type) {
        return Sheetz.validate(path, type);
    }

    // ==================== BUILDERS ====================

    public <T> Sheetz.ReaderBuilder<T> reader(Class<T> type) {
        return Sheetz.reader(type);
    }

    public <T> Sheetz.WriterBuilder<T> writer(Class<T> type) {
        return Sheetz.writer(type);
    }

    public WorkbookBuilder workbook() {
        return Sheetz.workbook();
    }

    // ==================== CONVERTER ====================

    public <T> void register(Class<T> type, Converter<T> converter) {
        Sheetz.register(type, converter);
    }
}
