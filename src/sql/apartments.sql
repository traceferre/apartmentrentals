DROP TABLE IF EXISTS apartments;

CREATE TABLE apartments (
	  id BIGSERIAL PRIMARY KEY NOT NULL,
	  rent INTEGER NOT NULL,
	  number_of_bedrooms INTEGER NOT NULL,
	  number_of_bathrooms NUMERIC(4, 2) NOT NULL,
	  square_footage INTEGER NOT NULL,
	  street VARCHAR(255) NOT NULL,
	  city VARCHAR(255) NOT NULL,
	  state VARCHAR(255) NOT NULL,
	  zip_code VARCHAR(30) NOT NULL,
	  user_id BIGINT,
	  is_active BOOLEAN NOT NULL default false
);
