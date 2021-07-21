package com.example.whitecollar.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity//Annotation is required to specify a JPA and Hibernate entity
public class Shop {
	 @Id//Annotation is required to specify the identifier property of the entity
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private int capacitat_maxima;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCapacitat_maxima() {
		return capacitat_maxima;
	}

	public void setCapacitat_maxima(int capacitat_maxima) {
		this.capacitat_maxima = capacitat_maxima;
	}
    
}