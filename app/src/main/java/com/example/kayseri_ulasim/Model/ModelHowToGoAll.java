package com.example.kayseri_ulasim.Model;

import com.google.android.gms.maps.model.LatLng;

public class ModelHowToGoAll {
  
  private ModelHowToGo htgList;
  private LatLng begin_loc, end_loc;
  private double my_lat,my_lng,konumdanUzaklik, ilkDuragaUzaklik, sonDuragaUzaklik, toplamYurumeMesafesi;
  
  
  public ModelHowToGoAll(ModelHowToGo htgList, LatLng begin_loc, LatLng end_loc, double my_lat, double my_lng, double konumdanUzaklik,
								 double ilkDuragaUzaklik, double sonDuragaUzaklik, double toplamYurumeMesafesi) {
	 this.htgList = htgList;
	 this.begin_loc = begin_loc;
	 this.end_loc = end_loc;
	 this.my_lat = my_lat;
	 this.my_lng = my_lng;
	 this.konumdanUzaklik = konumdanUzaklik;
	 this.ilkDuragaUzaklik = ilkDuragaUzaklik;
	 this.sonDuragaUzaklik = sonDuragaUzaklik;
	 this.toplamYurumeMesafesi = toplamYurumeMesafesi;
  }
  
  public ModelHowToGo getHtgList() {
	 return htgList;
  }
  
  public LatLng getBegin_loc() {
	 return begin_loc;
  }
  
  public LatLng getEnd_loc() {
	 return end_loc;
  }
  
  public double getMy_lat() {
	 return my_lat;
  }
  
  public double getMy_lng() {
	 return my_lng;
  }
  
  public double getKonumdanUzaklik() {
	 return konumdanUzaklik;
  }
  
  public double getIlkDuragaUzaklik() {
	 return ilkDuragaUzaklik;
  }
  
  public double getSonDuragaUzaklik() {
	 return sonDuragaUzaklik;
  }
  
  public double getToplamYurumeMesafesi() {
	 return toplamYurumeMesafesi;
  }
}
