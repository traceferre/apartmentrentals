package com.libertymutual.goforcode.spark.app.filters;

import static spark.Spark.halt;

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

}
