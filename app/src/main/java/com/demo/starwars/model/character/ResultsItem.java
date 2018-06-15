package com.demo.starwars.model.character;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("unused")
public class ResultsItem implements Serializable{

	@SerializedName("films")
	private List<String> films;

	@SerializedName("homeworld")
	private String homeworld;

	@SerializedName("gender")
	private String gender="";

	@SerializedName("skin_color")
	private String skinColor;

	@SerializedName("edited")
	private String edited;

	@SerializedName("created")
	private String created="";

	@SerializedName("mass")
	private String mass="";

	@SerializedName("vehicles")
	private List<String> vehicles;

	@SerializedName("url")
	private String url;

	@SerializedName("hair_color")
	private String hairColor;

	@SerializedName("birth_year")
	private String birthYear;

	@SerializedName("eye_color")
	private String eyeColor;

	@SerializedName("species")
	private List<String> species;

	@SerializedName("starships")
	private List<String> starships;

	@SerializedName("name")
	@NonNull
	private String name="";

	@SerializedName("height")
	private String height="";


	public List<String> getFilms() {
		return films;
	}

	public void setFilms(List<String> films) {
		this.films = films;
	}

	public List<String> getVehicles() {
		return vehicles;
	}

	public void setVehicles(List<String> vehicles) {
		this.vehicles = vehicles;
	}

	public List<String> getSpecies() {
		return species;
	}

	public void setSpecies(List<String> species) {
		this.species = species;
	}

	public List<String> getStarships() {
		return starships;
	}

	public void setStarships(List<String> starships) {
		this.starships = starships;
	}

	public void setHomeworld(String homeworld){
		this.homeworld = homeworld;
	}

	public String getHomeworld(){
		return homeworld;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setSkinColor(String skinColor){
		this.skinColor = skinColor;
	}

	public String getSkinColor(){
		return skinColor;
	}

	public void setEdited(String edited){
		this.edited = edited;
	}

	public String getEdited(){
		return edited;
	}

	public void setCreated(String created){
		this.created = created;
	}

	public String getCreated(){
		return created;
	}

	public void setMass(String mass){
		this.mass = mass;
	}

	public String getMass(){
		return mass;
	}


	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	public void setHairColor(String hairColor){
		this.hairColor = hairColor;
	}

	public String getHairColor(){
		return hairColor;
	}

	public void setBirthYear(String birthYear){
		this.birthYear = birthYear;
	}

	public String getBirthYear(){
		return birthYear;
	}

	public void setEyeColor(String eyeColor){
		this.eyeColor = eyeColor;
	}

	public String getEyeColor(){
		return eyeColor;
	}



	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setHeight(String height){
		this.height = height;
	}

	public String getHeight(){
		return height;
	}

	@Override
 	public String toString(){
		return 
			"ResultsItem{" + 
			",homeworld = '" + homeworld + '\'' +
			",gender = '" + gender + '\'' + 
			",skin_color = '" + skinColor + '\'' + 
			",edited = '" + edited + '\'' + 
			",created = '" + created + '\'' + 
			",mass = '" + mass + '\'' + 
			",url = '" + url + '\'' +
			",hair_color = '" + hairColor + '\'' + 
			",birth_year = '" + birthYear + '\'' + 
			",eye_color = '" + eyeColor + '\'' + 
			",name = '" + name + '\'' +
			",height = '" + height + '\'' + 
			"}";
		}
}