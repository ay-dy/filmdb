package controllers;

import java.util.ArrayList;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import models.Film;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "films")
public class FilmList
{
	@XmlElement(name = "film")
	private ArrayList<Film> films;

	public FilmList() {
	}

	public FilmList(ArrayList<Film> films) {
		this.films = films;
	}

	public ArrayList<Film> getFilms()
	{
		return films;
	}

	public void setFilms(ArrayList<Film> films)
	{
		this.films = films;
	}

}
