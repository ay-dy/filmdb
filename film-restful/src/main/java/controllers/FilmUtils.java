package controllers;

import java.io.StringWriter;
import java.util.ArrayList;

import com.google.gson.GsonBuilder;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import models.Film;

public class FilmUtils
{
	public static String getFormattedFilms(ArrayList<Film> films, String format)
	{
		switch (format) {
		case "json":
			/*
			 * Convert film ArrayList to a JSON string with indentations etc. for better
			 * readability.
			 */
			return new GsonBuilder().setPrettyPrinting().create().toJson(films);
		case "xml":
			try {
				FilmList xmlPreparedFilms = new FilmList(films);
				StringWriter sw = new StringWriter();

				JAXBContext jc = JAXBContext.newInstance(FilmList.class);
				Marshaller m = jc.createMarshaller();

				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				m.marshal(xmlPreparedFilms, sw);

				return sw.toString();
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		default:
			String stringPreparedFilms = "Id|Title|Year|Director|Stars|Review\n";
			for (Film film : films) {
				stringPreparedFilms += 
						(
							Integer.toString(film.getId())
							+ "|" + film.getTitle() 
							+ "|" + Integer.toString(film.getYear())
							+ "|" + film.getDirector()
							+ "|" + film.getStars()
							+ "|" + film.getReview() + "\n"
						);
			}
			return stringPreparedFilms;
		}
	}
}
