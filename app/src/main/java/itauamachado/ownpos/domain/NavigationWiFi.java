package itauamachado.ownpos.domain;

/**
 * Created by itauafm on 13/10/2015.
 */
public class NavigationWiFi {
    private String capabilities;
    private int Frequency;
    private int Level;
    private String SSID;
    private String BSSID;
    private int With;
    private int Height;


    public NavigationWiFi(){

    }

    public int getWith() {
        return With;
    }

    public void setWith(int with) {
        With = with;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    public int getFrequency() {
        return Frequency;
    }

    public void setFrequency(int frequency) {
        Frequency = frequency;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
    }

    public String toString(){
        String result = "";

        result = result + "capabilities: "+ this.getCapabilities();
        result = result + " Frequency: "+ this.getFrequency();
        result = result + " Level: "+ this.getLevel();
        result = result + " SSID: "+ this.getSSID();;
        result = result + " BSSID: "+ this.getBSSID();
        result = result + " With: "+ this.getWith();
        result = result + " Height: "+ this.getHeight();


        return result;
    }

}
