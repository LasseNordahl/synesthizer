package com.cs122b.web;

import com.cs122b.service.UserService;
import com.cs122b.model.User;
import com.cs122b.utils.JsonParse;
import com.cs122b.utils.RecaptchaVerifyUtils;

import com.google.gson.*;

import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.SQLException;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "UserServlet", urlPatterns = "/user")
public class UserServlet extends HttpServlet {
    private Gson gson = new Gson();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonRequestBody = JsonParse.toJson(request.getReader());

        PrintWriter out = response.getWriter();

//        try {
//            RecaptchaVerifyUtils.verify(jsonRequestBody.get("captcha").getAsString());
//        } catch (Exception e) {
//            response.setStatus(400);
//            out.write("{\"message\": \"No captcha provided or invalid captcha\"}");
//            return;
//        }

        User user = null;
        try {
            user = UserService.createUser(jsonRequestBody);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(400);
            out.write("{ \"message\": \"resource not created\"}");
            return;
        } catch (NamingException e) {
            e.printStackTrace();
        }

        if (user == null) {
            response.setStatus(400);
            out.write("{ \"message\": \"resource not created\"}");
            return;
        }

        String userResponse = this.gson.toJson(user);
        out.write(userResponse);
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject jsonRequestBody = JsonParse.toJson(request.getReader());

        Object user_id_obj = request.getSession().getAttribute("user_id");

        PrintWriter out = response.getWriter();

        try {
            RecaptchaVerifyUtils.verify(jsonRequestBody.get("captcha").getAsString());
        } catch (Exception e) {
            response.setStatus(400);
            out.write("{\"message\": \"No captcha provided or invalid captcha\"}");
        }

        if (user_id_obj == null) {
            response.setStatus(401);
            out.write("{ \"message\": \"must be logged in\"}");
            return;
        }
        int user_id = (Integer) user_id_obj;
        jsonRequestBody.addProperty("user_id", Integer.toString(user_id));

        User user = null;
        try {
            user = UserService.updateUser(jsonRequestBody);
        } catch (SQLException e) {
            e.printStackTrace();
            response.setStatus(400);
            out.write("{ \"message\": \"resource not updated\"}");
            return;
        } catch (NamingException e) {
            e.printStackTrace();
        }

        if (user == null) {
            response.setStatus(400);
            out.write("{ \"message\": \"email already being used\"}");
            return;
        }

        String userResponse = this.gson.toJson(user);
        out.write(userResponse);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int user_id = (Integer) request.getSession().getAttribute("user_id");

        User user = null;
        try {
            user = UserService.fetchUser(user_id);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NamingException e) {
            e.printStackTrace();
        }

        PrintWriter out = response.getWriter();

        if (user == null) {
            response.setStatus(404);
            out.print("{ \"message\": \"resource not found\"}");
        } else {
            String trackResponse = this.gson.toJson(user);
            out.print(trackResponse);
        }
    }
}