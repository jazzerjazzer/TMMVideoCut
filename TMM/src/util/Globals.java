package util;

public class Globals {
	
	public static String[] resolutions = new String[] {"1920x1080", "1280x800"};
	
	public static String[] encodingPresets = new String[]{"ultrafast", "superfast", "veryfast", "faster", "fast",
											"medium", "slow", "slower", "veryslow"};
	
	public static String[] crfValues = new String[]{"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"};
	public static String crfDefaultValue = "23";
	
	public static String lumaStrengthDefaultValue = "1.5";
	public static String chromaStrengthDefaultValue = "1.5";
	public static String temporalLumaStrengthDefaultValue = "6.0";
	public static String temporalChromaStrengthDefaultValue = "6.0";
	
	public static String minBitrateDefaultValue = "4000";
	public static String maxBitrateDefaultValue = "4000";
	public static String bufferSizeDefaultValue = "1835";
	
	public static String volumeAdjustmentDefaultValue = "2.0";
	
	public static String deinterlaceThreshValue = "10";
	public static String deinterlaceSharpValue = "1";
	public static String deinterlaceTwoWayValue = "1";
	public static String deinterlaceMapValue = "0";
	public static String deinterlaceOrderValue = "1";
}
