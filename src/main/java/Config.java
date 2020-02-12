import java.util.Properties;

class Config {
    private Properties configFile;

    Config() {
        configFile = new java.util.Properties();
        try {
            configFile.load(this.getClass().getClassLoader().
                    getResourceAsStream("config.properties"));
        } catch (Exception eta) {
            eta.printStackTrace();
        }
    }

    String getProperty(String key) {
        return this.configFile.getProperty(key);
    }
}