package controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import database.FilmDAO;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import models.Film;

@WebServlet("/filmapi")
public class FilmAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	FilmDAO dao = FilmDAO.getInstance();
	Gson gson = new Gson();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		
		ArrayList<Film> allFilms = dao.getAllFilms();
		PrintWriter writer = response.getWriter();
		String format = request.getParameter("format");
		String output = "";

		if (format.equals("json")) {
			response.setContentType("application/json");
			output = gson.toJson(allFilms);
		} else if (format.equals("xml")) {
			response.setContentType("text/xml");
			
			try {
				JAXBContext jc = JAXBContext.newInstance(Film.class);
				Marshaller m = jc.createMarshaller();
				StringWriter sw = new StringWriter();
				m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				m.marshal(allFilms, sw);
				output = sw.toString();
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			
		} else {
			response.setContentType("text/plain");
		}
		
		System.out.println(output);
		writer.print(output);
		writer.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException
	{

	}

}
