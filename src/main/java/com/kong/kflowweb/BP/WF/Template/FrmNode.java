package com.kong.kflowweb.BP.WF.Template;

import com.kong.kflowweb.BP.En.EntityMyPK;
import com.kong.kflowweb.BP.En.Map;
import com.kong.kflowweb.BP.En.UAC;
import com.kong.kflowweb.BP.Sys.FrmType;
import com.kong.kflowweb.BP.Sys.MapData;
import com.kong.kflowweb.BP.Sys.SysEnum;
import com.kong.kflowweb.BP.Tools.StringHelper;

/**
 * 节点表单 节点的工作节点有两部分组成. 记录了从一个节点到其他的多个节点. 也记录了到这个节点的其他的节点.
 * 
 */
public class FrmNode extends EntityMyPK {
	public final String getFrmUrl() {
		switch (this.getHisFrmType()) {
		case FoolForm:
			return com.kong.kflowweb.BP.WF.Glo.getCCFlowAppPath() + "WF/CCForm/FrmFix";
		case FreeFrm:
			return com.kong.kflowweb.BP.WF.Glo.getCCFlowAppPath() + "WF/CCForm/Frm";
		default:
			throw new RuntimeException("err,未处理。");
		}
	}

	private Frm _hisFrm = null;

	public final Frm getHisFrm() throws Exception {
		if (this._hisFrm == null) {
			this._hisFrm = new Frm(this.getFK_Frm());
			this._hisFrm.HisFrmNode = this;
		}
		return this._hisFrm;
	}

	/**
	 * UI界面上的访问控制
	 * 
	 * @throws Exception
	 * 
	 */
	@Override
	public UAC getHisUAC() throws Exception {
		UAC uac = new UAC();
		uac.OpenForSysAdmin();
		return uac;
	}

	/**
	 * 表单类型
	 * 
	 */
	public final com.kong.kflowweb.BP.Sys.FrmType getHisFrmType() {
		return com.kong.kflowweb.BP.Sys.FrmType.forValue(this.GetValIntByKey(FrmNodeAttr.FrmType));
	}

	public final void setHisFrmType(com.kong.kflowweb.BP.Sys.FrmType value) {
		this.SetValByKey(FrmNodeAttr.FrmType, value.getValue());
	}

	/**
	 * 表单类型
	 * 
	 * @throws Exception
	 * 
	 */
	public final String getHisFrmTypeText() throws Exception {
		SysEnum se = new SysEnum(FrmNodeAttr.FrmType, this.getHisFrmType().getValue());
		return se.getLab();
	}

	/**
	 * 是否启用装载填充事件
	 * 
	 */
	public final boolean getIsEnableLoadData() {
		return this.GetValBooleanByKey(FrmNodeAttr.IsEnableLoadData);
	}

	public final void setIsEnableLoadData(boolean value) {
		this.SetValByKey(FrmNodeAttr.IsEnableLoadData, value);
	}

	/**
	 * 是否启用节点组件?
	 */
	public final boolean getIsEnableFWC() {
		return this.GetValBooleanByKey(FrmNodeAttr.IsEnableFWC);
	}

	public final void setIsEnableFWC(boolean value) {
		this.SetValByKey(FrmNodeAttr.IsEnableFWC, value);
	}

	/**
	 * 是否执行1变n
	 * 
	 */
	public final boolean getIs1ToN() {
		return this.GetValBooleanByKey(FrmNodeAttr.Is1ToN);
	}

	public final void setIs1ToN(boolean value) {
		this.SetValByKey(FrmNodeAttr.Is1ToN, value);
	}

	/**
	 * 节点
	 * 
	 */
	public final int getFK_Node() {
		return this.GetValIntByKey(FrmNodeAttr.FK_Node);
	}

	public final void setFK_Node(int value) {
		this.SetValByKey(FrmNodeAttr.FK_Node, value);
	}

	/**
	 * 顺序号
	 * 
	 */
	public final int getIdx() {
		return this.GetValIntByKey(FrmNodeAttr.Idx);
	}

	public final void setIdx(int value) {
		this.SetValByKey(FrmNodeAttr.Idx, value);
	}

	/**
	 * 谁是主键？
	 * 
	 */
	public final WhoIsPK getWhoIsPK() {
		return WhoIsPK.forValue(this.GetValIntByKey(FrmNodeAttr.WhoIsPK));
	}

	public final void setWhoIsPK(WhoIsPK value) {
		this.SetValByKey(FrmNodeAttr.WhoIsPK, value.getValue());
	}

	/**
	 * 表单ID
	 * 
	 */
	public final String getFK_Frm() {
		return this.GetValStringByKey(FrmNodeAttr.FK_Frm);
	}

