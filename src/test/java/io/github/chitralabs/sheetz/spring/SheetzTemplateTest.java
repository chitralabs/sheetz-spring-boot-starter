package io.github.chitralabs.sheetz.spring;

import io.github.chitralabs.sheetz.annotation.Column;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class SheetzTemplateTest {

    @TempDir
    Path tempDir;

    private final SheetzTemplate template = new SheetzTemplate(new SheetzProperties());

    @Test
    void writeAndReadRoundTrip() {
        Path file = tempDir.resolve("products.xlsx");
        List<Product> data = Arrays.asList(
                new Product("Widget", 9.99, true),
                new Product("Gadget", 24.50, false),
                new Product("Doohickey", 3.75, true)
        );

        template.write(data, file);
        List<Product> result = template.read(file, Product.class);

        assertThat(result).hasSize(3);
        assertThat(result.get(0).getName()).isEqualTo("Widget");
        assertThat(result.get(0).getPrice()).isEqualTo(9.99);
        assertThat(result.get(0).isInStock()).isTrue();
        assertThat(result.get(1).getName()).isEqualTo("Gadget");
        assertThat(result.get(2).getName()).isEqualTo("Doohickey");
    }

    @Test
    void writeAndReadMaps() {
        Path file = tempDir.resolve("maps.xlsx");
        List<Product> data = Arrays.asList(
                new Product("Alpha", 1.00, true),
                new Product("Beta", 2.00, false)
        );

        template.write(data, file);
        List<Map<String, Object>> maps = template.readMaps(file);

        assertThat(maps).hasSize(2);
        assertThat(maps.get(0)).containsKey("Name");
        assertThat(maps.get(0).get("Name")).isEqualTo("Alpha");
    }

    @Test
    void writeAndReadRaw() {
        Path file = tempDir.resolve("raw.xlsx");
        List<Product> data = Arrays.asList(
                new Product("One", 10.0, true)
        );

        template.write(data, file);
        List<String[]> raw = template.readRaw(file);

        // First row is header, second is data
        assertThat(raw).hasSize(2);
        assertThat(raw.get(0)).contains("Name");
        assertThat(raw.get(1)[0]).isEqualTo("One");
    }

    @Test
    void csvRoundTrip() {
        Path file = tempDir.resolve("products.csv");
        List<Product> data = Arrays.asList(
                new Product("CSV Item", 5.55, true)
        );

        template.write(data, file);
        List<Product> result = template.read(file, Product.class);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("CSV Item");
    }

    // --- Test model ---

    public static class Product {
        @Column("Name")
        private String name;

        @Column("Price")
        private double price;

        @Column("In Stock")
        private boolean inStock;

        public Product() {}

        public Product(String name, double price, boolean inStock) {
            this.name = name;
            this.price = price;
            this.inStock = inStock;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }
        public boolean isInStock() { return inStock; }
        public void setInStock(boolean inStock) { this.inStock = inStock; }
    }
}
