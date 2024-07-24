import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DataAPI {
    private static Properties properties = new Properties();

    static {
        try (InputStream input = DataAPI.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new FileNotFoundException("Файл конфигурации 'config.properties' не найден в classpath");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getURL(){
        return properties.getProperty("api.url");
    }
    public static String getModelURI(){
        return properties.getProperty("api.modelURI");
    }
    public static String getApiKey(){
        return properties.getProperty("api.apiKey");
    }
}
