package controllers;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.FilmDAO;
import models.Film;

@WebServlet("/home")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{
		FilmDAO dao = FilmDAO.getInstance();
		ArrayList<Film> allFilms = dao.getAllFilms();

		request.setAttribute("films", allFilms);
		/*
		 * The result count will be used to determine whether to display the table or an
		 * error message.
		 */
		request.setAttribute("resultCount", allFilms.size());

		RequestDispatcher dispatcher = request.getRequestDispatcher("home.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException
	{
		response.sendRedirect("http://localhost:8081/film-mvc/home");
	}

}
