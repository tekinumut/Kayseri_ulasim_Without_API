package com.example.kayseri_ulasim.Model.ModelOthers;


public class ModelBusLinesOzelBilgi {

  private String dakika;
  private String aciklama;
  
  public ModelBusLinesOzelBilgi(String dakika, String aciklama) {
	 this.dakika = dakika;
	 this.aciklama = aciklama;
  }
  
  public String getDakika() {
	 return dakika;
  }
  
  public String getAciklama() {
	 return aciklama;
  }
  
}
