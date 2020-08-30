package BP.Project;

import java.util.List;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-08-26 11:12
 **/
public class ProjectTreeData {
    private String name;
    private List<ProjectTreeData> children;
    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProjectTreeData> getChildren() {
        return children;
    }

    public void setChildren(List<ProjectTreeData> children) {
        this.children = children;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
