package itauamachado.ownpos.domain;

import android.os.Parcel;
import android.os.Parcelable;


public class objNoticias implements Parcelable {

    private long id;
    private String diames;
    private String titulo;
    private String descri;
    private String link;

    public objNoticias(long id, String diames, String titulo, String descri, String link) {
        this.id = id;
        this.diames = diames;
        this.titulo = titulo;
        this.descri = descri;
        this.link = link;
    }
    public objNoticias(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDiames() {
        return diames;
    }

    public void setDiames(String diames) {
        this.diames = diames;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescri() {
        return descri;
    }

    public void setDescri(String descri) {
        this.descri = descri;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    // PARCELABLE
        public objNoticias(Parcel parcel) {
            setId(parcel.readLong());
            setDiames(parcel.readString());
            setTitulo(parcel.readString());
            setDescri(parcel.readString());
            setLink(parcel.readString());

        }
        @Override
        public int describeContents() {
            return 0;
        }
        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(getId());
            dest.writeString(getDiames());
            dest.writeString(getTitulo());
            dest.writeString(getDescri());
            dest.writeString(getLink());
        }
        public static final Creator<objNoticias> CREATOR = new Creator<objNoticias>(){
            @Override
            public objNoticias createFromParcel(Parcel source) {
                return new objNoticias(source);
            }
            @Override
            public objNoticias[] newArray(int size) {
                return new objNoticias[size];
            }
        };
}
