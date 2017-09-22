package com.libertymutual.goforcode.spark.app.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.models.ApartmentsUsers;
import com.libertymutual.goforcode.spark.app.models.User;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class ApartmentController {
	
	public static final Route details = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = Apartment.findById(Integer.parseInt((req.params("id"))));
			User user = req.session().attribute("currentUser");
			List<String> userNames = new ArrayList<String>();
			List<User> users = apartment.getAll(User.class);
			
			boolean owner = false;
			boolean active = false;
			boolean inactive = false;
			boolean unliked = false;
			
			//executes if user is owner of listing
			if (user != null && user.getId().equals(apartment.getUserId())) {
				owner = true;
				unliked = false;
				if(apartment.getIsActive() == true) active = true;
				else inactive = true;
				for (User oneUser : users) {
					userNames.add(oneUser.getFirstName() + " " + oneUser.getLastName());
				}										
			}
			//executes if user is logged in and not owner of listing
			else if (user != null) {
				unliked = true;
				for (User oneUser : users) {
					if (oneUser.getId().equals(user.getId())) unliked = false;
				}				
			}
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("apartment", apartment);
			model.put("owner", owner);
			model.put("active", active);
			model.put("inactive", inactive);
			model.put("unliked", unliked);
			model.put("userNames", userNames);
			model.put("currentUser", req.session().attribute("currentUser"));
			model.put("noUser", req.session().attribute("currentUser") == null);
			model.put("csrf", req.session().attribute("csrf"));
			return MustacheRenderer.getInstance().render("apartments/details.html", model);
		}		
	};
	
	public static final Route newForm = (Request req, Response res) -> {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("currentUser", req.session().attribute("currentUser"));
		model.put("noUser", req.session().attribute("currentUser") == null);
		model.put("csrf", req.session().attribute("csrf"));
		return MustacheRenderer.getInstance().render("apartments/newForm.html", model);
	};

	public static final Route create = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = new Apartment();
			apartment.setRent(Integer.parseInt(req.queryParams("rent")));
			apartment.setNumberOfBedrooms(Integer.parseInt(req.queryParams("number_of_bedrooms")));
			apartment.setNumberOfBathrooms(Double.parseDouble(req.queryParams("number_of_bathrooms")));
			apartment.setSquareFootage(Integer.parseInt(req.queryParams("square_footage")));
			apartment.setStreet(req.queryParams("street"));
			apartment.setCity(req.queryParams("city"));
			apartment.setState(req.queryParams("state"));
			apartment.setZipCode(req.queryParams("zip_code"));
			apartment.setIsActive(true);
			apartment.saveIt();
			
			User user = req.session().attribute("currentUser");
			user.add(apartment);
			user.saveIt();
			
			res.redirect("/");
			return "";
		}
	};

	public static final Route index = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			User user = req.session().attribute("currentUser");
			Long id = (Long) user.getId();
			
			List<Apartment> activeApartments = Apartment.where("user_id = ? and is_active = ?", id, true);
			List<Apartment> inactiveApartments = Apartment.where("user_id = ? and is_active = ?", id, false);
			
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("active", activeApartments);
			model.put("inactive", inactiveApartments);
			model.put("name", user.getFirstName());
			model.put("currentUser", req.session().attribute("currentUser"));
			model.put("noUser", req.session().attribute("currentUser") == null);
			model.put("csrf", req.session().attribute("csrf"));
			return MustacheRenderer.getInstance().render("apartments/index.html", model);
		}
	};

	public static final Route updateToActive = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = Apartment.findById(Integer.parseInt((req.params("id"))));
			apartment.setIsActive(true);
			apartment.saveIt();
			
			res.redirect("/apartments/" + apartment.getId());
			return "";
		}
	};

	public static final Route updateToInactive = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = Apartment.findById(Integer.parseInt((req.params("id"))));
			apartment.setIsActive(false);
			apartment.saveIt();
			
			res.redirect("/apartments/" + apartment.getId());
			return "";
		}
	};

	public static final Route updateLiked = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = Apartment.findById(Integer.parseInt((req.params("id"))));
			User user = req.session().attribute("currentUser");
			ApartmentsUsers au = new ApartmentsUsers(apartment.getId(), user.getId());
			au.saveIt();
			
			res.redirect("/apartments/" + req.params("id"));
			return "";
		}
	};
}
