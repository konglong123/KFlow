package com.kong.kflowweb.BP.Demo;

import com.kong.kflowweb.BP.DA.Log;
import com.kong.kflowweb.BP.Sys.BuessUnitBase;

public class BuessUnitDemo extends BuessUnitBase {

	@Override
	public String getTitle() {
		return "业务单元测试";
	}

	@Override
	public String DoIt() {
		
		Log.DebugWriteInfo("调用业务单元[" + this.toString() + "] WorkID: " + this.getWorkID());
		return super.DoIt();
	}

}
