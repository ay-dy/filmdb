package controllers;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.FilmDAO;

@WebServlet("/delete")
public class DeleteFilm extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		FilmDAO dao = FilmDAO.getInstance();

		int id = Integer.parseInt(request.getParameter("did"));

		dao.deleteFilm(id);

		/*
		 * Redirect the response to prevent the "delete" form from being resubmitted
		 * upon page refresh.
		 */
		response.sendRedirect("http://localhost:8081/film-mvc/home");
	}

}
