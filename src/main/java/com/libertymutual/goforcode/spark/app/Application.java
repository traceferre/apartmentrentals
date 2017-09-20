package com.libertymutual.goforcode.spark.app;

import com.libertymutual.goforcode.spark.app.controllers.ApartmentApiController;
import com.libertymutual.goforcode.spark.app.controllers.ApartmentController;
import com.libertymutual.goforcode.spark.app.controllers.HomeController;
import com.libertymutual.goforcode.spark.app.controllers.SessionController;
import com.libertymutual.goforcode.spark.app.controllers.SignupApiController;
import com.libertymutual.goforcode.spark.app.controllers.SignupController;
import com.libertymutual.goforcode.spark.app.filters.SecurityFilters;
import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;

import spark.Request;
import spark.Response;

import static spark.Spark.*;

import org.mindrot.jbcrypt.BCrypt;

public class Application {
	
	public static void main(String[] args){	
		
		String password = BCrypt.hashpw("passw0rd", BCrypt.gensalt());
		
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User.deleteAll();
			Apartment.deleteAll();
			new User("something@aol.com", password, "trace", "ferre").saveIt();
			new Apartment(3000, 1, 0, 350, "123 Main St", "San Francisco", "CA", "94111").saveIt();
			new Apartment(1400, 3, 2.5, 1400, "500 My St", "Seattle", "WA", "98036").saveIt();
		}
		
		path("/apartments", () -> {
			before("/new", SecurityFilters.isAuthenticated);
			get("/new", ApartmentController.newForm);
			get("/:id", ApartmentController.details);	
			before("", SecurityFilters.isAuthenticated);
			post("", ApartmentController.create);
		});
		
		get("/", HomeController.index);
		get("/login", SessionController.newForm);
		post("/login", SessionController.create);
		get("/logout", SessionController.destroy);
		
		get("/signup", SignupController.newForm);
		post("/signup", SignupController.create);
		
		path("/api", () -> {
			get("/apartments/:id", ApartmentApiController.details);
			post("/apartments", ApartmentApiController.create);
			post("/signup", SignupApiController.create);
		});
	}
}
