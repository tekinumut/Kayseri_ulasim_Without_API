package com.example.kayseri_ulasim.Model;

import android.graphics.Bitmap;

public class ModelHtgMap {
  
  private String title;
  private String snippet;
  private Bitmap icon;
  
  public ModelHtgMap(String title, String snippet, Bitmap icon) {
	 this.title = title;
	 this.snippet = snippet;
	 this.icon = icon;
  }
  
  public String getTitle() {
	 return title;
  }
  
  public String getSnippet() {
	 return snippet;
  }
  
  public Bitmap getIcon() {
	 return icon;
  }
}
