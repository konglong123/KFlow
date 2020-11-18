package BP.Ga;

import BP.NodeGroup.ComposeGroup;
import BP.NodeGroup.NodeGroup;
import BP.NodeGroup.NodeGroupAttr;
import BP.Tools.BeanTool;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.Direction;
import BP.WF.Template.Directions;
import BP.WF.Template.NodeAttr;
import BP.springCloud.controller.NLPModelController;
import com.alibaba.fastjson.JSONObject;
import java.util.*;


public class GeneticAth {
	private  ArrayList<Chromosome> group=new ArrayList<>();
	private  ArrayList<Chromosome> groupNew=new ArrayList<>();
	//对种群进行排序(降序)
	Comparator comparator=new Comparator<Chromosome>() {
		@Override
		public int compare(Chromosome o1, Chromosome o2) {
			if (o1.getIFitness()>o2.getIFitness()){
				return -1;
			}else if (o1.getIFitness()<o2.getIFitness()){
				return 1;
			}
			return 0;
		}
	};

	//流程编码
	private Flow flow;
	private int mohuNum=0;//模糊节点数
	public int totalTime=0;
	List<List<Gene>> groupGeneAll=new ArrayList<>(mohuNum);//所有基因（genesAll(i)表示第i个模糊节点，所有可选基因）
	public int geneKindMaxNum=100;//每个模糊节点，可选择的最大方案数
	int category=2;//1为关键字检索策略，2为word2vec策略
	List<String[]> nodeInfoList=new ArrayList<>();//[0]nodeNo,[1]abstracts
	ComposeGroup composeGroup =null;
	double maxFitness;
	double aveFitness;
	int type=1;
	float pc1=0.9f;
	float pc2=0.6f;
	float pm1=0.2f;
	float pm2=0.01f;

	public GeneticAth(){

	}

	//初始化种群所有基因可能
	//基因编码
	public GeneticAth(String newFlowNo,ComposeGroup composeGroup) throws Exception{
		this.composeGroup=composeGroup;
		this.pc1=composeGroup.getAcrossPro1();
		this.pc2=composeGroup.getAcrossPro2();
		this.pm1=composeGroup.getVariationPro1();
		this.pm2=composeGroup.getVariationPro2();

		flow=new Flow(newFlowNo);
		Nodes nodes=flow.getHisNodes();
		List<Node> nodeList=nodes.toList();
		int sumTime=0;
		for (Node node:nodeList){
			int type=node.GetValIntByKey(NodeAttr.RunModel);
			if (type==10) {//模糊节点（有时间改成枚举）
				String[] temp=new String[]{node.getNodeID()+"",node.getTip()};
				nodeInfoList.add(temp);
				sumTime+=node.getDoc();
			}
		}
		this.totalTime=sumTime;
		this.mohuNum=nodeInfoList.size();

		//计算各种历史模板的相似度
		if (category==2){
			NLPModelController nlpModel= BeanTool.getBean(NLPModelController.class);
			for (String[] temp:nodeInfoList){
				List<JSONObject> nodeGroups=nlpModel.queryNodeGroupByWord2(temp[1]);
				List<Gene> genes=new ArrayList<>();
				int count=0;
				f1:for(JSONObject nodeGroup:nodeGroups){
					//过滤分值过低选择
					if (nodeGroup.getFloat("score")<composeGroup.getThreshold())
						continue ;
					NodeGroup group=(NodeGroup)nodeGroup.get("group");
					Gene gene=new Gene(group.getNo(),group.GetValIntByKey(NodeGroupAttr.nodeNum),count);
					gene.setIFitness(nodeGroup.getFloat("score"));
					gene.setNodeNo(temp[0]);
					genes.add(gene);
					count++;
					if (count>geneKindMaxNum){
						break f1;
					}
				}
				groupGeneAll.add(genes);
			}
		}else {

		}

	}

	//初始化原始种群
	public void initGroup(){
		for(int j=0;j<composeGroup.getGroupNum();j++){
			Chromosome chr=new Chromosome(groupGeneAll);
			for(int i=0;i<mohuNum;i++){
				List<Gene> geneList=groupGeneAll.get(i);
				int pos=(int)Math.floor(Math.random()*geneList.size());
				chr.add(geneList.get(pos));
			}
			group.add(chr);
		}
	}

