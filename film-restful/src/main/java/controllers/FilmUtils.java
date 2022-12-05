package controllers;

import java.io.StringWriter;
import java.util.ArrayList;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import models.Film;

public class FilmUtils
{
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public static String getFormattedFilms(ArrayList<Film> films, String format)
	{
		switch (format) {
		case "json":
			return gson.toJson(films);
		case "xml":
			try {
				FilmList xmlPreparedFilms = new FilmList(films);
				StringWriter writer = new StringWriter();

				JAXBContext context = JAXBContext.newInstance(FilmList.class);
				Marshaller marshaller = context.createMarshaller();

				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.marshal(xmlPreparedFilms, writer);

				return writer.toString();
			} catch (JAXBException e) {
				e.printStackTrace();
			}
		default:
			String stringPreparedFilms = "Id|Title|Year|Director|Stars|Review\n";
			
			for (Film film : films) {	
				stringPreparedFilms += 
					(
						Integer.toString(film.getId()) + "|" 
						+ film.getTitle() + "|"
						+ Integer.toString(film.getYear()) + "|" 
						+ film.getDirector() + "|" 
						+ film.getStars() + "|"
						+ film.getReview() + "\n"
					);
			}
			
			return stringPreparedFilms;
		}
	}

	public static String getContentType(String format)
	{
		switch (format) {
		case "json":
			return "application/json";
		case "xml":
			return "text/xml";
		default:
			return "text/plain";
		}
	}
	
	public static String formatMessage(String message, String format) {
		switch (format) {
		case "json":
			return gson.toJson(message);
		case "xml":
			return "";
		default:
			return "";
		}
	}
}
