package model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;


public class IndexFileReader {
	
	private ArrayList <FragmentedVideo> fragmentedVideoParameters;
	FileInputStream file;
	Row row;
	int i = 0;
	String startStr, endStr;
	String hours, mins, secs, frames, startTimeCode, endTimeCode;
	static Cell start, end;
	
	public IndexFileReader(){}
	
	public ArrayList <FragmentedVideo> getFragmentedVideoParameters(String fileName){
		fragmentedVideoParameters = new ArrayList<FragmentedVideo>();
		
		try {
			
			file = new FileInputStream(new File(fileName));
			String extension = fileName.substring(fileName.lastIndexOf('.'));
			if(extension.equals("xls")){
				
			}else if(extension.equals(".xlsx")){
				
			}
			//Get the workbook instance for XLS file 
		    HSSFWorkbook workbook = new HSSFWorkbook(file);
		 
		    //Get first sheet from the workbook
		    HSSFSheet sheet = workbook.getSheetAt(0);
		     
		    //Iterate through each rows from first sheet
		    Iterator<Row> rowIterator = sheet.iterator();
		    
		    while(rowIterator.hasNext()) {
		    	row = rowIterator.next();

		    	if(i>2){
			    	System.out.println("row.getCell(3).toString() " + row.getCell(3).toString());
			    	if(row.getCell(3).toString().equals("") | (row.getCell(3).toString() == null)){
			    		break;
			    	}

		    		fragmentedVideoParameters.add(new FragmentedVideo(getArtistName(), getSongTitle(), 
		    				getStartTimeCode(), getEndTimeCode(), getTrackNumber(), 
		    					getFileNumber(), getVideoCode(), createFileName(getArtistName(), getSongTitle()), ""));
		    	}else{
		    		i++;
		    	}
		    }
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fragmentedVideoParameters;
	}
	
	private String getStartTimeCode(){
		
		start = row.getCell(3);
    	startStr = start.toString();
    	
    	hours = startStr.substring(0, startStr.indexOf(':'));
        mins = startStr.substring(startStr.indexOf(':')+1, startStr.indexOf('\''));        
        secs = startStr.substring(startStr.indexOf('\'')+1, startStr.indexOf('"'));
        frames =  startStr.substring(startStr.indexOf('"')+1, startStr.indexOf('f'));
        
        startTimeCode = hours+":"+mins+":"+secs+"."+Integer.parseInt(frames)*40;
             
		return startTimeCode;
	}
	
	private String getEndTimeCode(){
		end = row.getCell(4);
    	endStr = end.toString();
    	
    	hours = endStr.substring(0, endStr.indexOf(':'));
        mins = endStr.substring(endStr.indexOf(':')+1, endStr.indexOf('\''));
        secs = endStr.substring(endStr.indexOf('\'')+1, endStr.indexOf('"'));
        frames =  endStr.substring(endStr.indexOf('"')+1, endStr.indexOf('f'));
        
        String endTime = hours + ":" + mins + ":" + secs;
        
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date d = null;
		try {
			d = df.parse(endTime);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.MINUTE, 1);
        endTimeCode = df.format(cal.getTime());
        endTimeCode = endTimeCode + "."+Integer.parseInt(frames)*40;

        return endTimeCode;
	}
	
	private String getArtistName(){
		return row.getCell(1).toString();
	}
	private String getSongTitle(){
		return row.getCell(2).toString();
	}
	private String getTrackNumber(){
		if (row == null)
			return "";
		
		if((row.getCell(5) == null) | (row.getCell(5).toString().equals(""))){
			return "";
		}
		
		int trackNumber = (int) (Double.parseDouble(row.getCell(5).toString()));
		return trackNumber+"";
	}
	private String getFileNumber(){
		if (row == null)
			return ""; 
		if(row.getCell(6) == null | row.getCell(6).toString().equals(""))
			return "";
		
		int fileNumber = (int) (Double.parseDouble(row.getCell(6).toString()));
		return fileNumber+"";	
	}
	private String getVideoCode(){
		if (row == null)
			return ""; 
		if(row.getCell(7) == null | row.getCell(7).toString().equals(""))
			return "";
		return row.getCell(7).toString();
	}
	private String createFileName(String artistName, String songTitle){
		String fileName = artistName + " - " + songTitle;
		return fileName;
	}
}
