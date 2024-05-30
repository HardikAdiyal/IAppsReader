- This repository contains CRUD on E-Paper info table into PostgresSQL database.
- This Consist of 2 APIs of Uploading XML file for saving paper information into the table and a searching API for fetching data from the table, Also can perform filtering on seach API.

## Requirement

- Java 17
- STS
- Postman
- Docker Desktop
- PostgresSQL

## API Endpoints
- POST API: http://localhost:8080/api/epaper/upload
    - In form data pass parameter "file" as multipart type
    - On backend it will validate requested XML file, Process it and store it in the table.

- GET API: http://localhost:8080/api/epaper/search
    - parameters(All parameters are optional.):
        - search: filter data based on search string from fileName or Paper name stored in table
      - sortOn: Pass any field from result to perform sorting(Default sort on "Id"). All data will be order by descending by default.
       - fromDate: Filter by any date(Date format: yyyy-mm-dd).
       - toDate: Filter by any date(Date format: yyyy-mm-dd)
       - page: Page number, If want to jump to any specific page. Default page is 0.
       - pageSize: If want to resize page elements. Default size is 10.



### Steps to Run/Deploy 
1. Navigate to the project directory and go inside of it
2. Run below command to run the application and PostgresSQL(Please make sure Port: 8080 and 5432 are not running in the system)
	- 	docker-compose up
3. Application server will run on Port 8080 and DB server is on 5432