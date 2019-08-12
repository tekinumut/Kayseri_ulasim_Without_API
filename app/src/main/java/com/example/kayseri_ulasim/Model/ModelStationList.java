package com.example.kayseri_ulasim.Model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Favorites")
public class ModelStationList {
 
 @PrimaryKey()
 public int id;
 @ColumnInfo(name = "name")
 private String name;
 @ColumnInfo(collate = 4)
 private Double latitude;
 @ColumnInfo(collate = 4)
 private Double longitude;
 @ColumnInfo(collate = 4)
 private Double distance;
 
 
 public ModelStationList(int id, String name, Double latitude, Double longitude, Double distance) {
  this.id = id;
  this.name = name;
  this.latitude = latitude;
  this.longitude = longitude;
  this.distance = distance;
 }
 
 public int getId() {
  return id;
 }
 
 public String getName() {
  return name;
 }
 
 public Double getLatitude() {
  return latitude;
 }
 
 public Double getLongitude() {
  return longitude;
 }
 
 public Double getDistance() {
  return distance;
 }
}
