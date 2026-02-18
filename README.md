# Sheetz Spring Boot Starter — Auto-Configure Excel & CSV Processing in Spring Boot

[![Build](https://github.com/chitralabs/sheetz-spring-boot-starter/actions/workflows/ci.yml/badge.svg)](https://github.com/chitralabs/sheetz-spring-boot-starter/actions/workflows/ci.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.github.chitralabs.sheetz/sheetz-core)](https://central.sonatype.com/artifact/io.github.chitralabs.sheetz/sheetz-core)
[![Java 11+](https://img.shields.io/badge/Java-11%2B-blue.svg)](https://openjdk.java.net/)
[![Spring Boot 2.7+](https://img.shields.io/badge/Spring%20Boot-2.7%2B-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/chitralabs/sheetz?style=social)](https://github.com/chitralabs/sheetz)

Spring Boot auto-configuration for [Sheetz](https://github.com/chitralabs/sheetz) — inject a `SheetzTemplate` bean and read/write Excel and CSV files with zero boilerplate.

```java
@Autowired
private SheetzTemplate sheetz;

// Read Excel → Java objects
List<Product> products = sheetz.read("products.xlsx", Product.class);

// Write Java objects → CSV
sheetz.write(products, "output.csv");
```

No manual configuration required. Add the dependency, and it works.

---

## Quick Start

### 1. Add Dependencies

**Maven:**
```xml
<dependency>
    <groupId>io.github.chitralabs.sheetz</groupId>
    <artifactId>sheetz-spring-boot-starter</artifactId>
    <version>1.0.1</version>
</dependency>
<dependency>
    <groupId>io.github.chitralabs.sheetz</groupId>
    <artifactId>sheetz-core</artifactId>
    <version>1.0.1</version>
</dependency>
```

**Gradle:**
```groovy
implementation 'io.github.chitralabs.sheetz:sheetz-spring-boot-starter:1.0.1'
implementation 'io.github.chitralabs.sheetz:sheetz-core:1.0.1'
```

### 2. Inject and Use

```java
@Service
public class ReportService {

    private final SheetzTemplate sheetz;

    public ReportService(SheetzTemplate sheetz) {
        this.sheetz = sheetz;
    }

    public List<Order> importOrders(String path) {
        return sheetz.read(path, Order.class);
    }

    public void exportOrders(List<Order> orders, String path) {
        sheetz.write(orders, path);
    }
}
```

That's it. The starter auto-configures `SheetzTemplate` with sensible defaults.

---

## Configuration

Customize behavior in `application.properties` or `application.yml`:

```properties
# Date/time formats
sheetz.date-format=dd/MM/yyyy
sheetz.date-time-format=dd/MM/yyyy HH:mm:ss
sheetz.time-format=HH:mm

# Reading behavior
sheetz.trim-values=true
sheetz.skip-empty-rows=true
sheetz.evaluate-formulas=true
sheetz.header-row=0

# Writing behavior
sheetz.default-sheet-name=Sheet1
sheetz.streaming-threshold=10000

# Processing
sheetz.batch-size=1000
sheetz.charset=UTF-8

# Disable auto-configuration
sheetz.enabled=false
```

### All Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `sheetz.enabled` | `boolean` | `true` | Enable/disable auto-configuration |
| `sheetz.date-format` | `String` | `yyyy-MM-dd` | Date format for `LocalDate` |
| `sheetz.date-time-format` | `String` | `yyyy-MM-dd HH:mm:ss` | Format for `LocalDateTime` |
| `sheetz.time-format` | `String` | `HH:mm:ss` | Format for `LocalTime` |
| `sheetz.trim-values` | `boolean` | `true` | Trim whitespace from strings |
| `sheetz.skip-empty-rows` | `boolean` | `true` | Skip empty rows when reading |
| `sheetz.default-sheet-name` | `String` | `Sheet1` | Default sheet name for writing |
| `sheetz.header-row` | `int` | `0` | Header row index (0-based) |
| `sheetz.batch-size` | `int` | `1000` | Batch size for batch processing |
| `sheetz.evaluate-formulas` | `boolean` | `true` | Evaluate formulas when reading |
| `sheetz.streaming-threshold` | `int` | `10000` | Auto-stream writes above this row count |
| `sheetz.charset` | `Charset` | `UTF-8` | CSV character encoding |

---

## SheetzTemplate API

`SheetzTemplate` wraps the entire [Sheetz API](https://github.com/chitralabs/sheetz) as instance methods:

### Read

```java
// Typed objects
List<Product> products = sheetz.read("products.xlsx", Product.class);
List<Product> products = sheetz.read(path, Product.class);
List<Product> products = sheetz.read(inputStream, Product.class, Format.XLSX);

// Schema-free maps
List<Map<String, Object>> maps = sheetz.readMaps("data.csv");

// Raw strings
List<String[]> raw = sheetz.readRaw("data.xlsx");

// Preview first N rows
List<Product> preview = sheetz.readFirst("huge.xlsx", Product.class, 100);
```

### Write

```java
sheetz.write(products, "output.xlsx");
sheetz.write(products, path);
sheetz.write(products, outputStream, Format.CSV);
```

### Stream (constant memory)

```java
try (StreamingReader<Product> reader = sheetz.stream("huge.csv", Product.class)) {
    for (Product p : reader) {
        process(p);
    }
}
```

### Validate

```java
ValidationResult<Product> result = sheetz.validate("import.xlsx", Product.class);
System.out.printf("Valid: %d | Errors: %d%n", result.validCount(), result.errorCount());
```

### Builders

```java
// Fine-grained read control
List<Product> products = sheetz.reader(Product.class)
    .file("report.xlsx")
    .sheet("Q4 Data")
    .read();

// Fine-grained write control
sheetz.writer(Product.class)
    .data(products)
    .file("output.xlsx")
    .sheet("Products")
    .autoSize(true)
    .freezeHeader(true)
    .write();

// Multi-sheet workbook
sheetz.workbook()
    .sheet("Products", products)
    .sheet("Orders", orders)
    .write("report.xlsx");
```

---

## Custom Converters

Define a `Converter<T>` bean, and the starter auto-registers it:

```java
@Component
public class MoneyConverter implements Converter<BigDecimal> {

    @Override
    public BigDecimal fromCell(Object value, ConvertContext ctx) {
        return new BigDecimal(value.toString().replace("$", "").replace(",", "").trim());
    }

    @Override
    public Object toCell(BigDecimal value) {
        return "$" + value.setScale(2, RoundingMode.HALF_UP);
    }
}
```

All `BigDecimal` fields in your models will automatically use this converter — no manual registration needed.

---

## Custom Template Bean

Override the auto-configured `SheetzTemplate` if you need custom initialization:

```java
@Configuration
public class SheetzConfig {

    @Bean
    public SheetzTemplate sheetzTemplate(SheetzProperties properties) {
        SheetzTemplate template = new SheetzTemplate(properties);
        template.register(Money.class, new MoneyConverter());
        return template;
    }
}
```

---

## Requirements

- Java 11+
- Spring Boot 2.7+ (also compatible with Spring Boot 3.x)
- [sheetz-core](https://central.sonatype.com/artifact/io.github.chitralabs.sheetz/sheetz-core) 1.0.1

## Links

- [Sheetz — main library](https://github.com/chitralabs/sheetz)
- [Sheetz Examples — 8 runnable demos](https://github.com/chitralabs/sheetz-examples)
- [Sheetz Benchmarks — performance comparisons](https://github.com/chitralabs/sheetz-benchmarks)
- [Sheetz on Maven Central](https://central.sonatype.com/artifact/io.github.chitralabs.sheetz/sheetz-core)

## License

[Apache License 2.0](LICENSE) — free for commercial and personal use.

---

If Sheetz saved you time, consider giving the [main repo](https://github.com/chitralabs/sheetz) a star.

[![Star Sheetz](https://img.shields.io/github/stars/chitralabs/sheetz?style=social)](https://github.com/chitralabs/sheetz)
