package itauamachado.ownpos.domain;

import android.graphics.PointF;
import android.location.Location;

//event bus
public class MessageEB {

    private Location location;
    private String mResult;
    private Double distancia;
    private int idJobSchedule;
    private PointF ownpos;
    private ItensMapa pontoA;
    private ItensMapa pontoB;
    private String mTag;
    private boolean noSENAC = false;
    private float percentual;

    //TAG MessageBus
    public static final String TAG_WIFI_INFO = "wfInfo";
    public static final String TAG_INDOOR_ATLAS = "indoorAtlas";
    public static final String TAG_LOCATION_API = "locationAPI";
    public static final String TAG_LOCATION = "JobScheduler";
    public static final String TAG_MAPA_INDOOR = "mapaIndoor";

    public float getPercentual() {
        return percentual;
    }

    public void setPercentual(float percentual) {
        this.percentual = percentual;
    }

    public boolean isNoSENAC() {
        return noSENAC;
    }

    public void setNoSENAC(boolean noSENAC) {
        this.noSENAC = noSENAC;
    }

    public MessageEB(){
        this.mResult = "";
    }

    public String getmTag() {
        return mTag;
    }

    public void setmTag(String mTag) {
        this.mTag = mTag;
    }

    public PointF getOwnpos() {
        return ownpos;
    }

    public void setOwnpos(PointF ownpos) {
        this.ownpos = ownpos;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getmResult() {
        return mResult;
    }

    public void setmResult(String mResult) {
        this.mResult = mResult;
    }

    public Double getDistancia() {
        return distancia;
    }

    public void setDistancia(Double distancia) {
        this.distancia = distancia;
    }

    public int getIdJobSchedule() {
        return idJobSchedule;
    }

    public void setIdJobSchedule(int idJobSchedule) {
        this.idJobSchedule = idJobSchedule;
    }

    public ItensMapa getPontoA() {
        return pontoA;
    }

    public void setPontoA(ItensMapa pontoA) {
        this.pontoA = pontoA;
    }

    public ItensMapa getPontoB() {
        return pontoB;
    }

    public void setPontoB(ItensMapa pontoB) {
        this.pontoB = pontoB;
    }
}
