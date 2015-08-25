package controller;

public class VideoJob {
	
	private String inputVideoPath, indexFilePath, outputPath, ffmpegPath;
	public String getFfmpegPath() {
		return ffmpegPath;
	}

	public void setFfmpegPath(String ffmpegPath) {
		this.ffmpegPath = ffmpegPath;
	}

	private boolean deinterlace, denoise, scale, audioLevel; 
	private String minBitrate, maxBitrate, bufferSize, volumeAdjustment;
	private String lumaSpatialStrength, chromaSpatialStrength, temporalLumaStrength, temporalChromaStrength;
	private String encodingPreset, crfValue, scalingValue;
	boolean cutVideo, deinterlaceVideo, denoiseVideo, scaleVideo, audioLevelVideo;
	private String inputVideoFolderPath;
	private String deinterlaceThresh, deinterlaceSharp, deinterlaceTwoWay, deinterlaceMap, deinterlaceOrder;
	private String watermarkPath;
	
	public VideoJob(String inputVideoPath, String indexFilePath,
			String outputPath, String ffmpegPath, String inputVideoFolderPath, boolean deinterlace, boolean denoise, boolean scale,
			boolean audioLevel, String minBitrate, String maxBitrate,
			String bufferSize, String volumeAdjustment,
			String lumaSpatialStrength, String chromaSpatialStrength,
			String temporalLumaStrength, String temporalChromaStrength,
			String encodingPreset, String crfValue, String scalingValue,
			String deinterlaceThresh, String deinterlaceSharp, String deinterlaceTwoWay, String deinterlaceMap, 
			String deinterlaceOrder, String watermarkPath) {
		this.inputVideoFolderPath = inputVideoFolderPath;
		this.inputVideoPath = inputVideoPath;
		this.indexFilePath = indexFilePath;
		this.outputPath = outputPath;
		this.ffmpegPath = ffmpegPath;
		this.deinterlace = deinterlace;
		this.denoise = denoise;
		this.scale = scale;
		this.audioLevel = audioLevel;
		this.minBitrate = minBitrate;
		this.maxBitrate = maxBitrate;
		this.bufferSize = bufferSize;
		this.volumeAdjustment = volumeAdjustment;
		this.lumaSpatialStrength = lumaSpatialStrength;
		this.chromaSpatialStrength = chromaSpatialStrength;
		this.temporalLumaStrength = temporalLumaStrength;
		this.temporalChromaStrength = temporalChromaStrength;
		this.encodingPreset = encodingPreset;
		this.crfValue = crfValue;
		this.scalingValue = scalingValue;
		this.deinterlaceThresh = deinterlaceThresh;
		this.deinterlaceSharp = deinterlaceSharp;
		this.deinterlaceTwoWay = deinterlaceTwoWay;
		this.deinterlaceMap = deinterlaceMap;
		this.deinterlaceOrder = deinterlaceOrder;
		this.watermarkPath = watermarkPath;
	}

	public String getWatermarkPath() {
		return watermarkPath;
	}

	public void setWatermarkPath(String watermarkPath) {
		this.watermarkPath = watermarkPath;
	}

	public String getDeinterlaceThresh() {
		return deinterlaceThresh;
	}

	public void setDeinterlaceThresh(String deinterlaceThresh) {
		this.deinterlaceThresh = deinterlaceThresh;
	}

	public String getDeinterlaceSharp() {
		return deinterlaceSharp;
	}

	public void setDeinterlaceSharp(String deinterlaceSharp) {
		this.deinterlaceSharp = deinterlaceSharp;
	}

	public String getDeinterlaceTwoWay() {
		return deinterlaceTwoWay;
	}

	public void setDeinterlaceTwoWay(String deinterlaceTwoWay) {
		this.deinterlaceTwoWay = deinterlaceTwoWay;
	}

	public String getDeinterlaceMap() {
		return deinterlaceMap;
	}

	public void setDeinterlaceMap(String deinterlaceMap) {
		this.deinterlaceMap = deinterlaceMap;
	}

	public String getDeinterlaceOrder() {
		return deinterlaceOrder;
	}

	public void setDeinterlaceOrder(String deinterlaceOrder) {
		this.deinterlaceOrder = deinterlaceOrder;
	}

	public String getInputVideoFolderPath() {
		return inputVideoFolderPath;
	}

	public void setInputVideoFolderPath(String inputVideoFolderPath) {
		this.inputVideoFolderPath = inputVideoFolderPath;
	}

	public String getInputVideoPath() {
		return inputVideoPath;
	}

	public void setInputVideoPath(String inputVideoPath) {
		this.inputVideoPath = inputVideoPath;
	}

	public String getIndexFilePath() {
		return indexFilePath;
	}

	public void setIndexFilePath(String indexFilePath) {
		this.indexFilePath = indexFilePath;
	}

