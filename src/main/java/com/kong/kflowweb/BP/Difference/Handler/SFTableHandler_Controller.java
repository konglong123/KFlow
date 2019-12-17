package com.kong.kflowweb.BP.Difference.Handler;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/DataUser/SFTableHandler")
@ResponseBody
public class SFTableHandler_Controller{
	
	@RequestMapping(value = "/Demo_HandlerEmps")
	 public String Demo_HandlerEmps() throws Exception
     {
         com.kong.kflowweb.BP.Port.Emps emps = new com.kong.kflowweb.BP.Port.Emps();
         emps.RetrieveAll();
         return emps.ToJson();
     }

}
