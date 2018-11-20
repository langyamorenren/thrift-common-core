package huivo.core.common;


import huivo.util.CommonUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

/**
 * Created by langya on 2014/12/9.
 */
public class ConfigUtil {

    private static ConfigUtil demo;
    private static Document document;
    private static String module;
    Logger LOG = LoggerFactory.getLogger(ConfigUtil.class);
    private String fileName = "config/config.xml";

    public ConfigUtil() {
        module = CommonUtil.getEnvironment();
        InputStream inputXml = this.getClass().getClassLoader().getResourceAsStream(fileName);
        SAXReader saxReader = new SAXReader();
        try {
            document = saxReader.read(inputXml);
        } catch (DocumentException e) {
            e.printStackTrace();
            LOG.info("init document error");
        }
    }

    public static ConfigUtil getInstance() {
        if (demo == null) {
            demo = new ConfigUtil();
        }
        return demo;
    }

    public String getValue(String name) {
        String value = "";
        if (document != null) {
            Node node = document.selectSingleNode(ConfigConstants.root + "/" + module + name);
            value = node.getStringValue();
            //System.out.println(node.getStringValue());
        }
        return value;
    }

    public int getIntValue(String name) {
        int intValue = 0;
        String value = getValue(name);
        try {
            intValue = Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("getIntValue error");
        }
        return intValue;
    }

    public long getLongValue(String name) {
        long longValue = 0;
        String value = getValue(name);
        try {
            longValue = Long.parseLong(value);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("getLongValue error");
        }
        return longValue;
    }

}
