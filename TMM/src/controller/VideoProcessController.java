package controller;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

import model.FragmentedVideo;
import model.IndexFileReader;

public class VideoProcessController {

	IndexFileReader indexFileReader = new IndexFileReader();
	ArrayList <FragmentedVideo> fragmentedVideoParameters;
	FragmentedVideo currentVideo;
	LogController logController = new LogController();
	int exitVal;
	String cutOutputFolder;
	ArrayList<VideoJob> videoJobs = new ArrayList<>();

	public void addToJobList(VideoJob v, JTextPane statusArea){
		videoJobs.add(v);
		cutProcessVideo(v, statusArea);
	}
	
	public int cutProcessVideo(VideoJob v, JTextPane statusArea){
				
		System.out.println("process video : " + v.getInputVideoFolderPath());
		VideoCutRunnable R1 = new VideoCutRunnable(v, statusArea);
	    R1.start();
		return 0;
	}
	public void startCropping(VideoJob v, JTextPane statusArea){
		VideoCropEncodeRunnable R1 = new VideoCropEncodeRunnable(v, statusArea);
	    R1.start();
	}
	public String cutVideo(String indexFilePath, String inputVideoPath, String outputFolderPath, String ffmpegPath,
			JTextPane statusArea){
		appendToPane(statusArea, "Cutting started...", Color.WHITE);
		cutOutputFolder = outputFolderPath;
		fragmentedVideoParameters = indexFileReader.getFragmentedVideoParameters(indexFilePath);

		String inputVideoName = inputVideoPath.substring(inputVideoPath.lastIndexOf('/'), inputVideoPath.length());
		String extension = inputVideoName.substring(inputVideoName.lastIndexOf('.'));

		for(int i = 0; i < fragmentedVideoParameters.size(); i++){
			currentVideo = fragmentedVideoParameters.get(i);
			System.out.println("start: "+currentVideo.getStartTimeCode() + " end: " + currentVideo.getEndTimeCode());
			try {

				Runtime rt = Runtime.getRuntime();
				Process proc;

				File f = new File(outputFolderPath);
				if (f.exists() && f.isDirectory()) {

				}else{
					File dir = new File(outputFolderPath);
					dir.mkdir();
				}

				String[] cutCommand = new String[]{ffmpegPath, "-y", "-i", inputVideoPath, "-metadata", "title="
						+currentVideo.getTrackNumber(),"-metadata", "artist="+currentVideo.getFileNumber(),
						"-ss",currentVideo.getStartTimeCode(),
						"-to", currentVideo.getEndTimeCode(), "-acodec","copy","-vcodec", "copy", 
						outputFolderPath+"/"+currentVideo.getFileName()+extension};
				proc = rt.exec(cutCommand);
				System.out.println("CUT COMMAND");
				for(int j = 0; j < cutCommand.length; j++)
					System.out.println(cutCommand[j]);
				InputStream stderr = proc.getErrorStream();
				InputStreamReader isr = new InputStreamReader(stderr);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				try {
					while ( (line = br.readLine()) != null){
						System.out.println(line);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				try {
					exitVal = proc.waitFor();
					if(exitVal == 0){
						System.out.println("CUT SUCCESS");
						appendToPane(statusArea, "\n " + currentVideo.getFileName()+extension + " is cut succesfully.", Color.GREEN);
					}else{
						System.out.println("CUT FAIL");
						appendToPane(statusArea, "\n " + currentVideo.getFileName()+extension + " cannot be cut.", Color.RED);
					}
				} catch (InterruptedException e) {
					appendToPane(statusArea, "\n " + currentVideo.getFileName()+extension + " cannot be cut, exception thrown.", 
							Color.RED);
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return outputFolderPath;
	}

	public void watermarkVideo(String ffmpegPath, String watermarkPath, 
			String inputVideoFolderPath, JTextPane statusArea, VideoJob v){
		
		appendToPane(statusArea, "\nWatermarking...", Color.WHITE);

		String watermarkInputPath = inputVideoFolderPath +"/temp2/";
		String watermarkOutputPath = inputVideoFolderPath +"/h264/";

		File f = new File(watermarkOutputPath);
		if (f.exists() && f.isDirectory()) {

		}else{
			File dir = new File(watermarkOutputPath);
			dir.mkdir();
		}
		
		File folder = new File(watermarkInputPath);
		File[] listOfFiles = folder.listFiles();
		for(int i = 0; i < listOfFiles.length; i++){
			
			Runtime rt = Runtime.getRuntime();
			Process proc = null;

			String[] watermarkCommand = new String[]{ffmpegPath, "-y", "-i", listOfFiles[i].getAbsolutePath(), 
					"-i", watermarkPath, "-filter_complex", "overlay=0:0","-b:v", v.getMinBitrate()+"k", "-acodec", "copy", 
					"-strict", "-2", watermarkOutputPath+listOfFiles[i].getName()};
			try {
				proc = rt.exec(watermarkCommand);

				InputStream stderr = proc.getErrorStream();
				InputStreamReader isr = new InputStreamReader(stderr);
				BufferedReader br = new BufferedReader(isr);
				String line = null;
				try {
					while ( (line = br.readLine()) != null){
						System.out.println(line);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				int result = proc.waitFor();
				if(result == 0){
					appendToPane(statusArea, "\nVideo " + listOfFiles[i].getName() 
							+ " is watermarked successfully", Color.GREEN);
				}else{
					appendToPane(statusArea, "\nVideo " + listOfFiles[i].getName() 
							+ " cannot be watermarked successfully", Color.RED);
				}
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		appendToPane(statusArea, "Deleting temp directories...", Color.WHITE);
		
		deleteDirectory(new File(inputVideoFolderPath + "/temp"));
		deleteDirectory(new File(inputVideoFolderPath + "/temp2"));
		appendToPane(statusArea, "\nDone!", Color.MAGENTA);
		
	}

	public ArrayList<CropData> cropDetect(String inputFolderPath, String ffmpegPath, String inputVideoFolderPath, 
			JTextPane statusArea, VideoJob v){
	    
		ArrayList <CropData> cropValues = new ArrayList<>();
		
	    appendToPane(statusArea, "\nDetecting crop values...", Color.WHITE);
		String cropOutput = inputVideoFolderPath +"/temp/";
		System.out.println("Crop Output Path: " + cropOutput);
		File f = new File(cropOutput);
		if (f.exists() && f.isDirectory()) {

		}else{
			File dir = new File(cropOutput);
			dir.mkdir();
		}
		String cropValue="";
		File folder = new File(inputFolderPath);
		File[] listOfFiles = folder.listFiles();
		int fileCount = 0;
		for(int i = 0; i < listOfFiles.length; i++){
			if(listOfFiles[i].toString().contains(".DS_Store"))
				continue;
			else
				fileCount++;
		}
		
		System.out.println("Crop Detect File Count: " + fileCount);
		
		for(int i = 0; i < fileCount; i++){
			if(listOfFiles[i].toString().contains(".DS_Store"))
				continue;

			Runtime rt = Runtime.getRuntime();
			Process proc = null;

			String[] cropDetectCommand = new String[]{ffmpegPath, "-y", "-i", 
					listOfFiles[i].toString(), "-pix_fmt", "yuv420p", "-vf", 
					"kerndeint=thresh="+v.getDeinterlaceThresh()+":sharp="+v.getDeinterlaceSharp()+
					":twoway="+v.getDeinterlaceTwoWay()+":map="+v.getDeinterlaceMap()+":order="+v.getDeinterlaceOrder()
					+",cropdetect=24:16:0"+",hqdn3d=1.5:1.5:6:6",
					"-vcodec", "libx264", "-b:v", "25000k","-b:a","256k","-af:", "volume="+v.getVolumeAdjustment(), 
					"-strict", "-4",cropOutput + listOfFiles[i].getName().substring(0, 
																		listOfFiles[i].getName().lastIndexOf('.')) + ".mp4" };
			
			System.out.println("CROP DETECT COMMAND");
			for(int j = 0; j < cropDetectCommand.length; j++){
				System.out.println(cropDetectCommand[j]);
			}
			try {
				proc = rt.exec(cropDetectCommand);
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			InputStream stderr = proc.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			boolean cropAdded = false;
			String newFileName;
			try {
				while ( (line = br.readLine()) != null){
					System.out.println(line);
					if(line.contains("crop=") && line.contains("t:30") && !cropAdded){
						cropValue = line.substring(line.indexOf("=")+1);
						appendToPane(statusArea, "\nCrop value for video " + listOfFiles[i].getName() + " is: " + cropValue, 
								Color.GREEN);
						newFileName = listOfFiles[i].getName().substring(0,listOfFiles[i].getName().lastIndexOf('.')) + ".mp4";
						cropValues.add(new CropData(cropOutput + newFileName, cropValue, newFileName));
						cropAdded = true;
					}else{
						System.out.println(listOfFiles[i].getAbsolutePath() + " " + cropValue);
					}
					/*if(line.contains("crop=") && line.contains("t:30")){
						cropValue = line.substring(line.indexOf("=")+1);
						System.out.println("\nCrop value for video " + listOfFiles[i].getName() + " is: " + cropValue);
						appendToPane(statusArea, "\nCrop value for video " + listOfFiles[i].getName() + " is: " + cropValue, 
								Color.GREEN);
						System.out.println(listOfFiles[i].getAbsolutePath() + " " + cropValue);
						cropValues.add(new CropData(cropOutput + listOfFiles[i].getName(), cropValue, listOfFiles[i].getName()));
						
						break;
					}*/
				}
				cropAdded = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return cropValues;
	}

	public void crop(ArrayList<CropData> cropValues, String ffmpegPath, String inputFolderPath, String inputVideoFolderPath, 
			JTextPane statusArea, VideoJob v){
		
		appendToPane(statusArea, "\nCropping...", Color.WHITE);
		inputFolderPath = inputVideoFolderPath + "/temp";
		
		File folder = new File(inputFolderPath);
		File[] listOfFiles = folder.listFiles();

		deleteDirectory(new File(inputVideoFolderPath+"/temp2"));

		File dir = new File(inputVideoFolderPath+"/temp2");
		dir.mkdir();
		int fileCount = 0;
		for(int i = 0; i < listOfFiles.length; i++){
			if(listOfFiles[i].toString().contains(".DS_Store"))
				continue;
			else
				fileCount++;
		}
		
		System.out.println("Crop File Count: " + fileCount);

		for(int i = 0; i < fileCount; i++){

			Runtime rt = Runtime.getRuntime();
			Process proc = null;
			if(listOfFiles[i].toString().contains(".DS_Store"))
				continue;

			String cropOutputPath = inputVideoFolderPath + "/temp2/" + cropValues.get(i).fileName.substring(0, 
					cropValues.get(i).fileName.lastIndexOf(".")) + ".mp4";
			// "crop="+(String) cropValues.get(i).fileCropValue+","+ 
			// "-s", v.getScalingValue(),
			/*
			 * 
			 * ./ffmpeg -y -i output.mp4 -vf "crop=720:432:0:72" 
			 * -s 1920x1080 -b:v 25000k -acodec copy -strict -4 output cropped.mp4
			 * 
			 * */
			String[] cropCommand = new String[]{
					ffmpegPath, "-y", "-i", cropValues.get(i).fileAbsPath,"-vf", "crop="+cropValues.get(i).fileCropValue, 
					"-s", v.getScalingValue(), "-b:v", "25000k", "-acodec", "copy", "-strict", "-4",
					cropOutputPath
			};
			System.out.println("CROP COMMAND");
			for(int k = 0; k < cropCommand.length; k++)
				System.out.println(cropCommand[k]);
			try {
				proc = rt.exec(cropCommand);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			InputStream stderr = proc.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			try {
				while ( (line = br.readLine()) != null){
					System.out.println(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int exitVal = -1;
			try {
				exitVal = proc.waitFor();
				if(exitVal == 0)
					appendToPane(statusArea, "\nVideo " + listOfFiles[i].getName() + " is cropped properly.", Color.GREEN);
				else
					appendToPane(statusArea, "\nVideo " + listOfFiles[i].getName() + " cannot be cropped properly.", Color.RED);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Exit Val for: " + listOfFiles[i] + " is: " + exitVal);
		}
	}

	public static boolean deleteDirectory(File directory) {
		if(directory.exists()){
			File[] files = directory.listFiles();
			if(null!=files){
				for(int i=0; i<files.length; i++) {
					if(files[i].isDirectory()) {
						deleteDirectory(files[i]);
					}
					else {
						files[i].delete();
					}
				}
			}
		}
		return(directory.delete());
	}
	
    private void appendToPane(JTextPane tp, String msg, Color c){
    	
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, c);

        aset = sc.addAttribute(aset, StyleConstants.FontFamily, "Lucida Console");
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }
    
	class VideoCutRunnable implements Runnable {
		private Thread t;
		VideoJob v;
		JTextPane statusArea;
		
		VideoCutRunnable(VideoJob v, JTextPane statusArea){
			this.v = v;
			this.statusArea = statusArea;
		}
		public void run() {
			
			String cutOutputFolder = cutVideo(v.getIndexFilePath(), v.getInputVideoPath(), v.getOutputPath(), 
												v.getFfmpegPath(), statusArea);
			
			/*ArrayList<CropData> tempCropValues = cropDetect(cutOutputFolder, v.getFfmpegPath(), v.getInputVideoFolderPath(),
					statusArea, v);
			crop(tempCropValues, v.getFfmpegPath(), v.getOutputPath(), v.getInputVideoFolderPath(), statusArea, v);
			
			watermarkVideo(v.getFfmpegPath(), v.getWatermarkPath(), v.getInputVideoFolderPath(), statusArea, v);*/
		}
		
		public void start () {
			if (t == null) {
				t = new Thread (this);
				t.start ();
			}
		}
	}
	
	class VideoCropEncodeRunnable implements Runnable {
		private Thread t;
		VideoJob v;
		JTextPane statusArea;
		
		VideoCropEncodeRunnable(VideoJob v, JTextPane statusArea){
			this.v = v;
			this.statusArea = statusArea;
		}
		public void run() {

			ArrayList<CropData> tempCropValues = cropDetect(cutOutputFolder, v.getFfmpegPath(), v.getInputVideoFolderPath(),
					statusArea, v);
			crop(tempCropValues, v.getFfmpegPath(), v.getOutputPath(), v.getInputVideoFolderPath(), statusArea, v);
			
			watermarkVideo(v.getFfmpegPath(), v.getWatermarkPath(), v.getInputVideoFolderPath(), statusArea, v);
		}
		
		public void start () {
			if (t == null) {
				t = new Thread (this);
				t.start ();
			}
		}
	}
	
	class CropData {
		String fileAbsPath;
		String fileCropValue;
		String fileName;
		public CropData(String fileAbsPath, String fileCropValue, String fileName){
			this.fileAbsPath = fileAbsPath;
			this.fileCropValue = fileCropValue;
			this.fileName = fileName;
		}
	}
}
