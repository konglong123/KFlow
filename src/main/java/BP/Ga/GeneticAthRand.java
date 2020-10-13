package BP.Ga;

import BP.NodeGroup.ComposeGroup;
import BP.WF.Flow;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * @program: kflow-web
 * @description:随机生成测试数据
 * @author: Mr.Kong
 * @create: 2020-10-04 14:08
 **/
public class GeneticAthRand {
    private ArrayList<Chromosome> group=new ArrayList<>();
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
    private int mohuNum=100;//模糊节点数
    public List<List<Gene>> groupGeneAll=new ArrayList<>(mohuNum);//所有基因（genesAll(i)表示第i个模糊节点，所有可选基因）
    public int geneKindMaxNum=5000;//每个模糊节点，可选择的最大方案数
    ComposeGroup composeGroup =null;
    double maxFitness;
    double aveFitness;
    List<Double> m;//精英个体中对应基因的平均值
    List<Double> cgm;//精英个体中对应基因的方差
    float pc1=0.9f;
    float pc2=0.6f;
    float pm1=0.2f;
    float pm2=0.01f;
    int type=1;
    Random random=new Random();
    List<Double> maxHistory;
    List<Double> aveHistory;

    public GeneticAthRand(){

    }

    //初始化种群所有基因可能
    //基因编码
    public GeneticAthRand(ComposeGroup composeGroup) throws Exception{
        this.composeGroup=composeGroup;
        for (int i=0;i<mohuNum;i++){
            List<Gene> genes=new ArrayList<>();
            f1:for(int j=0;j<geneKindMaxNum;j++){
                Gene gene=new Gene(i+"_"+j,(int) Math.floor(Math.random()*100)+5,j);
                gene.setIFitness(Math.random()*2);
                genes.add(gene);
            }
            groupGeneAll.add(genes);
        }
    }

    //初始化原始种群
    public void initGroup(){
        group.clear();
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

    //种群突变，染色体中基因发生突变
    private void mutation(){

        for (Chromosome chromosome:group){
            if (this.type==3){
                int geneNum=chromosome.chromosome.size();
                boolean mark=true;
                    while (mark) {
                    Chromosome child = new Chromosome(this.groupGeneAll);//变异产生的新子代
                    for (int i = 0; i < geneNum; i++) {
                        Gene gene = chromosome.get(i);
                        if (random.nextLong() <= this.pm2) {
                            child.add(variationN(gene,i));
                            mark=false;
                        } else
                            child.add(gene);
                    }
                    if (!mark)//保证变异能够产生新个体
                        groupNew.add(child);
                }
            }else {
                Chromosome newChr = chromosome.chroVar(getPm(chromosome.getIFitness()));
                groupNew.add(newChr);
            }
        }
    }
    //正态分布变异
    private Gene variationN(Gene gene,int index){
        Double tempM = this.m.get(index);
        Double tempCgma = this.cgm.get(index);
        int newPos=0;
        double FK=0.01f;
        if (aveHistory.size()>=2){
            int len=aveHistory.size();
            FK=0.01*aveHistory.get(len-1)/(Math.abs(aveHistory.get(len-2)-aveHistory.get(len-1)));
        }
        int count=0;
        boolean mark=false;
        do {
            mark=false;
            count++;
            newPos=(int)Math.floor(tempM+random.nextGaussian()*groupGeneAll.get(index).size());
            if (newPos>=groupGeneAll.get(index).size())
                mark=true;
            else if (newPos<0)
                mark=true;
            else if (newPos==gene.pos){
                mark=true;
            }
        }while (mark);
        return groupGeneAll.get(index).get(newPos);
    }

    //对种群进行选择，采用精英选择策略
    private  void select(){

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

    //初始化种群交叉率，变异率相关参数
    private void initGroupPossible(){
        //初始化交叉率，变异率相关参数
        double sumAveFitness=0d;
        maxFitness=0d;
        for (Chromosome chromosome:group){
            sumAveFitness+=chromosome.getIFitness();
            maxFitness=Math.max(maxFitness,chromosome.getIFitness());
        }
        aveFitness=sumAveFitness/group.size();

        //初始化正态变异相关参数
        List<Double> tempM=new ArrayList<>(mohuNum);
        List<Double> tempCgm=new ArrayList<>(mohuNum);
        for (int i=0;i<mohuNum;i++){
            int eliteNum=(int)Math.floor(composeGroup.getGroupNum()*composeGroup.getElitePro());
            double sum=0d;
            for (int j=0;j<eliteNum;j++){
                sum+=group.get(j).get(i).pos;
            }
            tempM.add(sum/mohuNum);
        }
        this.m=tempM;
        for (int i=0;i<mohuNum;i++){
            int eliteNum=(int)Math.floor(composeGroup.getGroupNum()*composeGroup.getElitePro());
            double mItem=this.m.get(i);
            double sum=0d;
            for (int j=0;j<eliteNum;j++){
                sum+=(group.get(j).get(i).pos-mItem)*(group.get(j).get(i).pos-mItem);
            }
            tempCgm.add(Math.sqrt(sum/mohuNum));
        }
        this.cgm=tempCgm;

    }

    //进行进化
    public JSONObject run(int type) throws Exception{
        this.type=type;

        initGroup();//初始化种群
        int count=0;//进化代数
        this.maxHistory=new ArrayList<>();
        this.aveHistory=new ArrayList<>();
        while(count<composeGroup.getGenerateNum()){
            initGroupPossible();
            maxHistory.add(maxFitness);
            aveHistory.add(aveFitness);
            crossOver();//交叉
            mutation();//变异
            select();
            count++;
        }

        this.composeGroup.setScore(group.get(0).getIFitness());

        JSONObject data=new JSONObject();
        data.put("aveHistory",aveHistory);
        data.put("maxHistory",maxHistory);
        return data;

    }


}
