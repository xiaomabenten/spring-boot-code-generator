package com.uoko.generator.util;

import com.uoko.generator.domain.entity.ColumnEntity;
import com.uoko.generator.domain.entity.TableEntity;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 * 自动生成代码工具
 * </p>
 *
 * @author shaohua
 * Email shaohua@uoko.com
 * created by 2018/6/15
 */
public class GeneratorUtil {

    public static String getProjectName() {
        Configuration configuration = getConfig();
        return configuration.getString("projectName") + File.separator + "src" + File.separator;
    }

    /**
     * 获取有固定格式的模板
     *
     * @return
     */
    public static List<String> getMysqlTemplates() {
        List<String> templates = new ArrayList<String>();
        templates.add("templates/remote/entity/Entity.java.vm");
        templates.add("templates/remote/dao/Dao.java.vm");
        templates.add("templates/remote/dao/Dao.xml.vm");
        templates.add("templates/remote/service/Service.java.vm");
        templates.add("templates/remote/service/impl/ServiceImpl.java.vm");
        templates.add("templates/remote/controller/Endpoint.java.vm");
        return templates;
    }

    /**
     * 生成固定模板的数据,无需关联数据库
     *
     * @return
     */
    public static List<String> getConstantFile() {
        List<String> templates = new ArrayList<String>();
        templates.add("templates/remote/utils/PageUtils.java.vm");
        templates.add("templates/remote/utils/Query.java.vm");
        templates.add("templates/remote/utils/SQLFilter.java.vm");
        templates.add("templates/remote/utils/Response.java.vm");
        templates.add("templates/remote/resources/application.yml.vm");
        templates.add("templates/remote/springboot/SpringBootApplication.java.vm");
        templates.add("templates/remote/test/SpringBootApplicationTests.java.vm");
        templates.add("templates/remote/pom/pom.xml.vm");
        return templates;
    }

    /**
     * 生成空文件夹
     *
     * @return
     */
    public static List<String> getNullFile() {
        List<String> file = new ArrayList<>();
        file.add("domain" + File.separator + "dto" + File.separator);
        file.add("constant" + File.separator);
        file.add("config" + File.separator);
        file.add("remote" + File.separator);
        return file;
    }


