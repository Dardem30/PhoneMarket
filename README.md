Simple Spring Boot REST server with connection to MySQL database which implements 2 simple requests<br>
To start the app `docker-compose up -d`
####POST /calculate-price<br>
Content-Type:application/json<br>
Request body:
```json
{
  "product": 1,
  "taxNumber": "DE123456789",
  "couponCode": "P10"
}
```
Example response body for code 200:
```json
{"success": true, "message": null, "result": 107.100000}
```
Example response body for code 400:
```json
{"success":false,"message":"Wrong taxNumber [IT123]","result":null}
```
####POST /purchase<br>
Content-Type:application/json<br>
Request body:
```json
{
  "product": 1,
  "taxNumber": "IT12345678900",
  "couponCode": "D15",
  "paymentProcessor": "paypal"
}
```
Example response body for code 200:
```json
{"success":true,"message":null,"result":null}
```
Example response body for code 400:
```json
{"success":false,"message":"Price can't be more than 100000","result":null}
```
<br>
The code is pretty straightforward except handling exceptions which is implemented in GlobalExceptionHandler.java<br>
All request params are validated by spring-boot-validation tools and taxNumber format is validated by regex expression
which is stored in a table [countries] as [validation_regex] column