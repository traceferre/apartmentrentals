package com.libertymutual.goforcode.spark.app.controllers;

import java.util.HashMap;
import java.util.Map;

import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class SignupController {

	public static final Route newForm = (Request req, Response rep) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		model.put("csrf", req.session().attribute("csrf"));
		return MustacheRenderer.getInstance().render("signup/signup.html", model);
	};
	
	public static final Route create = (Request req, Response rep) -> {
		String email = req.queryParams("email");
		String password = req.queryParams("password");
		String firstName = req.queryParams("first_name");
		String lastName = req.queryParams("last_name");
		
		password = BCrypt.hashpw(password, BCrypt.gensalt());
		
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User user = User.findFirst("email = ?", email);
			if (user != null) {
				rep.redirect("/login?email=missing");
				return "";
			}
			user = new User(email, password, firstName, lastName);
			req.session().attribute("currentUser", user);
			user.saveIt();
		}			
		rep.redirect("/");
		return "";
	};

}
