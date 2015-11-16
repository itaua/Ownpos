package itauamachado.ownpos.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.mikepenz.materialdrawer.model.ProfileDrawerItem;

public class objUsuario implements Parcelable {

    private String id;
    private String id_senac;
    private String urlSocial;
    private String urlPhoto;
    private String nome;
    private String email;
    private String loginSocial;
    private String perfil;
    private String status;
    private int background;
    private ProfileDrawerItem profile;



    public objUsuario(){
    }

    public objUsuario(String perfil, String id, String nome, String id_senac, String status) {
        this.perfil = perfil;
        this.id = id;
        this.nome = nome;
        this.status = status;
        this.id_senac = id_senac;

    }

    public ProfileDrawerItem getProfile(){
        return this.profile;
    }


    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public String getUrlSocial() {
        return urlSocial;
    }

    public void setUrlSocial(String urlSocial) {
        this.urlSocial = urlSocial;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginSocial() {
        return loginSocial;
    }

    public void setLoginSocial(String loginSocial) {
        this.loginSocial = loginSocial;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId_senac() {
        return id_senac;
    }

    public void setId_senac(String id_senac) {
        this.id_senac = id_senac;
    }

    //PARCEABLE
    public objUsuario(Parcel parcel) {
        setId_senac(parcel.readString());
        setNome(parcel.readString());
        setEmail(parcel.readString());
        setLoginSocial(parcel.readString());
        setUrlPhoto(parcel.readString());
        setUrlSocial(parcel.readString());
        setBackground(parcel.readInt());
        setPerfil(parcel.readString());
        setStatus(parcel.readString());
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId_senac());
        dest.writeString(getNome());
        dest.writeString(getEmail());
        dest.writeString(getLoginSocial());
        dest.writeString(getUrlPhoto());
        dest.writeString(getUrlSocial());
        dest.writeInt(getBackground());
        dest.writeString(getPerfil());
        dest.writeString(getStatus());

    }
    public static final Creator<objUsuario> CREATOR = new Creator<objUsuario>(){
        @Override
        public objUsuario createFromParcel(Parcel source) {
            return new objUsuario(source);
        }
        @Override
        public objUsuario[] newArray(int size) {
            return new objUsuario[size];
        }
    };
}
