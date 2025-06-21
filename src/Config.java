import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
    private final Map<String, String> configMap = new HashMap<>();

    public Config(String filePath) throws IOException {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(filePath)) {
            props.load(in);
        }
        for (String name : props.stringPropertyNames()) {
            configMap.put(name, props.getProperty(name));
        }
    }

    public String get(String key) {
        return configMap.get(key);
    }

    public int getAsInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(configMap.get(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public long getAsLong(String key, long defaultValue) {
        try {
            return Long.parseLong(configMap.get(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean getAsBoolean(String key, boolean defaultValue) {
        try {
            return Boolean.parseBoolean(configMap.get(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public double getAsDouble(String key, double defaultValue) {
        try {
            return Double.parseDouble(configMap.get(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public float getAsFloat(String key, float defaultValue) {
        try {
            return Float.parseFloat(configMap.get(key));
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public boolean containsKey(String key) {
        return configMap.containsKey(key);
    }
}

