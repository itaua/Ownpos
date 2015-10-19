package itauamachado.ownpos.domain;

import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by itauafm on 20/09/2015.
 */
public class objMarker {

    private long _id;
    private String name;
    private String displayName;
    private int imageResource;
    private PointF point;
    private List<String> andares = new ArrayList<>();
    private boolean acesso;



    public objMarker(){
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public PointF getPoint() {
        return point;
    }

    public void setPoint(PointF point) {
        this.point = point;
    }

    public List<String> getAndares() {
        return andares;
    }

    public void setAndares(List<String> andares) {
        this.andares = andares;
    }

    public boolean isAcesso() {
        return acesso;
    }

    public void setAcesso(boolean acesso) {
        this.acesso = acesso;
    }
}