	public final void setFK_Frm(String value) {
		this.SetValByKey(FrmNodeAttr.FK_Frm, value);
	}

	/**
	 * 模版文件
	 * 
	 */
	public final String getTempleteFile() {
		String str = this.GetValStringByKey(FrmNodeAttr.TempleteFile);
		if (StringHelper.isNullOrEmpty(str)) {
			return this.getFK_Frm() + ".xls";
		}
		return str;
	}

	public final void setTempleteFile(String value) {
		this.SetValByKey(FrmNodeAttr.TempleteFile, value);
	}

	/**
	 * 是否显示
	 * 
	 */
	public final String getIsEnable() {
		return this.GetValStringByKey(FrmNodeAttr.IsEnable);
	}

	public final void setIsEnable(String value) {
		this.SetValByKey(FrmNodeAttr.IsEnable, value);
	}

	/**
	 * 关键字段
	 * 
	 */
	public final String getGuanJianZiDuan() {
		return this.GetValStringByKey(FrmNodeAttr.GuanJianZiDuan);
	}

	public final void setGuanJianZiDuan(String value) {
		this.SetValByKey(FrmNodeAttr.GuanJianZiDuan, value);
	}

	/**
	 * 对应的解决方案 0=默认方案.节点编号1=不可编辑,2=自定义方案, .
	 * 
	 */
	public final int getFrmSlnInt() {
		return this.GetValIntByKey(FrmNodeAttr.FrmSln);
	}

	public final void setFrmSlnInt(int value) {
		this.SetValByKey(FrmNodeAttr.FrmSln, value);
	}

	public final FrmSln getFrmSln() {
		return FrmSln.forValue(this.GetValIntByKey(FrmNodeAttr.FrmSln));
	}

	/**
	 * 启用规则
	 * 
	 */
	public final int getFrmEnableRoleInt() {
		return this.GetValIntByKey(FrmNodeAttr.FrmEnableRole);
	}

	public final void setFrmEnableRoleInt(int value) {
		this.SetValByKey(FrmNodeAttr.FrmEnableRole, value);
	}

	/**
	 * 表单启用规则
	 * 
	 */
	public final FrmEnableRole getFrmEnableRole() {
		return FrmEnableRole.forValue(this.GetValIntByKey(FrmNodeAttr.FrmEnableRole));
	}

	public final void setFrmEnableRole(FrmEnableRole value) {
		this.SetValByKey(FrmNodeAttr.FrmEnableRole, value.getValue());
	}

	/**
	 * 启用规则.
	 * 
	 * @throws Exception
	 * 
	 */
	public final String getFrmEnableRoleText() throws Exception {
		if (this.getFrmEnableRole() == FrmEnableRole.WhenHaveFrmPara
				&& this.getFK_Frm().equals("ND" + this.getFK_Node())) {
			return "不启用";
		}

		SysEnum se = new SysEnum(FrmNodeAttr.FrmEnableRole, this.getFrmEnableRoleInt());
		return se.getLab();
	}

	/**
	 * 表单启动表达式
	 * 
	 */
	public final String getFrmEnableExp() {
		return this.GetValStringByKey(FrmNodeAttr.FrmEnableExp);
	}

	public final void setFrmEnableExp(String value) {
		this.SetValByKey(FrmNodeAttr.FrmEnableExp, value);
	}

	/**
	 * 流程编号
	 * 
	 */
	public final String getFK_Flow() {
		return this.GetValStringByKey(FrmNodeAttr.FK_Flow);
	}

	public final void setFK_Flow(String value) {
		this.SetValByKey(FrmNodeAttr.FK_Flow, value);
	}

	/**
	 * 是否可以编辑？
	 * 
	 */
	public final void setIsEdit(boolean value) {
		this.SetValByKey(FrmNodeAttr.IsEdit, value);
	}

	public final boolean getIsEdit() {
		if (this.getFrmSlnInt() == 1) {
			return false;
		}
		return true;
	}

	/**
	 * 是否可以编辑？
	 * 
	 */
	public final int getIsEditInt() {
		if (this.getIsEdit()) {
			return 1;
		}
		return 0;
	}

	/**
	 * 是否可以打印
	 * 
	 */
	public final boolean getIsPrint() {
		return this.GetValBooleanByKey(FrmNodeAttr.IsPrint);
	}

	public final void setIsPrint(boolean value) {
		this.SetValByKey(FrmNodeAttr.IsPrint, value);
	}

	/**
	 * 是否可以打印
	 * 
	 */
	public final int getIsPrintInt() {
		return this.GetValIntByKey(FrmNodeAttr.IsPrint);
	}

	/**
	 * 汇总
	 * 
	 */
	public final String getHuiZong() {
		return this.GetValStringByKey(FrmNodeAttr.HuiZong);
	}

