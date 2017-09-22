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

public class SessionController {

	public static final Route newForm = (Request req, Response rep) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("email", req.queryParams("email"));
		model.put("returnPath", req.queryParams("returnPath"));
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		model.put("csrf", req.session().attribute("csrf"));
		return MustacheRenderer.getInstance().render("login/login.html", model);
	};
	
	public static final Route create = (Request req, Response rep) -> {
		String email = req.queryParams("email");
		String password = req.queryParams("password");
		
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User user = User.findFirst("email = ?", email);
			if (user != null && BCrypt.checkpw(password, user.getPassword())) {
					req.session().attribute("currentUser", user);
			}
		}
		
		if (req.queryParams("returnPath") != null) {
			rep.redirect(req.queryParams("returnPath"));
		}
		rep.redirect("/");
		return "";
	};

	public static final Route destroy = (Request req, Response rep) -> {
		req.session().invalidate();
		rep.redirect("/");
		return "";
	};

}
