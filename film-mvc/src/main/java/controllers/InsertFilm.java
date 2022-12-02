package controllers;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.FilmDAO;
import models.Film;

@WebServlet("/insert")
public class InsertFilm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		RequestDispatcher dispatcher = request.getRequestDispatcher("insert.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		FilmDAO dao = FilmDAO.getInstance();

		/*
		 * Store film data in the same order as it is in the database (title, year,
		 * director, stars, review).
		 */
		String[] data = request.getParameterValues("insert-data");

		/*
		 * Give the film the next available ID (passing "0" to a column set to
		 * AUTO_INCREMENT does the job). Use the data array to populate the remaining
		 * fields.
		 */
		dao.insertFilm(new Film(0, data[0], Integer.parseInt(data[1]), data[2], data[3], data[4]));

		/*
		 * Redirect the response to prevent the "insert" form from being resubmitted
		 * upon page refresh.
		 */
		response.sendRedirect("http://localhost:8081/film-mvc/home");
	}

}
