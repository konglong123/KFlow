package BP.Ga;

public class Gene implements Cloneable{
	private String nodeGroupNo;//节点分组编码
	private int nodeNum;//分组中节点数目
	private String nodeNo;//模糊节点编码
	private double iFitness;//数值越大，适应度越大
	public int pos;//维度值
	public Gene(String nodeGroupNo,int nodeNum,int pos){
		this.nodeGroupNo=nodeGroupNo;
		this.nodeNum=nodeNum;
		this.pos=pos;
	}

	public String getNodeGroupNo() {
		return nodeGroupNo;
	}

	public void setNodeGroupNo(String nodeGroupNo) {
		this.nodeGroupNo = nodeGroupNo;
	}

	public int getNodeNum() {
		return nodeNum;
	}

	public void setNodeNum(int nodeNum) {
		this.nodeNum = nodeNum;
	}

	public double getIFitness() {
		return iFitness;
	}

	public void setIFitness(double iFitness) {
		this.iFitness = iFitness;
	}

	public String getNodeNo() {
		return nodeNo;
	}

	public void setNodeNo(String nodeNo) {
		this.nodeNo = nodeNo;
	}
}
