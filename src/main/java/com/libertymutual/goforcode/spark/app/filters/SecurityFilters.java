package com.libertymutual.goforcode.spark.app.filters;

import static spark.Spark.halt;

import java.util.UUID;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;

import spark.Filter;
import spark.Request;
import spark.Response;

public class SecurityFilters {

	public static Filter isAuthenticated = (Request req, Response res) -> {
		if (req.session().attribute("currentUser") == null) {
			res.redirect("/login?returnPath=" + req.pathInfo());
			halt();
		}
	};
	
	public static Filter isNew = (Request req, Response res) -> {
		if (req.session().isNew()) {
			 UUID token = UUID.randomUUID();
			 req.session().attribute("csrf", token);
		}
	};
	
	public static Filter isPostSameToken = (Request req, Response res) -> {		
		if (req.requestMethod() == "POST" && !req.session().attribute("csrf").toString().equals(req.queryParams("token"))) {
			res.redirect("/");
			halt();
		}
	};
	
	public static Filter isOwner = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = Apartment.findById(Integer.parseInt((req.params("id"))));
			User user = req.session().attribute("currentUser");
			if (!user.getId().equals(apartment.getUserId())) {
				res.redirect("/");
				halt();
			}
		}		
	};
	
	

}
