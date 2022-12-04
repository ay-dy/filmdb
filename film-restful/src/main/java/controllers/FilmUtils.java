package controllers;

import java.io.StringWriter;
import java.util.ArrayList;

import com.google.gson.Gson;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import models.Film;

public class FilmUtils {

	public static String getFormattedFilms(ArrayList<Film> films, String format)
	{
		switch (format) {
		case "json":
			return new Gson().toJson(films);
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
			return "";
		}
	}
}
