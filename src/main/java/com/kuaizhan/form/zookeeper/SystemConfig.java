package com.kuaizhan.form.zookeeper;

 

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Properties;

/**
 * œµÕ≥≈‰÷√(ƒ⁄≤ø π”√)
 * 
 * @author adyliu (adyliu@sohu-inc.com)
 * @since 2011-6-13s
 */
public class SystemConfig {

    private static SystemConfig instance = new SystemConfig();

    public static SystemConfig getInstance() {
        return instance;
    }

    private Properties config = new Properties();

    private SystemConfig() {
        init();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        String value = getString(key, null);
        return value == null ? defaultValue : Boolean.valueOf(value).booleanValue();
    }

    public int getInt(String key, int defaultValue) {
        String value = getString(key, null);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {}
        }
        return defaultValue;

    }

    public String getString(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value == null) {
            value = config.getProperty(key, defaultValue);
        }
        return value;
    }

    private void init() {
        try {
            Properties pro = loadAllProperties("suc-core.properties");
            config.putAll(pro);
        } catch (IOException e) {
            e.printStackTrace();
            //ignore all exception
        }
    }

    private Properties loadAllProperties(String resourceName) throws IOException {
        ClassLoader clToUse = SystemConfig.class.getClassLoader();
        Properties properties = new Properties();
        Enumeration<URL> urls = clToUse.getResources(resourceName);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            InputStream is = null;
            try {
                URLConnection con = url.openConnection();
                con.setUseCaches(false);
                is = con.getInputStream();
                properties.load(is);
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        return properties;
    }

}
