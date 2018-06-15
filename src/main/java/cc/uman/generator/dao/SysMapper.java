package cc.uman.generator.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 自动生成代码mapper
 * </p>
 *
 * @author shaohua
 * Email xiaomabenten@gmail.com
 * created by 2018/6/13
 */
@Mapper
public interface SysMapper {

    /**
     * 查询数据库所有表
     *
     * @param map
     * @return
     */
    List<Map<String, Object>> queryList(Map<String, Object> map);

    /**
     * 查询map结果对应总数
     *
     * @param map
     * @return
     */
    Integer queryTotal(Map<String, Object> map);

    /**
     * 查询表
     *
     * @param tableName
     * @return
     */
    Map<String, String> queryTable(String tableName);


    /**
     * 查询表的列
     *
     * @param tableName
     * @return
     */
    List<Map<String, String>> queryColumns(String tableName);
}
