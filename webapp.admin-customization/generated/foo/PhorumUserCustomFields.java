package foo;


/**
 * PhorumUserCustomFields is a Querydsl bean type
 */
public class PhorumUserCustomFields {

    private String data;

    private Integer type;

    private Integer userId;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}

