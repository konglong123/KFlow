package BP.springCloud.tool;

import java.io.Serializable;
import java.util.List;

/**
 * @program: basic-services
 * @description: 系统分页用
 * @author: Mr.Kong
 * @create: 2019-12-26 09:26
 **/
public class Page<T> implements Serializable {
    private int startPoint=0;
    private int pageLength=10;
    private int totalPages;
    private int totalNums;
    private List<T> data;

    public int getTotalNums() {
        return totalNums;
    }

    public void setTotalNums(int totalNums) {
        this.totalNums = totalNums;
    }

    public int getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(int startPoint) {
        this.startPoint = startPoint;
    }

    public int getPageLength() {
        return pageLength;
    }

    public void setPageLength(int pageLength) {
        this.pageLength = pageLength;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Page{" +
                "startPoint=" + startPoint +
                ", pageLength=" + pageLength +
                ", totalPages=" + totalPages +
                ", totalNums=" + totalNums +
                ", data=" + data +
                '}';
    }
}
