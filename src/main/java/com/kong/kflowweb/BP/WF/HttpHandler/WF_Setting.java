package com.kong.kflowweb.BP.WF.HttpHandler;

import java.io.File;
import java.util.Hashtable;

import javax.servlet.http.HttpServletRequest;
import com.kong.kflowweb.BP.DA.*;
import com.kong.kflowweb.BP.Difference.Handler.CommonFileUtils;
import com.kong.kflowweb.BP.Difference.Handler.WebContralBase;
import com.kong.kflowweb.BP.Sys.*;
import com.kong.kflowweb.BP.Web.*;
import com.kong.kflowweb.BP.Port.*;
import com.kong.kflowweb.BP.En.*;
import com.kong.kflowweb.BP.WF.*;
import com.kong.kflowweb.BP.WF.Port.WFEmp;
import com.kong.kflowweb.BP.WF.Template.*;

/** 
 页面功能实体
 
*/
public class WF_Setting extends WebContralBase
{
	
	/**
	 * 构造函数
	 */
	public WF_Setting()
	{
	
	}
	
	

		///#region 执行父类的重写方法.
	/** 
	 默认执行的方法
	 
	 @return 
	*/
	@Override
	protected String DoDefaultMethod()
	{
		return "";
	}

	public final String Default_Init() throws Exception
	{
		java.util.Hashtable ht = new java.util.Hashtable();
		ht.put("UserNo", WebUser.getNo());
		ht.put("UserName", WebUser.getName());

		com.kong.kflowweb.BP.Port.Emp emp = new Emp();
		emp.setNo(WebUser.getNo());
		emp.Retrieve();

		//部门名称.
		ht.put("DeptName", emp.getFK_DeptText());

		 
			com.kong.kflowweb.BP.GPM.DeptEmpStations des = new com.kong.kflowweb.BP.GPM.DeptEmpStations();
			des.Retrieve(com.kong.kflowweb.BP.GPM.DeptEmpStationAttr.FK_Emp, WebUser.getNo());

			String depts = "";
			String stas = "";

			for (com.kong.kflowweb.BP.GPM.DeptEmpStation item : des.ToJavaList()
					)
			{
				com.kong.kflowweb.BP.Port.Dept dept = new Dept();
				dept.setNo(item.getFK_Dept());
				int count = dept.RetrieveFromDBSources();
				if(count !=0)
					depts += dept.getName() + "、";


				com.kong.kflowweb.BP.Port.Station sta = new Station();
				sta.setNo(item.getFK_Station());
				count = sta.RetrieveFromDBSources();
				if(count!=0)
					stas += sta.getName() + "、";
			}

			ht.put("Depts", depts);
			ht.put("Stations", stas);
		

		 

		com.kong.kflowweb.BP.WF.Port.WFEmp wfemp = new com.kong.kflowweb.BP.WF.Port.WFEmp(WebUser.getNo());
		ht.put("Tel", wfemp.getTel());
		ht.put("Email", wfemp.getEmail());

		return com.kong.kflowweb.BP.Tools.Json.ToJson(ht);
	}

		///#region 图片签名.
	public final String Siganture_Init() throws Exception
	{
		if (com.kong.kflowweb.BP.Web.WebUser.getNoOfRel() == null)
            return "err@登录信息丢失";

		java.util.Hashtable ht = new java.util.Hashtable();
        ht.put("No", com.kong.kflowweb.BP.Web.WebUser.getNo());
        ht.put("Name", com.kong.kflowweb.BP.Web.WebUser.getName());
        ht.put("FK_Dept", com.kong.kflowweb.BP.Web.WebUser.getFK_Dept());
        ht.put("FK_DeptName", com.kong.kflowweb.BP.Web.WebUser.getFK_DeptName());
        return com.kong.kflowweb.BP.Tools.Json.ToJson(ht);
	}
	

