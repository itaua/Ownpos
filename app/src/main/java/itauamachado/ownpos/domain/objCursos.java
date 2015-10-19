package itauamachado.ownpos.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by itauafm on 13/09/2015.
 */
public class objCursos implements Parcelable{

    //curso
    private long id;
    private String tipo;
    private String area;
    private String curso;
    private String img;
    private String href;
    private String urlbase;



    public objCursos(){
        super();
    }



    public objCursos(long id, String tipo, String area, String curso, String img, String href) {
        this.id = id;
        this.tipo = tipo;
        this.area = area;
        this.curso = curso;
        this.img = img;
        this.href = href;
    }

    public void setUrlbase(String urlbase) {
        this.urlbase = urlbase;
    }

    public String getUrlbase() {
        return urlbase;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }


    // PARCELABLE
    public objCursos(Parcel parcel) {

        setId(parcel.readLong());
        setTipo(parcel.readString());
        setArea(parcel.readString());
        setCurso(parcel.readString());
        setImg(parcel.readString());
        setHref(parcel.readString());
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeLong(getId());
        dest.writeString(getTipo());
        dest.writeString(getArea());
        dest.writeString(getCurso());
        dest.writeString(getImg());
        dest.writeString(getHref());
    }
    public static final Parcelable.Creator<objAcervo> CREATOR = new Parcelable.Creator<objAcervo>(){
        @Override
        public objAcervo createFromParcel(Parcel source) {
            return new objAcervo(source);
        }
        @Override
        public objAcervo[] newArray(int size) {
            return new objAcervo[size];
        }
    };
}
