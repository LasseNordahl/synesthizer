package com.cs122b.web;

import com.cs122b.model.Album;
import com.cs122b.service.AlbumService;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "AlbumsServlet", urlPatterns = { "/albums" })
public class AlbumsServlet extends HttpServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String offset = request.getParameter("offset");
        String limit = request.getParameter("limit");
        String sortBy = request.getParameter("sortBy");

        List<Album> albums = null;
        try {
            albums = AlbumService.fetchAlbums(offset != null && offset != "" ? Integer.parseInt(offset) : 0,
                    limit != null && limit != "" ? Integer.parseInt(limit) : 20,
                    sortBy != null && sortBy != "" ? sortBy : "popularity desc");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        if (albums.size() > 0) {
            String albumsResponse = this.gson.toJson(albums);
            out.print("{ \"albums\": " + albumsResponse + " }");
        } else {
            response.setStatus(404);
            out.print("{ \"message\": \"resource not found\"}");
        }
    }
}
