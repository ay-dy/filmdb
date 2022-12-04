package models;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlType(propOrder = { "id", "title", "year", "director", "stars", "review" })
public class Film
{
	public Film() {}

	public Film(int id, String title, int year, String director, String stars, String review) {
		super();
		this.id = id;
		this.title = title;
		this.year = year;
		this.director = director;
		this.stars = stars;
		this.review = review;
	}

	int id;
	String title;
	int year;
	String director;
	String stars;
	String review;

	@XmlElement
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	@XmlElement
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	@XmlElement
	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	@XmlElement
	public String getDirector()
	{
		return director;
	}

	public void setDirector(String director)
	{
		this.director = director;
	}

	@XmlElement
	public String getStars()
	{
		return stars;
	}

	public void setStars(String stars)
	{
		this.stars = stars;
	}

	@XmlElement
	public String getReview()
	{
		return review;
	}

	public void setReview(String review)
	{
		this.review = review;
	}

	@Override
	public String toString()
	{
		return "Film [id=" + id + ", title=" + title + ", year=" + year + ", director=" + director + ", stars=" + stars
				+ ", review=" + review + "]";
	}

}
