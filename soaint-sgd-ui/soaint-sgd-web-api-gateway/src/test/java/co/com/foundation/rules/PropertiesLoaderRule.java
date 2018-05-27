package co.com.foundation.rules;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.util.Arrays;
import java.util.Properties;

public class PropertiesLoaderRule implements TestRule {

    private Properties properties;

    public static PropertiesLoaderRule from(String ...filenames) {

            Properties properties = new Properties();
            Arrays.asList(filenames).forEach(filename -> {
                try {
                    Properties props = PropertiesLoaderUtils.loadAllProperties(filename);
                    properties.putAll(props);
                } catch (Exception ignored) {}
            });

        return new PropertiesLoaderRule(properties);
    }

    public PropertiesLoaderRule(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return base;
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
