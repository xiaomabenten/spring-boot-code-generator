package cc.uman.generator.domain.dto;

/**
 * <p>
 * 列表请求对象
 * </p>
 *
 * @author shaohua
 * Email xiaomabenten@gmail.com
 * created by 2018/6/13
 */
public class ListRequestDto {
    private int page;
    private String order;
    private int limit;
    private String tableName;

    public ListRequestDto() {
    }

    public ListRequestDto(int page, String order, int limit, String tableName) {
        this.page = page;
        this.order = order;
        this.limit = limit;
        this.tableName = tableName;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
