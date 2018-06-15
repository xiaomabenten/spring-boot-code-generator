package cc.uman.generator.domain.entity;

/**
 * <p>
 * 列表数据
 * </p>
 *
 * @author shaohua
 * Email xiaomabenten@gmail.com
 * created by 2018/6/13
 */
public class ColumnEntity {
    /**
     * 列名
     */
    private String columnName;
    /**
     * 列名类型
     */
    private String dataType;
    /**
     * 列名备注
     */
    private String comments;

    /**
     * 属性名称(第一个字母大写)，如：user_name => UserName
     */
    private String attrName;
    /**
     * 属性名称(第一个字母小写)，如：user_name => userName
     */
    private String attrname;
    /**
     * 属性类型
     */
    private String attrType;
    /**
     * auto_increment
     */
    private String extra;

    public ColumnEntity() {
    }

    public ColumnEntity(String columnName, String dataType,
                        String comments, String attrName, String attrname, String attrType, String extra) {
        this.columnName = columnName;
        this.dataType = dataType;
        this.comments = comments;
        this.attrName = attrName;
        this.attrname = attrname;
        this.attrType = attrType;
        this.extra = extra;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getAttrname() {
        return attrname;
    }

    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public String getAttrType() {
        return attrType;
    }

    public void setAttrType(String attrType) {
        this.attrType = attrType;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }
}
