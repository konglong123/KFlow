package com.kong.kflowweb.BP.WF.Port;

import java.util.ArrayList;
import java.util.List;

import com.kong.kflowweb.BP.En.Attr;
import com.kong.kflowweb.BP.En.EntitiesNoName;
import com.kong.kflowweb.BP.En.Entity;

public class AdminEmps extends EntitiesNoName {

	/*
	 *管理员s
	 */
	public AdminEmps()
	{
	}
	/*
	 * 得到它的 Entity
	 */
	@Override
	public Entity getGetNewEntity() {
		return new AdminEmp();
	}
	@Override
	 public  int RetrieveAll() throws Exception
     {
         return super.RetrieveAll("FK_Dept","Idx");
     }
	
	 public List<AdminEmp> ToJavaList() 
     {
		 return (List<AdminEmp>)(Object)this;
     }
	 public List<AdminEmp> Tolist()
     {
         List<AdminEmp> list = new ArrayList<AdminEmp>();
         for (int i = 0; i < this.size(); i++)
         {
             list.add((AdminEmp)this.get(i));
         }
         return list;
     }

}
