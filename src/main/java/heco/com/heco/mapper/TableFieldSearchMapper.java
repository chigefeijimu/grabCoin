package heco.com.heco.mapper;

import heco.com.heco.entity.TableField;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author junqiao.li
 */
@Mapper
public interface TableFieldSearchMapper {

    /**
     *
     * @param tableName;
     * @return List of table fields;
     */
    List<TableField> selectTableFielByTableName(String tableName);

    /**
     * 返回macrowing_erms_db_v2数据库的所有表名
     * @return List<String>
     */
    List<String> selectAllTableName();

}