	public final String Siganture_Save()
	{
		try
		{
			String empNo = this.GetRequestVal("EmpNo");
	        if (DataType.IsNullOrEmpty(empNo) == true)
	             empNo = WebUser.getNo();
	        HttpServletRequest request = getRequest();
			String contentType = request.getContentType();
			if (contentType != null && contentType.indexOf("multipart/form-data") != -1) { 				
				String tempFilePath = com.kong.kflowweb.BP.Sys.SystemConfig.getPathOfWebApp() + "/DataUser/Siganture/" + empNo + ".jpg";
				File tempFile = new File(tempFilePath);
				if(tempFile.exists()){
					tempFile.delete();
				}
//				MultipartFile multipartFile = ((DefaultMultipartHttpServletRequest)request).getFile("File_Upload");
//				multipartFile.transferTo(tempFile);
				CommonFileUtils.upload(request, "File_Upload", tempFile);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			return "err@执行失败";
		}
		
		return "文件上传成功";
    
	}

		///#endregion 图片签名.
	//	 头像.
    public String HeadPic_Save()
    {
    	
    	try
		{
			String empNo = this.GetRequestVal("EmpNo");
	        if (DataType.IsNullOrEmpty(empNo) == true)
	             empNo = WebUser.getNo();
	        HttpServletRequest request = getRequest();
			String contentType = request.getContentType();
			if (contentType != null && contentType.indexOf("multipart/form-data") != -1) { 				
				String tempFilePath = com.kong.kflowweb.BP.Sys.SystemConfig.getPathOfWebApp() + "/DataUser/UserIcon/" + empNo + ".png";
				File tempFile = new File(tempFilePath);
				if(tempFile.exists()){
					tempFile.delete();
				}
//				MultipartFile multipartFile = ((DefaultMultipartHttpServletRequest)request).getFile("File_Upload");
//				multipartFile.transferTo(tempFile);
				CommonFileUtils.upload(request, "File_Upload", tempFile);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			return "err@执行失败";
		}
		
		return "文件上传成功";
    }
    
    /// <summary>
    /// 初始化
    /// </summary>
    /// <returns>json数据</returns>
    public String Author_Init() throws Exception
    {
        com.kong.kflowweb.BP.WF.Port.WFEmp emp = new WFEmp(com.kong.kflowweb.BP.Web.WebUser.getNo());
        Hashtable ht = emp.getRow();
        ht.remove(com.kong.kflowweb.BP.WF.Port.WFEmpAttr.StartFlows); //移除这一列不然无法形成json.
        return emp.ToJson();
    }
    public String Author_Save() throws Exception
    {
    	 com.kong.kflowweb.BP.WF.Port.WFEmp emp = new WFEmp(com.kong.kflowweb.BP.Web.WebUser.getNo());
        emp.setAuthor(this.GetRequestVal("Author"));
        emp.setAuthorDate(this.GetRequestVal("AuthorDate"));
        emp.setAuthorWay(this.GetRequestValInt("AuthorWay"));
        emp.Update();
        return "保存成功";
    }
    
    
	public final String UserIcon_Init()
	{
		return "";
	}

	public final String UserIcon_Save()
	{
		return "";
	}
	
	/** 
	 修改密码 @于庆海.
	 
	 @return 
	 * @throws Exception 
*/
	public final String ChangePassword_Submit() throws Exception
	{
		String oldPass = this.GetRequestVal("OldPass");
		String pass = this.GetRequestVal("Pass");

		com.kong.kflowweb.BP.Port.Emp emp = new Emp(com.kong.kflowweb.BP.Web.WebUser.getNo());
		if (emp.CheckPass(oldPass) == false)
		{
			return "err@旧密码错误.";
		}

		if (com.kong.kflowweb.BP.Sys.SystemConfig.getIsEnablePasswordEncryption() == true)
		{
			pass = Cryptography.EncryptString(pass);
		}
		emp.setPass(pass);
		emp.Update();

		return "密码修改成功...";
	}
	/** 切换部门.
	 
	 @return 
	 * @throws Exception 
	 */
   public  String ChangeDept_Submit() throws Exception
	
    {
		
		
        String deptNo = this.GetRequestVal("DeptNo");
        com.kong.kflowweb.BP.GPM.Dept dept = new com.kong.kflowweb.BP.GPM.Dept(deptNo);

        com.kong.kflowweb.BP.Web.WebUser.setFK_Dept(dept.getNo());//FK_Dept = dept.No;
        com.kong.kflowweb.BP.Web.WebUser.setFK_DeptName(dept.getName());//FK_DeptName = dept.Name;
        com.kong.kflowweb.BP.Web.WebUser.setFK_DeptNameOfFull(dept.getNameOfPath());//FK_DeptNameOfFull = dept.NameOfPath;

        //重新设置cookies.
        String strs = "";
        strs += "@No=" + WebUser.getNo();
        strs += "@Name=" + WebUser.getName();
        strs += "@FK_Dept=" + WebUser.getFK_Dept();
        strs += "@FK_DeptName=" + WebUser.getFK_DeptName();
        strs += "@FK_DeptNameOfFull=" + WebUser.getFK_DeptNameOfFull();
        //com.kong.kflowweb.BP.Web.WebUser.setSetValToCookie(strs);
        com.kong.kflowweb.BP.WF.Port.WFEmp emp = new com.kong.kflowweb.BP.WF.Port.WFEmp(WebUser.getNo());
        emp.setStartFlows(""); 
        emp.Update();

        try
        {
            String sql = "UPDATE Port_Emp Set fk_dept='"+deptNo+"' WHERE no='"+WebUser.getNo()+"'";
            DBAccess.RunSQL(sql);
            com.kong.kflowweb.BP.WF.Dev2Interface.Port_Login(WebUser.getNo());
        }
        catch (Exception ex)
        {

        }
		//ChangeDept_Init();
        return "@执行成功,已经切换到｛" + com.kong.kflowweb.BP.Web.WebUser.getFK_DeptName() + "｝部门上。";
    }
	
	/** 初始化切换部门.
	 
	 @return 
	 * @throws Exception 
	 */
	public final String ChangeDept_Init() throws Exception
	{
		String sql = "SELECT a.No,a.Name, NameOfPath, '0' AS  CurrentDept FROM Port_Dept A, Port_DeptEmp B WHERE A.No=B.FK_Dept AND B.FK_Emp='" + com.kong.kflowweb.BP.Web.WebUser.getNo() + "'";
		DataTable dt = DBAccess.RunSQLReturnTable(sql);

		if (SystemConfig.getAppCenterDBType() == DBType.Oracle)
		{
			dt.Columns.get("NO").ColumnName = "No";
			dt.Columns.get("NAME").ColumnName = "Name";
			dt.Columns.get("CURRENTDEPT").ColumnName = "CurrentDept";
			dt.Columns.get("NAMEOFPATH").ColumnName = "NameOfPath";
		}

		//设置当前的部门.
		for (DataRow dr : dt.Rows)
		{
			if (dr.getValue("No").toString().equals(WebUser.getFK_Dept()))
			{
				dr.setValue("CurrentDept","1");
			}

			if (!dr.getValue("NameOfPath").toString().equals(""))
			{
				dr.setValue("Name", dr.getValue("NameOfPath"));
			}
		}

		return com.kong.kflowweb.BP.Tools.Json.ToJson(dt);
	}
}