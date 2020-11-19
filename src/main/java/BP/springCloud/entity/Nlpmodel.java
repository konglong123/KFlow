package BP.springCloud.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * nlp
 * @author pcc
 */
public class Nlpmodel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键
	 */
	private int No;
	
	/**
	 * 模型名
	 */
	private String name;
	
	/**
	 * 模型文件路径
	 */
	private String modelFile;
	
	/**
	 * 测试文件
	 */
	private String testFile;
	
	/**
	 * 数据文件
	 */
	private String trainFile;
	
	/**
	 * 语言
	 */
	private String language;
	
	/**
	 * 词汇量
	 */
	private int termSize;
	
	/**
	 * 维度（隐藏层神经元数量）
	 */
	private int dimensions;
	
	/**
	 * 模型内存大小
	 */
	private int memorySize;
	
	/**
	 * 训练用时
	 */
	private String useTime;
	
	/**
	 * 训练数目
	 */
	private int trainTermSize;
	
	/**
	 * 测试词汇量
	 */
	private int testTermSize;
	
	/**
	 * 正确率
	 */
	private Double correctRate;
	
	/**
	 * 压缩比例
	 */
	private Float compressRate;
	
	/**
	 * 类型（1word2vec，2分词器模型）
	 */
	private int modelType;
	
	/**
	 * 删除标志
	 */
	private int isDelete;
	
	/**
	 * 隐藏层神经元数量
	 */
	private int layerSize;
	
	/**
	 * 神经网络类型(1,CBOW,2skip)
	 */
	private int neuralNetworkType;
	
	/**
	 * 输出层是否归一化
	 */
	private int hierarchicalSoftmax;
	
	/**
	 * 负采样数量
	 */
	private int negativeSamples;
	
	/**
	 * 训练线程数
	 */
	private int numThreads;
	
	/**
	 * 学习率
	 */
	private Double learningRate;
	
	/**
	 * 未知
	 */
	private Double downSampleRate;
	
	/**
	 * 迭代次数
	 */
	private int iterations;
	
	/**
	 * 最小概率
	 */
	private int minFrequency;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 更新时间
	 */
	private Date updateTime;
	
	/**
	 * 备注
	 */
	private String context;

	private String historyNo;
	
	/**
	 * @return the id
	 */
	public int getNo() {
		return No;
	}
	
	/**
	 * @param No the id to set
	 */
	public void setNo(int No) {
		this.No = No;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the modelFile
	 */
	public String getModelFile() {
		return modelFile;
	}
	
	/**
	 * @param modelFile the modelFile to set
	 */
	public void setModelFile(String modelFile) {
		this.modelFile = modelFile;
	}
	
	/**
	 * @return the testFile
	 */
	public String getTestFile() {
		return testFile;
	}
	
	/**
	 * @param testFile the testFile to set
	 */
	public void setTestFile(String testFile) {
		this.testFile = testFile;
	}
	
	/**
	 * @return the trainFile
	 */
	public String getTrainFile() {
		return trainFile;
	}
	
	/**
	 * @param trainFile the trainFile to set
	 */
	public void setTrainFile(String trainFile) {
		this.trainFile = trainFile;
	}
	
	/**
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}
	
	/**
	 * @param language the language to set
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	
	/**
	 * @return the termSize
	 */
	public int getTermSize() {
		return termSize;
	}
	
	/**
	 * @param termSize the termSize to set
	 */
	public void setTermSize(int termSize) {
		this.termSize = termSize;
	}
	
	/**
	 * @return the dimensions
	 */
	public int getDimensions() {
		return dimensions;
	}
	
	/**
	 * @param dimensions the dimensions to set
	 */
	public void setDimensions(int dimensions) {
		this.dimensions = dimensions;
	}
	
	/**
	 * @return the memorySize
	 */
	public int getMemorySize() {
		return memorySize;
	}
	
	/**
	 * @param memorySize the memorySize to set
	 */
	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}
	
	/**
	 * @return the useTime
	 */
	public String getUseTime() {
		return useTime;
	}
	
	/**
	 * @param useTime the useTime to set
	 */
	public void setUseTime(String useTime) {
		this.useTime = useTime;
	}
	
	/**
	 * @return the trainTermSize
	 */
	public int getTrainTermSize() {
		return trainTermSize;
	}
	
	/**
	 * @param trainTermSize the trainTermSize to set
	 */
	public void setTrainTermSize(int trainTermSize) {
		this.trainTermSize = trainTermSize;
	}
	
	/**
	 * @return the testTermSize
	 */
	public int getTestTermSize() {
		return testTermSize;
	}
	
	/**
	 * @param testTermSize the testTermSize to set
	 */
	public void setTestTermSize(int testTermSize) {
		this.testTermSize = testTermSize;
	}
	
	/**
	 * @return the correctRate
	 */
	public Double getCorrectRate() {
		return correctRate;
	}
	
	/**
	 * @param correctRate the correctRate to set
	 */
	public void setCorrectRate(Double correctRate) {
		this.correctRate = correctRate;
	}
	
	/**
	 * @return the compressRate
	 */
	public Float getCompressRate() {
		return compressRate;
	}
	
	/**
	 * @param compressRate the compressRate to set
	 */
	public void setCompressRate(Float compressRate) {
		this.compressRate = compressRate;
	}
	
	/**
	 * @return the modelType
	 */
	public int getModelType() {
		return modelType;
	}
	
	/**
	 * @param modelType the modelType to set
	 */
	public void setModelType(int modelType) {
		this.modelType = modelType;
	}
	
	/**
	 * @return the isDelete
	 */
	public int getIsDelete() {
		return isDelete;
	}
	
	/**
	 * @param isDelete the isDelete to set
	 */
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	
	/**
	 * @return the layerSize
	 */
	public int getLayerSize() {
		return layerSize;
	}
	
	/**
	 * @param layerSize the layerSize to set
	 */
	public void setLayerSize(int layerSize) {
		this.layerSize = layerSize;
	}
	
	/**
	 * @return the neuralNetworkType
	 */
	public int getNeuralNetworkType() {
		return neuralNetworkType;
	}
	
	/**
	 * @param neuralNetworkType the neuralNetworkType to set
	 */
	public void setNeuralNetworkType(int neuralNetworkType) {
		this.neuralNetworkType = neuralNetworkType;
	}
	
	/**
	 * @return the hierarchicalSoftmax
	 */
	public int getHierarchicalSoftmax() {
		return hierarchicalSoftmax;
	}
	
	/**
	 * @param hierarchicalSoftmax the hierarchicalSoftmax to set
	 */
	public void setHierarchicalSoftmax(int hierarchicalSoftmax) {
		this.hierarchicalSoftmax = hierarchicalSoftmax;
	}
	
	/**
	 * @return the negativeSamples
	 */
	public int getNegativeSamples() {
		return negativeSamples;
	}
	
	/**
	 * @param negativeSamples the negativeSamples to set
	 */
	public void setNegativeSamples(int negativeSamples) {
		this.negativeSamples = negativeSamples;
	}
	
	/**
	 * @return the numThreads
	 */
	public int getNumThreads() {
		return numThreads;
	}
	
	/**
	 * @param numThreads the numThreads to set
	 */
	public void setNumThreads(int numThreads) {
		this.numThreads = numThreads;
	}
	
	/**
	 * @return the learningRate
	 */
	public Double getLearningRate() {
		return learningRate;
	}
	
	/**
	 * @param learningRate the learningRate to set
	 */
	public void setLearningRate(Double learningRate) {
		this.learningRate = learningRate;
	}
	
	/**
	 * @return the downSampleRate
	 */
	public Double getDownSampleRate() {
		return downSampleRate;
	}
	
	/**
	 * @param downSampleRate the downSampleRate to set
	 */
	public void setDownSampleRate(Double downSampleRate) {
		this.downSampleRate = downSampleRate;
	}
	
	/**
	 * @return the iterations
	 */
	public int getIterations() {
		return iterations;
	}
	
	/**
	 * @param iterations the iterations to set
	 */
	public void setIterations(int iterations) {
		this.iterations = iterations;
	}
	
	/**
	 * @return the minFrequency
	 */
	public int getMinFrequency() {
		return minFrequency;
	}
	
	/**
	 * @param minFrequency the minFrequency to set
	 */
	public void setMinFrequency(int minFrequency) {
		this.minFrequency = minFrequency;
	}
	
	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}
	
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	/**
	 * @return the context
	 */
	public String getContext() {
		return context;
	}
	
	/**
	 * @param context the context to set
	 */
	public void setContext(String context) {
		this.context = context;
	}

	public String getHistoryNo() {
		return historyNo;
	}

	public void setHistoryNo(String historyNo) {
		this.historyNo = historyNo;
	}
}