package model;

public class FragmentedVideo {
	
	private String artistName, songTitle, startTimeCode, endTimeCode, trackNumber, fileNumber, 
			videoCode,  fileName, filePath;

	public FragmentedVideo(String artistName, String songTitle,
			String startTimeCode, String endTimeCode, String trackNumber,
			String fileNumber, String videoCode, String fileName, String filePath) {
		this.artistName = artistName;
		this.songTitle = songTitle;
		this.startTimeCode = startTimeCode;
		this.endTimeCode = endTimeCode;
		this.trackNumber = trackNumber;
		this.fileNumber = fileNumber;
		this.videoCode = videoCode;
		this.fileName = fileName;
		this.filePath = filePath;
	}

	public String getArtistName() {
		return artistName;
	}

	public void setArtistName(String artistName) {
		this.artistName = artistName;
	}

	public String getSongTitle() {
		return songTitle;
	}

	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}

	public String getStartTimeCode() {
		return startTimeCode;
	}

	public void setStartTimeCode(String startTimeCode) {
		this.startTimeCode = startTimeCode;
	}

	public String getEndTimeCode() {
		return endTimeCode;
	}

	public void setEndTimeCode(String endTimeCode) {
		this.endTimeCode = endTimeCode;
	}

	public String getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}

	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	public String getVideoCode() {
		return videoCode;
	}

	public void setVideoCode(String videoCode) {
		this.videoCode = videoCode;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	@Override
	public String toString() {
		return "FileNumber: " + fileNumber + " videoCode: " + videoCode + " trackNumber: " + trackNumber 
				+ " startTime: " + startTimeCode + " endTimeCode: " + endTimeCode
				+ " artistName: " + artistName + " songTitle: " + songTitle;
	}
	
}
