package com.uoko.generator.service.impl;

import com.uoko.generator.dao.SysMapper;
import com.uoko.generator.service.SysService;
import com.uoko.generator.util.GeneratorUtil;
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
 * Email shaohua@uoko.com
 * created by 2018/6/13
 */
@Service
public class SysServiceImpl implements SysService {

    @Autowired
    private SysMapper sysMapper;

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
        return sysMapper.queryTable(tableName);
    }

    /**
     * 查询列
     *
     * @param tableName
     * @return
     */
    public List<Map<String, String>> queryColumns(String tableName) {
        return sysMapper.queryColumns(tableName);
    }

}
