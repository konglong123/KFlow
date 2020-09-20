package BP.Ga;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Chromosome {
	//种群所有基因选择可能
	private List<List<Gene>> groupGeneAll=new ArrayList<>();
	private ArrayList<Gene> chromosome =new ArrayList<>();
	double iFitness=-1d;

	public Chromosome(List<List<Gene>> groupGeneAll){
		this.groupGeneAll=groupGeneAll;
	}

	//添加基因
	public void add(Gene ge){
		chromosome.add(ge);
	}
	//移除基因
	public void remove(int pos){
		chromosome.remove(pos);
	}
	public void set(int pos,Gene gene){
		chromosome.set(pos, gene);
	}
	public Gene get(int pos){
		return chromosome.get(pos);
	}
	//计算该染色体的适应度后，将适应度保留，以便下次直接调用
	public double getIFitness(){
		if(iFitness<0)
			iFitness=calculateIndividual();
		return iFitness;
	}
	//计算个体的适应度,即单个染色体的适应度
	private  double calculateIndividual(){
		double fitness=0.0d;
		int nodeNum = 0;
		for (Gene gene:chromosome) {
			fitness+=gene.getIFitness()*gene.getNodeNum();
			nodeNum+=gene.getNodeNum();
		}
		fitness=fitness/nodeNum;
		return fitness;
	}

	//对染色体进行随机变异
	public  Chromosome chroVar(float geneVPro){
		int geneNum=chromosome.size();
		//需要变异的基因数量
		int selGeneNum=(int)Math.floor(geneNum*geneVPro);
		Gene gene;
		Set<Integer> set=new HashSet<>();
		do{
			set.add((int)Math.floor(Math.random()*geneNum));//选定变异基因位置
		}while(set.size()<selGeneNum);

		Chromosome child=new Chromosome(this.groupGeneAll);//变异产生的新子代
		for(int i=0;i<geneNum;i++){
			gene =chromosome.get(i);
			if(set.contains(i))
				child.add(variation(gene,i));
			else
				child.add(gene);
		}
		return child;
	}
	//对单个基因进行变异
	private Gene variation(Gene gene,int id){
		int newPos=0;
		do {
			newPos=(int)Math.floor(Math.random()*groupGeneAll.get(id).size());
		}while (!groupGeneAll.get(id).get(newPos).getNodeGroupNo().equals(gene.getNodeGroupNo()));
		return groupGeneAll.get(id).get(newPos);
	}

	public ArrayList<Gene> getChromosome() {
		return chromosome;
	}

	public void setChromosome(ArrayList<Gene> chromosome) {
		this.chromosome = chromosome;
	}


	public List<List<Gene>> getGroupGeneAll() {
		return groupGeneAll;
	}

	public void setGroupGeneAll(List<List<Gene>> groupGeneAll) {
		this.groupGeneAll = groupGeneAll;
	}
}
