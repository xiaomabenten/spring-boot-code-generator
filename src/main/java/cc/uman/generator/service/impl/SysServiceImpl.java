package cc.uman.generator.service.impl;

import cc.uman.generator.dao.SqlServerSysMapper;
import cc.uman.generator.dao.SysMapper;
import cc.uman.generator.service.SysService;
import cc.uman.generator.util.GeneratorUtil;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * <p>
 *
 * </p>
 *
 * @author shaohua
 * Email xiaomabenten@gmail.com
 * created by 2018/6/13
 */
@Service
public class SysServiceImpl implements SysService {

    @Autowired
    private SysMapper sysMapper;
    @Autowired
    private SqlServerSysMapper sqlServerSysMapper;

    @Override
    public List<Map<String, Object>> queryList(Map<String, Object> map) {
        return sysMapper.queryList(map);
    }

    @Override
    public Integer queryTotal(Map<String, Object> map) {
        return sysMapper.queryTotal(map);
    }

    @Override
    public byte[] generatorCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        //生成与表有关的数据
        for (String tableName : tableNames) {
            //查询表信息
            Map<String, String> table = queryTable(tableName);
            //查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            //生成代码
            GeneratorUtil.generatorCode(table, columns, zip);
        }
        //生成与表无关的信息
        GeneratorUtil.generatorNullCodeFile(zip);
        //生产utils
        GeneratorUtil.generatorConstantCode(zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 查询表
     *
     * @param tableName
     * @return
     */
    public Map<String, String> queryTable(String tableName) {
        if (isMysql()) {
            return sysMapper.queryTable(tableName);
        } else if (isSqlServer()) {
            return sqlServerSysMapper.queryTable(tableName);
        } else {
            throw new RuntimeException("请添加数据库的支持类型 如：mysql 或者 sqlserver");
        }
    }

    /**
     * 查询列
     *
     * @param tableName
     * @return
     */
    public List<Map<String, String>> queryColumns(String tableName) {
        if (isMysql()) {
            return sysMapper.queryColumns(tableName);
        } else if (isSqlServer()) {
            return sqlServerSysMapper.queryColumns(tableName);
        } else {
            throw new RuntimeException("请添加数据库的支持类型 如：mysql 或者 sqlserver");
        }

    }


    /**
     * 判断是不是mysql
     *
     * @return
     */
    public boolean isMysql() {
        Configuration config = GeneratorUtil.getConfig();
        if (config.getString("sql-type").equals("mysql")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是不是sqlserver
     *
     * @return
     */
    public boolean isSqlServer() {
        Configuration config = GeneratorUtil.getConfig();
        if (config.getString("sql-type").equals("sqlserver")) {
            return true;
        }
        return false;
    }

}
