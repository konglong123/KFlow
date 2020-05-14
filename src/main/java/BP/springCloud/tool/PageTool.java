package BP.springCloud.tool;

import BP.DA.DataTable;
import BP.En.Entities;
import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.*;

/**
 * @program: kflow-web
 * @description:
 * @author: Mr.Kong
 * @create: 2020-03-05 10:09
 **/
public class PageTool {
    /**
    *@Description: 封装分页查询结果
    *@Param:
    *@return:
    *@Author: Mr.kong
    *@Date: 2020/3/5
    */
    public static void TransToResult(Entities entities, HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        //请求页码、每页显示行数、偏移、总数
        int page, rows;
        String input_page = request.getParameter("page");
        page = (input_page == null) ? 1 : Integer.parseInt(input_page);
        String input_rows = request.getParameter("rows");
        rows = (input_rows == null) ? 10 : Integer.parseInt(input_rows);
        JSONObject jsonObject = new JSONObject();
        DataTable dt = entities.ToDataTableField();//此方法针对boolean类型对字段无法正常转换（建议换成string类型，map中定义）
        List resourceList = dt.Rows;
        Map<String, Object> jsonMap = new HashMap<>();//定义map
        int allNum = resourceList.size();
        jsonMap.put("total", allNum);//total键 存放总记录数，必须的
        if (allNum == 0) {
            jsonMap.put("rows", new ArrayList<>(1));//消除查询结果为空时，前端报错
        } else {
            List rList = new ArrayList(rows);
            int endPos = page * rows;
            endPos = endPos < allNum ? endPos : allNum;
            for (int i = (page - 1) * rows; i < endPos; i++) {
                Map<String, Object> temp = (Map) resourceList.get(i);
                rList.add(temp);
            }
            jsonMap.put("rows", rList);//rows键 存放每页记录 list
        }
        JSONObject json = new JSONObject();
        json.putAll(jsonMap);
        String result = json.toString();
        out.print(result);
        out.flush();
        out.close();
    }

    public static void TransToResultList(List<Object> objectList, HttpServletRequest request, HttpServletResponse response) throws Exception{
        response.setCharacterEncoding("utf-8");
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        //请求页码、每页显示行数、偏移、总数
        int page, rows;
        String input_page = request.getParameter("page");
        page = (input_page == null) ? 1 : Integer.parseInt(input_page);
        String input_rows = request.getParameter("rows");
        rows = (input_rows == null) ? 10 : Integer.parseInt(input_rows);

        Map<String, Object> jsonMap = new HashMap<>();//定义map
        int allNum = objectList.size();
        jsonMap.put("total", allNum);//total键 存放总记录数，必须的
        if (allNum == 0) {
            jsonMap.put("rows", new ArrayList<>(1));//消除查询结果为空时，前端报错
        } else {
            List rList = new ArrayList(rows);
            int endPos = page * rows;
            endPos = endPos < allNum ? endPos : allNum;
            for (int i = (page - 1) * rows; i < endPos; i++) {
                Map<String, Object> temp = JSONObject.fromObject(objectList.get(i));
                rList.add(temp);
            }
            jsonMap.put("rows", rList);//rows键 存放每页记录 list
        }
        JSONObject json = new JSONObject();
        json.putAll(jsonMap);
        String result = json.toString();
        out.print(result);
        out.flush();
        out.close();
    }


}
