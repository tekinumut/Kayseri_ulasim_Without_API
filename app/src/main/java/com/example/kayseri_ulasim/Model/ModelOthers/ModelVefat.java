package com.example.kayseri_ulasim.Model.ModelOthers;

import com.google.gson.annotations.SerializedName;

public class ModelVefat {
  
  @SerializedName("AdSoyad")
  private String name;
  @SerializedName("BabaAdi")
  private String baba_adi;
  @SerializedName("AnaAdi")
  private String ana_adi;
  @SerializedName("DogumYeri")
  private String dogum_yeri;
  @SerializedName("DogumYili")
  private String dogum_yili;
  @SerializedName("CenazeNamaziYeri")
  private String namaz_yeri;
  @SerializedName("CenazeNamaziVakti")
  private String namaz_vakti;
  @SerializedName("TaziyeAdresi")
  private String taziye_adresi;
  @SerializedName("Mezarlik")
  private String mezarlik;
  @SerializedName("MezarlikKonum")
  private ModelMezarlikKonum mezarlik_konum;
  @SerializedName("TaziyeKonum")
  private ModelTaziyeKonum taziye_konum;
  
  public ModelVefat(String name, String baba_adi, String ana_adi, String dogum_yeri, String dogum_yili, String namaz_yeri, String namaz_vakti,
						  String taziye_adresi, String mezarlik, ModelMezarlikKonum mezarlik_konum, ModelTaziyeKonum taziye_konum) {
	 this.name = name;
	 this.baba_adi = baba_adi;
	 this.ana_adi = ana_adi;
	 this.dogum_yeri = dogum_yeri;
	 this.dogum_yili = dogum_yili;
	 this.namaz_yeri = namaz_yeri;
	 this.namaz_vakti = namaz_vakti;
	 this.taziye_adresi = taziye_adresi;
	 this.mezarlik = mezarlik;
	 this.mezarlik_konum = mezarlik_konum;
	 this.taziye_konum = taziye_konum;
  }
  
  public String getName() {
	 return name;
  }
  
  public String getBaba_adi() {
	 return baba_adi;
  }
  
  public String getAna_adi() {
	 return ana_adi;
  }
  
  public String getDogum_yeri() {
	 return dogum_yeri;
  }
  
  public String getDogum_yili() {
	 return dogum_yili;
  }
  
  public String getNamaz_yeri() {
	 return namaz_yeri;
  }
  
  public String getNamaz_vakti() {
	 return namaz_vakti;
  }
  
  public String getTaziye_adresi() {
	 return taziye_adresi;
  }
  
  public String getMezarlik() {
	 return mezarlik;
  }
  
  public ModelMezarlikKonum getMezarlik_konum() {
	 return mezarlik_konum;
  }
  
  public ModelTaziyeKonum getTaziye_konum() {
	 return taziye_konum;
  }
}