	//种群繁殖，染色体进行交叉，采用双点交叉
	private void crossOver(){
		for (int i=1;i<group.size();i+=2){
			double tempPc=getPc(Math.max(group.get(i).getIFitness(),group.get(i-1).getIFitness()));
			if (Math.random()>=tempPc)
				continue;
			int[] pos=getOperationPos();
			doublePointAcross(pos);
		}
	}
	private double getPc(double fitness){
		if (type==1&&fitness>aveFitness)//自适应变异
			return this.pc1-((pc1-pc2)*(fitness-aveFitness))/(maxFitness-aveFitness);
		else
			return pc1;
	}
	private double getPm(double fitness){
		if (type==1&&fitness>aveFitness)
			return this.pm1-((pm1-pm2)*(fitness-aveFitness))*(maxFitness-aveFitness);
		else
			return pm1;
	}
	private  void doublePointAcross(int[] pos){
		int ch1,ch2,pos1,pos2;
		ch1=pos[2];
		ch2=pos[3];
		pos1=pos[0];
		pos2=pos[1];
		Chromosome parent1=group.get(ch1);
		Chromosome parent2=group.get(ch2);
		Chromosome child1=new Chromosome(parent1.getGroupGeneAll());
		Chromosome child2=new Chromosome(parent1.getGroupGeneAll());

		int m;
		if(pos1>pos2){
			m=pos2;
			pos2=pos1;
			pos1=m;
		}

		//复制0-pos1的基因
		for(int i=0;i<pos1;i++){
			child1.add(parent1.get(i));
			child2.add(parent2.get(i));
		}

		//交叉中间部分
		for(int i=pos1;i<=pos2;i++){
			child1.add(parent2.get(i));
			child2.add(parent1.get(i));
		}
		//复制pos2-最后
		for(int i=pos2+1;i<mohuNum;i++){
			child1.add(parent1.get(i));
			child2.add(parent2.get(i));
		}
		groupNew.add(child1);
		groupNew.add(child2);
	}

	//pos[0],pos[1]代表基因的位置，pos[2],pos[3]代表选择的染色体在种群中的位置
	private int[] getOperationPos(){
		int[] pos=new int[4];
		//染色体中基因位置
		pos[0]=(int)Math.floor(Math.random()*mohuNum);
		do{
			pos[1]=(int)Math.floor(Math.random()*mohuNum);
		}while(pos[0]==pos[1]);

		//染色体位置
		pos[2]=(int)Math.floor(Math.random()*composeGroup.getGroupNum());
		do{
			pos[3]=(int)Math.floor(Math.random()*composeGroup.getGroupNum());
		}while(pos[2]==pos[3]);

		return pos;
	}

	//种群突变，染色体中基因发生突变
	//种群突变，染色体中基因发生突变
	private void mutation(){

		for (Chromosome chromosome:group){
			Chromosome newChr = chromosome.chroVar(getPm(chromosome.getIFitness()));
			groupNew.add(newChr);
		}
	}

	//对种群进行选择，采用精英选择策略
	private  void select(){
		Chromosome tempChr;


		Collections.sort(group,comparator);
		Collections.sort(groupNew,comparator);

		//选择个体
		int eliteNum=(int)Math.floor(composeGroup.getGroupNum()*composeGroup.getElitePro());
		ArrayList<Chromosome> nextGroup=new ArrayList<>();
		for (int i=0;i<eliteNum;i++)
			nextGroup.add(group.get(i));
		for(int i=0;i<composeGroup.getGroupNum()-eliteNum;i++)
			nextGroup.add(groupNew.get(i));

		Collections.sort(nextGroup,comparator);
		group=nextGroup;
		groupNew.clear();
	}

	private void initGroupPossible(){
		//初始化交叉率，变异率相关参数
		double sumAveFitness=0d;
		maxFitness=0d;
		for (Chromosome chromosome:group){
			sumAveFitness+=chromosome.getIFitness();
			maxFitness=Math.max(maxFitness,chromosome.getIFitness());
		}
		aveFitness=sumAveFitness/group.size();
	}

	private void initFitness(){
		for (Chromosome temp:group){

		}
		for (Chromosome temp:groupNew){

		}
	}

	//进行进化
	public JSONObject run(int type) throws Exception{
		this.type=type;

		initGroup();//初始化种群
		int count=0;//进化代数
		List<Double> maxHistory=new ArrayList<>();
		List<Double> aveHistory=new ArrayList<>();
		while(count<composeGroup.getGenerateNum()){
			initGroupPossible();
			maxHistory.add(maxFitness);
			aveHistory.add(aveFitness);
			crossOver();//交叉
			mutation();//变异
			initFitness();//计算个体适应度
			select();
			count++;
		}

		//创建推荐的组合流程
		List<String> flowNos=createComposeFlow();
		List<JSONObject> flows=new ArrayList<>(composeGroup.getMaxSaveNum());
		for (int i=0;i<composeGroup.getMaxSaveNum();i++){
			JSONObject item=new JSONObject();
			item.put("flowNo",flowNos.get(i));
			item.put("score",group.get(i).getIFitness());
			flows.add(item);
		}
		JSONObject data=new JSONObject();
		data.put("aveHistory",aveHistory);
		data.put("maxHistory",maxHistory);
		data.put("flows",flows);
		return data;

	}

