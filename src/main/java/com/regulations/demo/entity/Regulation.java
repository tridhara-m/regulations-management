package com.regulations.demo.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Regulation {

	
	@Id
	private String celex;
	private String title;
	private LocalDate date;
	public String getCelex() {
		return celex;
	}
	public void setCelex(String celex) {
		this.celex = celex;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
}