    /**
     * 生成与表无关的文件数据
     */
    public static void generatorNullCodeFile(ZipOutputStream zip) {
        String packagePath = getProjectName() + "main" + File.separator + "java" + File.separator;
        //配置信息
        Configuration config = getConfig();
        String pageage = config.getString("package");
        if (StringUtils.isNotBlank(pageage)) {
            packagePath += pageage.replace(".", File.separator) + File.separator + config.getString("moduleName") + File.separator;
        }
        for (String s : getNullFile()) {
            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(packagePath + s));
                zip.closeEntry();
            } catch (IOException e) {
                throw new RRException("空文件夹生成失败", e);
            }
        }
    }

    /**
     * 生产固定不变的代码
     */
    public static void generatorConstantCode(ZipOutputStream zip) {
        //配置信息
        Configuration config = getConfig();
        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);
        //获取模板列表
        List<String> templates = getConstantFile();
        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("package", config.getString("package"));
        map.put("moduleName", config.getString("moduleName"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        map.put("artifactId", config.getString("artifactId"));
        map.put("description", config.getString("description"));
        map.put("version", config.getString("version"));
        map.put("server-port", config.getString("server-port"));
        map.put("context-path", config.getString("context-path"));
        map.put("mysql-url", config.getString("mysql-url"));
        map.put("mysql-port", config.getString("mysql-port"));
        map.put("mysql-username", config.getString("mysql-username"));
        map.put("mysql-password", config.getString("mysql-password"));
        map.put("springBootName", WordUtils.capitalize(config.getString("moduleName")));
        VelocityContext context = new VelocityContext(map);

        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);
            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(Objects.requireNonNull(getConstantFileName(template))));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new RRException("空文件夹生成失败", e);
            }
        }
    }


    /**
     * 生成代码
     */
    public static void generatorCode(Map<String, String> table,
                                     List<Map<String, String>> columns, ZipOutputStream zip) {
        //配置信息
        Configuration config = getConfig();
        boolean hasBigDecimal = false;
        //表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName"));
        tableEntity.setComments(table.get("tableComment"));
        //表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), config.getString("tablePrefix"));
        tableEntity.setClassName(className);
        tableEntity.setClassname(StringUtils.uncapitalize(className));

        //列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for (Map<String, String> column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName"));
            columnEntity.setDataType(column.get("dataType"));
            columnEntity.setComments(column.get("columnComment"));
            columnEntity.setExtra(column.get("extra"));

            //列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrname(StringUtils.uncapitalize(attrName));

            //列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            //是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        //没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        //设置velocity资源加载器
        Properties prop = new Properties();
        prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        Velocity.init(prop);

        String mainPath = config.getString("mainPath");
        mainPath = StringUtils.isBlank(mainPath) ? "com.uoko" : mainPath;

        //封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classname", tableEntity.getClassname());
        map.put("pathName", tableEntity.getClassname().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("mainPath", mainPath);
        map.put("package", config.getString("package"));
        map.put("moduleName", config.getString("moduleName"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
        VelocityContext context = new VelocityContext(map);
        //获取模板列表
        List<String> templates = getMysqlTemplates();
        for (String template : templates) {
            //渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            tpl.merge(context, sw);

            try {
                //添加到zip
                zip.putNextEntry(new ZipEntry(Objects.requireNonNull(getFileName(template, tableEntity.getClassName(), config.getString("package"), config.getString("moduleName")))));
                IOUtils.write(sw.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(sw);
                zip.closeEntry();
            } catch (IOException e) {
                throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            }
        }
    }


    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String tablePrefix) {
        if (StringUtils.isNotBlank(tablePrefix)) {
            tableName = tableName.replace(tablePrefix, "");
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RRException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName, String moduleName) {
        String packagePath = getProjectName() + "main" + File.separator + "java" + File.separator;
        String resourcesPath = getProjectName() + "main" + File.separator + "resources" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
        }
        if (template.contains("Entity.java.vm")) {
            return packagePath + "domain\\entity" + File.separator + className + "Entity.java";
        }

        if (template.contains("Dao.java.vm")) {
            return packagePath + "dao" + File.separator + className + "Dao.java";
        }

        if (template.contains("Service.java.vm")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.vm")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains("Endpoint.java.vm")) {
            return packagePath + "endpoint" + File.separator + className + "Endpoint.java";
        }

        if (template.contains("Dao.xml.vm")) {
            return resourcesPath + "mapper" + File.separator + moduleName + File.separator + className + "Dao.xml";
        }
        if (template.contains("Dao.xml.vm")) {
            return resourcesPath + "mapper" + File.separator + moduleName + File.separator + className + "Dao.xml";
        }
        return null;
    }

    public static String getConstantFileName(String template) {
        //配置信息
        Configuration config = getConfig();
        String packagePath = getProjectName() + "main" + File.separator + "java" + File.separator;
        String resourcesPath = getProjectName() + "main" + File.separator + "resources" + File.separator;
        String testPath = getProjectName() + "test" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(config.getString("package"))) {
            packagePath += config.getString("package").replace(".", File.separator) + File.separator + config.getString("moduleName") + File.separator;
            testPath += config.getString("package").replace(".", File.separator) + File.separator + config.getString("moduleName") + File.separator;
        }
        if (template.contains("PageUtils.java.vm")) {
            return packagePath + "utils" + File.separator + "PageUtils.java";
        }
        if (template.contains("Query.java.vm")) {
            return packagePath + "utils" + File.separator + "Query.java";
        }
        if (template.contains("SQLFilter.java.vm")) {
            return packagePath + "utils" + File.separator + "SQLFilter.java";
        }
        if (template.contains("Response.java.vm")) {
            return packagePath + "utils" + File.separator + "Response.java";
        }
        if (template.contains("SpringBootApplication.java.vm")) {
            return packagePath + WordUtils.capitalize(config.getString("moduleName")) + "Application.java";
        }
        if (template.contains("SpringBootApplicationTests.java.vm")) {
            return testPath + WordUtils.capitalize(config.getString("moduleName")) + "ApplicationTests.java";
        }
        if (template.contains("application.yml.vm")) {
            return resourcesPath + "application.yml";
        }
        if (template.contains("pom.xml.vm")) {
            return config.getString("projectName") + File.separator + "pom.xml";
        }
        return null;
    }
}
