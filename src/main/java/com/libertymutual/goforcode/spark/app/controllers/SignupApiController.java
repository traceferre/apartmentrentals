package com.libertymutual.goforcode.spark.app.controllers;

import org.mindrot.jbcrypt.BCrypt;

import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import org.javalite.common.JsonHelper;

import spark.Route;
import spark.Request;
import spark.Response;

public class SignupApiController {
	
	public static final Route create = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			
//			User user = User.findFirst("email = ?", email);
//			if (user != null) {
//				res.redirect("/login?email=missing");
//				return "";
//			}
			
			User user = new User();
			user.fromMap(JsonHelper.toMap(req.body()));
			
			String password = user.getPassword();
			password = BCrypt.hashpw(password, BCrypt.gensalt());
			user.setPassword(password);
			
			user.saveIt();
			res.status(201);
			return user.toJson(true);
						
		}
	};

}
