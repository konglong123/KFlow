package com.kong.kflowweb.BP.WF.HttpHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.http.protocol.HttpContext;

import com.kong.kflowweb.BP.DA.DBAccess;
import com.kong.kflowweb.BP.DA.DBType;
import com.kong.kflowweb.BP.DA.DataColumn;
import com.kong.kflowweb.BP.DA.DataRow;
import com.kong.kflowweb.BP.DA.DataSet;
import com.kong.kflowweb.BP.DA.DataTable;
import com.kong.kflowweb.BP.DA.Log;
import com.kong.kflowweb.BP.Difference.Handler.WebContralBase;
import com.kong.kflowweb.BP.En.EditType;
import com.kong.kflowweb.BP.En.FieldTypeS;
import com.kong.kflowweb.BP.En.UIContralType;
import com.kong.kflowweb.BP.Sys.MapAttr;
import com.kong.kflowweb.BP.Sys.MapAttrAttr;
import com.kong.kflowweb.BP.Sys.MapAttrs;
import com.kong.kflowweb.BP.Sys.MapData;
import com.kong.kflowweb.BP.Sys.SysEnumMain;
import com.kong.kflowweb.BP.Sys.SystemConfig;
import com.kong.kflowweb.BP.Web.WebUser;

public class WF_AppClassic extends WebContralBase {
	/**
	 * 初始化数据
	 * 
	 * @param mycontext
	 */
	public WF_AppClassic(HttpContext mycontext) {
		this.context = mycontext;
	}

	public WF_AppClassic() {
	}

	// /#region 执行父类的重写方法.
	/**
	 * 默认执行的方法
	 * 
	 * @return
	 */
	@Override
	protected String DoDefaultMethod() {
		// switch (this.DoType)
		// ORIGINAL LINE: case "DtlFieldUp":
		if (this.getDoType().equals("DtlFieldUp")) // 字段上移
		{
			return "执行成功.";
		} else {
		}

		// 找不不到标记就抛出异常.
		return "err@没有判断的执行标记:" + this.getDoType();
	}

	// /#endregion 执行父类的重写方法.

	// /#region xxx 界面 .

	// /#endregion xxx 界面方法.

	/**
	 * 初始化Home
	 * 
	 * @return
	 * @throws Exception 
	 */
	public final String Home_Init() throws Exception {
		AppACE page = new AppACE(context);
		return page.Home_Init();
	}

	public final String Index_Init() throws Exception {
		AppACE page = new AppACE(context);
		return page.Index_Init();
	}
	// /#region 登录界面.
	public final String Login_Init() throws Exception {
		AppACE page = new AppACE(context);
		return page.Login_Init();
	}

	public final String Login_Submit() throws Exception {
		AppACE page = new AppACE(context);
		 
			return page.Login_Submit();
		 
	}
	
	
}