	public boolean isDeinterlace() {
		return deinterlace;
	}

	public void setDeinterlace(boolean deinterlace) {
		this.deinterlace = deinterlace;
	}

	public boolean isDenoise() {
		return denoise;
	}

	public void setDenoise(boolean denoise) {
		this.denoise = denoise;
	}

	public boolean isScale() {
		return scale;
	}

	public void setScale(boolean scale) {
		this.scale = scale;
	}

	public boolean isAudioLevel() {
		return audioLevel;
	}

	public void setAudioLevel(boolean audioLevel) {
		this.audioLevel = audioLevel;
	}

	public String getMinBitrate() {
		return minBitrate;
	}

	public void setMinBitrate(String minBitrate) {
		this.minBitrate = minBitrate;
	}

	public String getMaxBitrate() {
		return maxBitrate;
	}

	public void setMaxBitrate(String maxBitrate) {
		this.maxBitrate = maxBitrate;
	}

	public String getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(String bufferSize) {
		this.bufferSize = bufferSize;
	}

	public String getVolumeAdjustment() {
		return volumeAdjustment;
	}

	public void setVolumeAdjustment(String volumeAdjustment) {
		this.volumeAdjustment = volumeAdjustment;
	}

	public String getLumaSpatialStrength() {
		return lumaSpatialStrength;
	}

	public void setLumaSpatialStrength(String lumaSpatialStrength) {
		this.lumaSpatialStrength = lumaSpatialStrength;
	}

	public String getChromaSpatialStrength() {
		return chromaSpatialStrength;
	}

	public void setChromaSpatialStrength(String chromaSpatialStrength) {
		this.chromaSpatialStrength = chromaSpatialStrength;
	}

	public String getTemporalLumaStrength() {
		return temporalLumaStrength;
	}

	public void setTemporalLumaStrength(String temporalLumaStrength) {
		this.temporalLumaStrength = temporalLumaStrength;
	}

	public String getTemporalChromaStrength() {
		return temporalChromaStrength;
	}

	public void setTemporalChromaStrength(String temporalChromaStrength) {
		this.temporalChromaStrength = temporalChromaStrength;
	}

	public String getEncodingPreset() {
		return encodingPreset;
	}

	public void setEncodingPreset(String encodingPreset) {
		this.encodingPreset = encodingPreset;
	}

	public String getCrfValue() {
		return crfValue;
	}

	public void setCrfValue(String crfValue) {
		this.crfValue = crfValue;
	}

	public String getScalingValue() {
		return scalingValue;
	}

	public void setScalingValue(String scalingValue) {
		this.scalingValue = scalingValue;
	}

	@Override
	public String toString() {
		return "VideoJob [inputVideoPath=" + inputVideoPath
				+ ", indexFilePath=" + indexFilePath + ", outputPath="
				+ outputPath + ", deinterlace=" + deinterlace + ", denoise="
				+ denoise + ", scale=" + scale + ", audioLevel=" + audioLevel
				+ ", minBitrate=" + minBitrate + ", maxBitrate=" + maxBitrate
				+ ", bufferSize=" + bufferSize + ", volumeAdjustment="
				+ volumeAdjustment + ", lumaSpatialStrength="
				+ lumaSpatialStrength + ", chromaSpatialStrength="
				+ chromaSpatialStrength + ", temporalLumaStrength="
				+ temporalLumaStrength + ", temporalChromaStrength="
				+ temporalChromaStrength + ", encodingPreset=" + encodingPreset
				+ ", crfValue=" + crfValue + ", scalingValue=" + scalingValue
				+ "]";
	}

	public String getOutputPath() {
		return outputPath;
	}

	public void setOutputPath(String outputPath) {
		this.outputPath = outputPath;
	}

	public boolean isCutVideo() {
		return cutVideo;
	}

	public void setCutVideo(boolean cutVideo) {
		this.cutVideo = cutVideo;
	}

	public boolean isDeinterlaceVideo() {
		return deinterlaceVideo;
	}

	public void setDeinterlaceVideo(boolean deinterlaceVideo) {
		this.deinterlaceVideo = deinterlaceVideo;
	}

	public boolean isDenoiseVideo() {
		return denoiseVideo;
	}

	public void setDenoiseVideo(boolean denoiseVideo) {
		this.denoiseVideo = denoiseVideo;
	}

	public boolean isScaleVideo() {
		return scaleVideo;
	}

	public void setScaleVideo(boolean scaleVideo) {
		this.scaleVideo = scaleVideo;
	}

	public boolean isAudioLevelVideo() {
		return audioLevelVideo;
	}

	public void setAudioLevelVideo(boolean audioLevelVideo) {
		this.audioLevelVideo = audioLevelVideo;
	}
	
	
}
