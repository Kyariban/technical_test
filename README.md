# Technical test

### Local build

Open a terminal at the root of the project then type the following commands :  
`mvn clean install`

Once maven has finished you can use the next command to start the application :   
`mvn spring-boot:run`

Or you can of course always use your IDE to run the app.

The application will start on port 8080 you can then start calling it using the postman collection available within this project.

The application runs an internal h2 database, to access the admin console you can use the following link :  
http://localhost:8080/h2-console

Use the following parameters to access it :   
```
JDBC url :  jdbc:h2:mem:testdb  
Username : admin  
password: tests 
```
The other parameters are left as default.

### API Documentation

Since this application is only for test purposes there is not many requirements to query the API.  
Here's the list of the available endpoint and how to interact with them.

- `GET /user/{username}`   
    Simply pass the username of the user you want to know the details of in the url.

- `POST /user`  
    Creates a new user if the given data is valid, errors are thrown otherwise.
    
```json
{
    "username": "[String | Mandatory]",
     "birthDate": "[yyyy-MM-dd | Mandatory]",
     "countryOfResidence": "[String | Mandatory]",
     "phoneNumber": "[Numbers only | Optional]",
     "gender": "[MALE / FEMALE | Optional]"
}
  ```

It's important to note that a user can only be created if he is over 18 years old and living in France.

The given postman collection only displays working cases, but it's still a good entry point to test the different possible behaviours.