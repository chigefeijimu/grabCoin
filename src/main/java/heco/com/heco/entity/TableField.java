package heco.com.heco.entity;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 数据库表对应的字段信息类
 */
public class TableField {

    /**
     * 字段名
     */
    @ExcelProperty("字段名")
    private String columnName;
    /**
     *数据类型
     */
    @ExcelProperty("数据类型")
    private String columnType;
    /**
     *字段类型
     */
    @ExcelProperty("字段类型")
    private String dataType;
    /**
     * 长度
     */
    @ExcelProperty("长度")
    private String characterMaximumLength;
    /**
     * 是否为空
     */
    @ExcelProperty("是否为空")
    private String isNullable;
    /**
     * 默认值
     */
    @ExcelProperty("默认值")
    private String columnDefault;
    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String columnComment;

    public TableField() {
    }

    public TableField(String columnName, String columnType, String dataType, String characterMaximumLength, String isNullable, String columnDefault, String columnComment) {
        this.columnName = columnName;
        this.columnType = columnType;
        this.dataType = dataType;
        this.characterMaximumLength = characterMaximumLength;
        this.isNullable = isNullable;
        this.columnDefault = columnDefault;
        this.columnComment = columnComment;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    public void setCharacterMaximumLength(String characterMaximumLength) {
        this.characterMaximumLength = characterMaximumLength;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    @Override
    public String toString() {
        return "TableField{" +
                "columnName='" + columnName + '\'' +
                ", columnType='" + columnType + '\'' +
                ", dataType='" + dataType + '\'' +
                ", characterMaximumLength='" + characterMaximumLength + '\'' +
                ", isNullable='" + isNullable + '\'' +
                ", columnDefault='" + columnDefault + '\'' +
                ", columnComment='" + columnComment + '\'' +
                '}';
    }
}
