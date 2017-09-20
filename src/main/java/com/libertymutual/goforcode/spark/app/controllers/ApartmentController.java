package com.libertymutual.goforcode.spark.app.controllers;

import java.util.HashMap;
import java.util.Map;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;
import com.libertymutual.goforcode.spark.app.utilities.MustacheRenderer;

import spark.Request;
import spark.Response;
import spark.Route;

public class ApartmentController {
	
	public static final Route details = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = Apartment.findById(Integer.parseInt((req.params("id"))));
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("apartment", apartment);
			return MustacheRenderer.getInstance().render("apartments/details.html", model);
		}		
	};
	
	public static final Route newForm = (Request req, Response res) -> {
		return MustacheRenderer.getInstance().render("apartments/newForm.html", null);
	};

	public static final Route create = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = new Apartment();
			apartment.setRent(Integer.parseInt(req.queryParams("rent")));
			apartment.setNumberOfBedrooms(Integer.parseInt(req.queryParams("numberOfBedrooms")));
			apartment.setNumberOfBathrooms(Double.parseDouble(req.queryParams("numberOfBathrooms")));
			apartment.setSquareFootage(Integer.parseInt(req.queryParams("squareFootage")));
			apartment.setStreet(req.queryParams("street"));
			apartment.setCity(req.queryParams("city"));
			apartment.setState(req.queryParams("state"));
			apartment.setZip(req.queryParams("zip"));
			apartment.saveIt();
			
			res.redirect("/");
			return "";
		}
	};
}