	//根据进化结果，产生组合流程
	public List<String> createComposeFlow() throws Exception{
		List<String> flowNoList=new ArrayList<>(composeGroup.getMaxSaveNum());
		Nodes nodes=flow.getHisNodes();
		List<Node> nodeList=nodes.toList();
		Directions directions=new Directions(flow.getNo());
		List<Direction> directionsList=directions.toList();

		//解码获取前maxSaveNum个体
		for (int index=0;index<composeGroup.getMaxSaveNum();index++){
			List<Gene> genes=group.get(index).getChromosome();//某个幸存个体的基因
			//新建组合流程
			String flowNo =BP.WF.Template.TemplateGlo.NewFlow("112","组合流程",null,null,null,null);
			Flow newFlow=new Flow(flowNo);
			flowNoList.add(newFlow.getNo());

			Map<String,String> mapNG=new HashMap<>();//<nodeNo,groupNo>
			Map<String,String[]> mapGNs=new HashMap<>();//<groupNo,nodeNos[]>
			for (Gene gene:genes){
				mapGNs.put(gene.getNodeGroupNo(),NodeGroup.getItems(gene.getNodeGroupNo()));
				mapNG.put(gene.getNodeNo(),gene.getNodeGroupNo());
			}

			Map<String ,String> mapNodeNewOld=new HashMap<>();//<oldNodeNo,newNodeNo>
			Map<String,Map<String,String>> mapGroupNodeNewOld=new HashMap<>();//<groupNo,newNodeNos>
			//复制节点
			for (Node node:nodeList){
				String groupNo=mapNG.get(node.getNo());
				if (groupNo==null){
					String oldNodeNo=node.getNo();
					String newNodeNo=newFlow.copyNode(node);
					mapNodeNewOld.put(oldNodeNo,newNodeNo);
				}else {
					String[] groupNodes=mapGNs.get(groupNo);
					List<Node> newNodeList=newFlow.copyNodes(groupNodes,node.getX(),node.getY());
					Map<String,String> map=new HashMap<>();
					for (int i=0;i<groupNodes.length;i++){
						map.put(groupNodes[i],newNodeList.get(i).getNo());
					}
					mapGroupNodeNewOld.put(node.getNo(),map);
				}
			}

			//复制方向
			for (Direction direction:directionsList){
				String fromGroupNo=mapNG.get(direction.getNode()+"");
				String toGroupNo=mapNG.get(direction.getToNode()+"");
				NodeGroup group;
				if (fromGroupNo==null&&toGroupNo==null){//原来流程中连线node---->node
					direction.setNode(Integer.valueOf(mapNodeNewOld.get(direction.getNode()+"")));
					direction.setToNode(Integer.valueOf(mapNodeNewOld.get(direction.getToNode()+"")));
				}else if (fromGroupNo==null){//node---->group
					direction.setNode(Integer.valueOf(mapNodeNewOld.get(direction.getNode()+"")));
					group=new NodeGroup(toGroupNo);

					//先获取模糊节点对应的group，再获取group中对应的新节点的编号
					Map<String,String> map=mapGroupNodeNewOld.get(direction.getToNode()+"");
					direction.setToNode(Integer.valueOf(map.get(group.GetValStrByKey(NodeGroupAttr.inNodeNo))));
				}else if (toGroupNo==null){//group---->node
					direction.setToNode(Integer.valueOf(mapNodeNewOld.get(direction.getToNode()+"")));
					group=new NodeGroup(fromGroupNo);
					Map<String,String> map=mapGroupNodeNewOld.get(direction.getNode()+"");
					direction.setNode(Integer.valueOf(map.get(group.GetValStrByKey(NodeGroupAttr.outNodeNo))));
				}else {//group--->group
					group=new NodeGroup(fromGroupNo);
					Map<String,String> map=mapGroupNodeNewOld.get(direction.getNode()+"");
					direction.setNode(Integer.valueOf(map.get(group.GetValStrByKey(NodeGroupAttr.outNodeNo))));
					group=new NodeGroup(toGroupNo);
					map=mapGroupNodeNewOld.get(direction.getToNode()+"");
					direction.setToNode(Integer.valueOf(map.get(group.GetValStrByKey(NodeGroupAttr.inNodeNo))));
				}
				direction.setFK_Flow(newFlow.getNo());
				direction.Insert();
			}

		}
		return flowNoList;
	}








}
