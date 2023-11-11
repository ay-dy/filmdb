package controllers;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
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
			// Note that the ID heading is not included as we won't display it in the table.
			// We are still making use of the ID itself though.
			String stringPreparedFilms = "ID|Title|Year|Director|Stars|Review";

			for (Film film : films) {
				stringPreparedFilms += ("\n" + Integer.toString(film.getId()) + "|" + film.getTitle() + "|"
						+ Integer.toString(film.getYear()) + "|" + film.getDirector() + "|" + film.getStars() + "|"
						+ film.getReview());
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

	public static String formatMessage(String message, String format)
	{
		switch (format) {
		case "json":
			return gson.toJson(message);
		case "xml":
			return "";
		default:
			return "";
		}
	}

	public static Film getDecodedFilm(HttpServletRequest request)
			throws JsonSyntaxException, JsonIOException, IOException, JAXBException
	{
		String acceptType = request.getHeader("Accept");
		Film film = new Film();

		switch (acceptType) {
		case "application/json":
			film = gson.fromJson(request.getReader(), Film.class);
			break;
		case "text/xml":
			String body = "";
			String line = "";

			while ((line = request.getReader().readLine()) != null) {
				body = body.concat(line);
			}

			JAXBContext context = JAXBContext.newInstance(Film.class);
			Unmarshaller u = context.createUnmarshaller();
			film = (Film) u.unmarshal(new StringReader(body));

			break;
		default:
			String[] filmData = request.getReader().readLine().split("\\|");

			film.setId(Integer.parseInt(filmData[0]));
			film.setTitle(filmData[1]);
			film.setYear(Integer.parseInt(filmData[2]));
			film.setDirector(filmData[3]);
			film.setStars(filmData[4]);
			film.setReview(filmData[5]);
		}

		return film;
	}
}