	public final void setHuiZong(String value) {
		this.SetValByKey(FrmNodeAttr.HuiZong, value);
	}

	/// #endregion

	/**
	 * 节点表单
	 * 
	 */
	public FrmNode() {
	}

	/**
	 * 节点表单
	 * 
	 * @param mypk
	 * @throws Exception
	 */
	public FrmNode(String mypk) throws Exception {
		super(mypk);
	}

	/**
	 * 节点表单
	 * 
	 * @param fk_node
	 *            节点
	 * @param fk_frm
	 *            表单
	 * @throws Exception
	 */
	public FrmNode(String fk_flow, int fk_node, String fk_frm) throws Exception {
		int i = this.Retrieve(FrmNodeAttr.FK_Node, fk_node, FrmNodeAttr.FK_Frm, fk_frm);
		if (i == 0) {
			this.setIsPrint(false);

			// 默认方案.
			this.setFrmSlnInt(0);
			return;
			// throw new RuntimeException("@表单关联信息已被删除。");
		}
	}

	/**
	 * 重写基类方法
	 * 
	 */
	@Override
	public Map getEnMap() {
		if (this.get_enMap() != null) {
			return this.get_enMap();
		}

		Map map = new Map("WF_FrmNode", "节点表单");

		map.AddMyPK();

		map.AddTBString(FrmNodeAttr.FK_Frm, null, "表单ID", true, true, 1, 32, 32);
		map.AddTBInt(FrmNodeAttr.FK_Node, 0, "节点编号", true, false);
		map.AddTBString(FrmNodeAttr.FK_Flow, null, "流程编号", true, true, 1, 20, 20);
		map.AddTBString(FrmNodeAttr.FrmType, "0", "表单类型", true, true, 1, 20, 20);

		// 菜单在本节点的权限控制.
		// map.AddTBInt(FrmNodeAttr.IsEdit, 1, "是否可以更新", true, false);
		map.AddTBInt(FrmNodeAttr.IsPrint, 0, "是否可以打印", true, false);
		map.AddTBInt(FrmNodeAttr.IsEnableLoadData, 0, "是否启用装载填充事件", true, false);

		// 显示的
		map.AddTBInt(FrmNodeAttr.Idx, 0, "顺序号", true, false);
		map.AddTBInt(FrmNodeAttr.FrmSln, 0, "表单控制方案", true, false);
		map.AddTBInt(FrmNodeAttr.IsEnableFWC, 0, "是否启用审核组件？", true, false);

		map.AddTBInt(FrmNodeAttr.WhoIsPK, 0, "谁是主键？", true, false);

		map.AddTBInt(FrmNodeAttr.Is1ToN, 0, "是否1变N？", true, false);
		map.AddTBString(FrmNodeAttr.HuiZong, null, "子线程要汇总的数据表", true, true, 0, 300, 20);
		map.AddTBInt(FrmNodeAttr.FrmEnableRole, 0, "表单启用规则", true, false);
		map.AddTBString(FrmNodeAttr.FrmEnableExp, null, "启用的表达式", true, true, 0, 900, 20);
		map.AddTBInt(FrmNodeAttr.IsDefaultOpen, 0, "是否默认打开", true, false);

		// 模版文件，对于office表单有效.
		map.AddTBString(FrmNodeAttr.TempleteFile, null, "模版文件", true, true, 0, 500, 20);
		// 是否显示
		map.AddTBInt(FrmNodeAttr.IsEnable, 1, "是否显示", true, false);
		map.AddTBString(FrmNodeAttr.GuanJianZiDuan, null, "关键字段", true, true, 1, 20, 20);

		this.set_enMap(map);
		return this.get_enMap();
	}

	/// #endregion

	/// #region 方法.
	public final void DoUp() {
		this.DoOrderUp(FrmNodeAttr.FK_Node, (new Integer(this.getFK_Node())).toString(), FrmNodeAttr.Idx);
	}

	public final void DoDown() {
		this.DoOrderDown(FrmNodeAttr.FK_Node, (new Integer(this.getFK_Node())).toString(), FrmNodeAttr.Idx);
	}

	@Override
	protected boolean beforeUpdateInsertAction() throws Exception {
		this.setMyPK(this.getFK_Frm() + "_" + this.getFK_Node() + "_" + this.getFK_Flow());
		// 获取表单的类型
		MapData mapData = new MapData();
		mapData.setNo(this.getFK_Frm());
		if (mapData.RetrieveFromDBSources() == 1)
			this.setHisFrmType(mapData.getHisFrmType());
		else
			this.setHisFrmType(FrmType.FoolForm);

		return super.beforeUpdateInsertAction();
	}

	/// #endregion 方法.

}