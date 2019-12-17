package com.kong.kflowweb.WebServiceImp;

import javax.jws.WebMethod;
import javax.jws.WebService;


import com.kong.kflowweb.BP.DA.DataRow;
import com.kong.kflowweb.BP.DA.DataSet;
import com.kong.kflowweb.BP.DA.DataTable;
import com.kong.kflowweb.BP.DA.DataType;
import com.kong.kflowweb.BP.En.Attr;
import com.kong.kflowweb.BP.En.Attrs;
import com.kong.kflowweb.BP.Sys.GEDtlAttr;
import com.kong.kflowweb.BP.Sys.GEDtls;
import com.kong.kflowweb.BP.Sys.MapData;
import com.kong.kflowweb.BP.Sys.MapDtl;
import com.kong.kflowweb.BP.Sys.MapDtls;
import com.kong.kflowweb.BP.Web.WebUser;
import com.kong.kflowweb.WebService.CCFormAPII;

@WebService
public class CCFormAPI implements CCFormAPII{
	
	/** 
	 获得单据模版信息
	 @param userNo 用户编号
	 @param sid SID
	 @param workID 工作ID
	 @param billTemplateNo 单据模版编号
	 @param ds 返回的数据源
	 @param bytes 返回的字节
	 * @throws Exception 
	*/

	@Override
	@WebMethod
	public void GenerBillTemplate(String userNo, String sid, long workID, String billTemplateNo, DataSet ds,
			byte[] bytes) throws Exception {
		com.kong.kflowweb.BP.WF.Dev2Interface.Port_Login(userNo);
		if (DataType.IsNullOrEmpty(userNo) == true)
		{
			userNo = WebUser.getNo();
		}

		com.kong.kflowweb.BP.WF.Dev2Interface.Port_Login(userNo);
		com.kong.kflowweb.BP.WF.GenerWorkFlow gwf = new com.kong.kflowweb.BP.WF.GenerWorkFlow(workID);

		//是否可以查看该工作.
		boolean b = com.kong.kflowweb.BP.WF.Dev2Interface.Flow_IsCanViewTruck(gwf.getFK_Flow(), gwf.getWorkID(), userNo);
		if (b == false)
		{
			throw new RuntimeException("err@无权查看该流程.");
		}

		String frmID = "ND" + Integer.parseInt(gwf.getFK_Flow()) + "Rpt";
		com.kong.kflowweb.BP.WF.Data.GERpt rpt = new com.kong.kflowweb.BP.WF.Data.GERpt("ND" + Integer.parseInt(gwf.getFK_Flow()) + "Rpt", workID);
		DataTable dt = rpt.ToDataTableField( "Main");
		ds.Tables.add(dt);
		Attrs attrs = rpt.getEnMap().getAttrs();
			///#region 处理bool类型.
		for (Attr item : attrs)
		{
			if (item.getMyDataType() == DataType.AppBoolean)
			{
				dt.Columns.Add(item.getKey() + "Text", String.class);

				for (DataRow dr : dt.Rows)
				{
					String val = dr.getValue(item.getKey()).toString();
					if (val.equals("1"))
					{
						dr.setValue(item.getKey() + "Text","[√]");
					}
					else
					{
						dr.setValue(item.getKey() + "Text", "[×]");
					}
				}
			}
		}
			///#endregion 处理bool类型.

		//把从表数据加入里面去.
		MapDtls dtls = new MapDtls("ND" + gwf.getFK_Node());
		for (MapDtl item : dtls.ToJavaList())
		{
			GEDtls dtlEns = new GEDtls(item.getNo());
			dtlEns.Retrieve(GEDtlAttr.RefPK, workID);

			DataTable dtDtl = dtlEns.ToDataTableField(item.getNo());
			ds.Tables.add(dtDtl);
		}

		//生成模版的文件流.
		com.kong.kflowweb.BP.WF.Template.BillTemplate template = new com.kong.kflowweb.BP.WF.Template.BillTemplate(billTemplateNo);
		bytes = template.GenerTemplateFile();
		return;

	}

	/** 
	 获得Word文件 - 未开发完成.
	 
	 @param userNo 用户编号
	 @param sid SID
	 @param frmID 表单ID
	 @param oid 表单主键
	 @return 
	 * @throws Exception 
	*/
	@Override
	public void WordFileGener(String userNo, String sid, long workID, byte[] bytes) throws Exception {
		com.kong.kflowweb.BP.WF.Dev2Interface.Port_Login(userNo);
		if (DataType.IsNullOrEmpty(userNo) == true)
		{
			userNo = WebUser.getNo();
		}


		com.kong.kflowweb.BP.WF.Dev2Interface.Port_Login(userNo);
		com.kong.kflowweb.BP.WF.GenerWorkFlow gwf = new com.kong.kflowweb.BP.WF.GenerWorkFlow(workID);

		boolean b = com.kong.kflowweb.BP.WF.Dev2Interface.Flow_IsCanViewTruck(gwf.getFK_Flow(), gwf.getWorkID(),userNo);
		if (b == false)
		{
			throw new RuntimeException("err@无权查看该流程.");
		}


		String frmID = "ND" + Integer.parseInt(gwf.getFK_Flow()) + "Rpt";

		MapData md = new MapData(frmID);

		//创建excel表单描述，让其保存到excel表单指定的字段里, 扩展多个表单映射同一张表.
		//MapFrmWord mfe = new MapFrmExcels(frmID);

		//返回文件模版.
		//md.WordGenerFile((new Long(workID)).toString(), bytes, mfe.DBSave);

		
	}
	
	 

}
