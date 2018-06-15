package cc.uman.generator.service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author shaohua
 * Email xiaomabenten@gmail.com
 * created by 2018/6/13
 */
public interface SysService {

    /**
     * 查询数据库所有表
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryList(Map<String, Object> map);

    /**
     * 查询 map 搜索的结果
     *
     * @param map
     * @return
     */
    Integer queryTotal(Map<String, Object> map);

    /**
     * 生成code 代码模块
     *
     * @param tableNames
     * @return
     */
    byte[] generatorCode(String[] tableNames);

}
