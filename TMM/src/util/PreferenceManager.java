package util;
import java.util.prefs.Preferences;

public class PreferenceManager {
	private Preferences prefs;
	public PreferenceManager(){
		prefs = Preferences.userRoot().node(this.getClass().getName());
	}
	public void putPreference(String key, String value){
		prefs.put(key, value);
	}
	public String getPreference(String key){
		return prefs.get(key, "defaultValue");
	}	
}
