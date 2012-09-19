package foo;


/**
 * PhorumSettings is a Querydsl bean type
 */
public class PhorumSettings {

    private String data;

    private Integer groupId;

    private String name;

    private String type;

    public String getData() {
        return data;
    }

    public void setData(String data) {
    	System.out.println("setData");
        this.data = data;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
    	System.out.println("setGroupId");
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
    	System.out.println("setName");
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

