package example;

import java.io.*;
import java.util.Properties;

/**
 * 示例：读取配置
 */
public class PropertiesExample {
    public static void main(String[] args) throws IOException {
        String filePath = "D:\\CodeRepositories\\java\\io\\src\\main\\resources\\a.properties";
        plainReadProperties(filePath);
        readProperties(filePath);
        addProperties();
        setProperties();
    }

    /**
     * 使用字符流读取文件
     *
     * @param filePath 文件路径
     */
    private static void plainReadProperties(String filePath) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
            String readLintStr;
            while ((readLintStr = bufferedReader.readLine()) != null) {
                String[] split = readLintStr.split("=");
                System.out.println(split[0] + " " + split[1]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用 Properties 读取文件
     */
    private static void readProperties(String filePath) {
        Properties properties = new Properties();
        // 加载指定配置文件
        try {
            properties.load(new FileReader(filePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 输出到“标准”输出
        properties.list(System.out);
        System.out.println("user: " + properties.getProperty("user"));
    }

    /**
     * 添加属性对
     */
    private static void addProperties() throws IOException {
        Properties properties = new Properties();
        // 设置属性
        properties.setProperty("a", "haha");
        properties.setProperty("b", "张三");

        properties.store(new FileOutputStream("D:\\CodeRepositories\\java\\io\\src\\main\\resources\\b.properties"), "这里是注释");
    }

    /**
     * 修改属性值
     */
    private static void setProperties() throws IOException {
        Properties properties = new Properties();
        // 设置属性：如果有这个属性就是更新，没有就是新增
        properties.setProperty("a", "wa");
        properties.setProperty("b", "李四");
        properties.store(new FileOutputStream("D:\\CodeRepositories\\java\\io\\src\\main\\resources\\b.properties"), "这里是注释");
    }
}
