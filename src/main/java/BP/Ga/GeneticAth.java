package BP.Ga;

import BP.GPM.Group;
import BP.NodeGroup.NodeGroupAttr;
import BP.Tools.BeanTool;
import BP.WF.Flow;
import BP.WF.Node;
import BP.WF.Nodes;
import BP.WF.Template.NodeAttr;
import BP.springCloud.controller.NLPModelController;
import com.alibaba.fastjson.JSONObject;

import javax.swing.*;
import java.util.*;


public class GeneticAth {
	private  ArrayList<Chromosome> group=new ArrayList<>();
	private  ArrayList<Chromosome> groupNew=new ArrayList<>();
	private  int groupNum=40;//种群数目
	private  float variationPro=0.5f;//种群突变的比率
	private float acrossPro=0.7f;//染色体交叉的概率
	private int generateNum=100;//默认100代
	private float elitePro=0.1f;//精英选择比例
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
	private String flowNo;
	private int mohuNum;//模糊节点数
	List<List<Gene>> groupGeneAll=new ArrayList<>(mohuNum);//所有基因（genesAll(i)表示第i个模糊节点，所有可选基因）
	double geneKindMaxNum=100;//每个模糊节点，可选择的最大方案数
	int category=2;//1为关键字检索策略，2为word2vec策略
	public GeneticAth(){

	}

	//初始化种群所有基因可能
	public GeneticAth(String flowNo) throws Exception{
		Flow flow=new Flow(flowNo);
		Nodes nodes=flow.getHisNodes();
		List<Node> nodeList=nodes.toList();
		Map<String,String> map=new HashMap<>();//<NodeNo,abstracts>
		for (Node node:nodeList){
			int type=node.GetValIntByKey(NodeAttr.RunModel);
			if (type==10) {//模糊节点（有时间改成枚举）
				map.put(node.getNo(),node.getTip());
			}
		}
		List<List<Gene>> groupGene=new ArrayList<>(map.size());

		//计算各种历史模板的相似度
		if (category==2){
			NLPModelController nlpModel= BeanTool.getBean(NLPModelController.class);
			for (Map.Entry<String,String> entry:map.entrySet()){
				List<JSONObject> nodeGroups=nlpModel.queryNodeGroupByWord2(entry.getValue());
				List<Gene> genes=new ArrayList<>();
				int count=0;
				f1:for(JSONObject nodeGroup:nodeGroups){
					Group group=(Group)nodeGroup.get("group");
					Gene gene=new Gene(group.getNo(),group.GetValIntByKey(NodeGroupAttr.nodeNum));
					gene.setIFitness(nodeGroup.getFloat("score"));
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
		for(int j=0;j<groupNum;j++){
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
		int pos[]=new int[4];
		int num=(int)Math.floor(groupNum*acrossPro);//需要交叉的染色体数量
		for(int i=0;i<num;i+=2){
			pos=getOperationPos();
			doublePointAcross(pos);
		}
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
			child1.add(parent1.get(i));
			child1.add(parent2.get(i));
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
		pos[2]=(int)Math.floor(Math.random()*groupNum);
		do{
			pos[3]=(int)Math.floor(Math.random()*groupNum);
		}while(pos[2]==pos[3]);

		return pos;
	}

	//种群突变，染色体中基因发生突变
	private void mutation(){
		Set<Integer> set=new HashSet();
		int num=(int)Math.floor(groupNum*variationPro);//突变染色体数目
		do{
			set.add((int)Math.floor(Math.random()*groupNum));//选定变异染色体
		}while(set.size()<num);
		for (Integer pos:set){
			Chromosome chr=group.get(pos);
			groupNew.add(chr.chroVar());
		}
	}

	//对种群进行选择，采用精英选择策略
	private  void select(){
		Chromosome tempChr;

		Collections.sort(group,comparator);
		Collections.sort(groupNew,comparator);

		//选择个体
		int eliteNum=(int)Math.floor(groupNum*elitePro);
		for(int i=0;i<groupNum-eliteNum;i++){
			tempChr=groupNew.get(i);
			group.add(i+eliteNum,tempChr);
		}
		Collections.sort(group,comparator);

		groupNew.clear();
	}

	//计算种群的适应度
	private  double calculateGroup(){
		double fitness=0d;
		Chromosome s;
		for(int i=0;i<group.size();i++){
			s=group.get(i);
			fitness+=s.getIFitness();
		}
		return fitness;
	}

	//进行进化
	public JSONObject run(){

		initGroup();//初始化种群
		int count=0;//进化代数
		List<Double> history=new ArrayList<>();
		while(count<generateNum){
			double fitness=calculateGroup();
			history.add(fitness);
			crossOver();//交叉
			mutation();//变异
			select();
			count++;
		}

		JSONObject data=new JSONObject();
		data.put("history",history);
		data.put("group",group);
		return data;

	}


}
