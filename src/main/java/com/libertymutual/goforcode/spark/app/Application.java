package com.libertymutual.goforcode.spark.app;

import com.libertymutual.goforcode.spark.app.controllers.ApartmentApiController;
import com.libertymutual.goforcode.spark.app.controllers.ApartmentController;
import com.libertymutual.goforcode.spark.app.controllers.HomeController;
import com.libertymutual.goforcode.spark.app.controllers.SessionController;
import com.libertymutual.goforcode.spark.app.controllers.SignupApiController;
import com.libertymutual.goforcode.spark.app.controllers.SignupController;
import com.libertymutual.goforcode.spark.app.filters.SecurityFilters;
import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.ApartmentsUsers;
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
			ApartmentsUsers.deleteAll();
			
//			User trace = new User("trace@gmail.com", password, "Trace", "Ferre");
//			trace.saveIt();
//			
//			Apartment apartA = new Apartment(3000, 1, 0, 350, "123 Main St", "San Francisco", "CA", "94111");
//			apartA.setIsActive(true);
//			Apartment apartB = new Apartment(1400, 3, 2.5, 1400, "500 My St", "Seattle", "WA", "98036");
//			
//			apartA.saveIt();
//			apartB.saveIt();
//			
//			trace.add(apartA);
//			trace.add(apartB);			
		}
		
		before("/*", SecurityFilters.isNew);
		before("/*", SecurityFilters.isPostSameToken);
		
		path("/apartments", () -> {
			before("/new", SecurityFilters.isAuthenticated);
			get("/new", ApartmentController.newForm);
			
			before("/mine", SecurityFilters.isAuthenticated);
			get("/mine", ApartmentController.index);
			
			before("/:id/activations", SecurityFilters.isOwner);
			before("/:id/activations", SecurityFilters.isAuthenticated);
			post("/:id/activations", ApartmentController.updateToActive);
			
			before("/:id/deactivations", SecurityFilters.isOwner);
			before("/:id/deactivations", SecurityFilters.isAuthenticated);
			post("/:id/deactivations", ApartmentController.updateToInactive);
			
			before("/:id/like", SecurityFilters.isAuthenticated);
			post("/:id/like", ApartmentController.updateLiked);
			
			get("/:id", ApartmentController.details);	
			
			before("", SecurityFilters.isAuthenticated);
			post("", ApartmentController.create);
		});
		
		get("/", HomeController.index);
		get("/login", SessionController.newForm);
		post("/login", SessionController.create);
		post("/logout", SessionController.destroy);
		
		get("/users/new", SignupController.newForm);
		post("/users", SignupController.create);
		
		path("/api", () -> {
			get("/apartments/:id", ApartmentApiController.details);
			post("/apartments", ApartmentApiController.create);
			post("/signup", SignupApiController.create);
		});
	}
}
