package com.libertymutual.goforcode.spark.app.controllers;

import com.libertymutual.goforcode.spark.app.models.Apartment;
import com.libertymutual.goforcode.spark.app.utilities.AutoCloseableDb;

import spark.Request;
import spark.Response;
import spark.Route;
import static spark.Spark.*;

import org.javalite.common.JsonHelper;


public class ApartmentApiController {

	public static final Route details = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			String idAsString = req.params("id");
			int id = Integer.parseInt(idAsString);
			Apartment apartment = Apartment.findById(id);
			
			if (apartment != null) {
				res.header("Content-Type", "application/json");
				return apartment.toJson(true);
			}
			notFound("Could not find apartment...");
			return "";
		}
	};
	
	public static final Route create = (Request req, Response res) -> {
		try (AutoCloseableDb db = new AutoCloseableDb()) {
			Apartment apartment = new Apartment();
			apartment.fromMap(JsonHelper.toMap(req.body()));
			apartment.saveIt();
			
			res.status(201);
			return apartment.toJson(true);
		}		
	};
	
	

}
