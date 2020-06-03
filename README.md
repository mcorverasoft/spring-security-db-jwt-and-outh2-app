## Building a app with robust security connecting to Database (MySQL in this case) or Oauth2 providers and creating a JSON web token on Spring Security, REST Service and JSON WebToken

This application contains an implementation of Auditing and an implementation of a user profile that requires Spring Security

## Please follow the next steps:

1. **Clone the application**

	```bash
	git clone https://github.com/mcorverasoft/spring-security-db-jwt-and-outh2-app.git
	cd spring-security-db-jwt-app
	```

2. **Create a MySQL database**

	```bash
	create database socialappdb
	```

3. **Change MySQL username and password as per your MySQL installation**

	+ open `src/main/resources/application.properties` file.

	+ change `spring.datasource.username` and `spring.datasource.password` properties as per your mysql installation

4. **Create an app on Facebook and Google in order to authenticate others user with this providers:**

	+ open `src/main/resources/application.properties` file.
	
	+ create an app from facebook in https://developers.facebook.com/
	
	+ create an app from Google in https://console.cloud.google.com/
	
	+ change `clientId` and `clientSecret`
	
5. **Run the file SpringSecurityDbJWTAppAplicationTest as J Unit (4)**
	
	Run a test unit for create users or roles, please edit the test case
	
6. **Run the app**

	You can run the spring boot app by typing the following command -

	```bash
	mvn spring-boot:run
	```
	
	The server will start on port 8080 (please go the aplication.properties file for edit it).
	
7. **Run postman to get a JWT**
	
	Plase create a new Request, method POST for login
	url http://localhost:8080/app/login
	
	on Body Tab send a type raw JSON Application, by example:
	```bash
	{
	 "usernameOrEmail":"youremail@gmail.com",
	 "password": "yourpassword"
	}
	```
    The post call return a JSON object with the token type jsonwebtoken.
    
 8. **Call other Rest Services**
 	Call other Rest Services as http://localhost/list-user (type GET)
 	sending a Header "Authorization" with the value Bearer<yourtoken>
 	The app will verify if your token is valid in order to access to http8080://localhost/list-user 
 	if your token is valid, the call will be successful, else the app return a 403 Error.
 	
 9. **If you want create a new user**
 	create a new Request Post on postman, with url http://localhost:8080/app/signup
 	on Body Tab send a type raw JSON Application, by example:
	```bash
	{
	 "email":"youremail@gmail.com",
	 "username":"yourusername",
	 "name": "yourname",
	 "password": "yourpassword"
	}
	```
	 The post call return a JSON object with the token type jsonwebtoken and previously will create a new user.

10. **Repeat step 6 and 7 with the new credentials**
	Your App already has robust security.
	
	
11. **Download or clone the client (Angular Project)**

	```bash
		https://github.com/mcorverasoft/spring-security-db-jwt-and-outh2-app.git
		
		please follow indications in the README file
	```