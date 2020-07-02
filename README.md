# Bank Account Service

## Endpoints ##
 
  -----------
  *Create user with two accounts (PLN&USD)*
   ----
     
  * **Endpoint**
  
    /api/user
  
  * **Method:**
  
    `POST`
    
  *  **Path Params**
  
        None
  
  * **Body**
  
 
       {
       "fullName": "Edith Piaf",
       "initialBalance": 100,
       "personalIdNumber": "90010101001"
       }
   
  * **Success Response:**
  
    * **Code:** 201 <br />
    
   * **Example call:**
   
        ```
       curl --location --request POST 'http://localhost:8080/api/user' \
       --header 'Content-Type: application/json' \
       --data-raw '{"fullName": "Edith Piaf",
       "initialBalance": 100,
       "personalIdNumber": "90010101001"
       }'
    
 
 -----------
 *Get user info for personal id number*
  ----
 
  * **Endpoint**
  
    /api/user/{personalIdNumber}
  
  * **Method:**
  
    `GET`
    
  *  **Path Params**
  
        personalIdNumber - 11-digit number, must reached legal age
  
  * **Success Response:**

    * **Code:** 200 <br />
    
    
  * **Example call:**
       
       ```
           curl --location --request GET 'http://localhost:8080/api/user/90010101001'
 
   **Content:** 
   
      {
          "user": {
              "id": "31ff4eb9-a44e-466a-b284-ff08f8c07573",
              "personalIdNumber": "90010101001",
              "fullName": "Edith Piaf"
          },
          "accountInfo": [
              {
                  "currency": "PLN",
                  "balance": 99.6
              },
              {
                  "currency": "USD",
                  "balance": 0.0
              }
          ]
      }
      
      
   -----------
   *Exchange currencies*
   ----
* **Endpoint**
    
      /api/user/{personalIdNumber}/exchange
    
    * **Method:**
    
      `POST`
      
    *  **Path Params**
    
          personalIdNumber
    
    * **Body**
    
   
         {
         "currencyFrom": "usd",
         "currencyTo": "pln",
         "amount": 4.98
         }
     
   * **Success Response:**
    
      * **Code:** 200 <br />
      
   * **Example call:**
     
          ```
         curl --location --request POST 'http://localhost:8080/api/user/90010101001/exchange' \
         --header 'Content-Type: application/json' \
         --data-raw '{"currencyFrom": "usd",
         "currencyTo": "pln",
         "amount": 4.98}'
      
   
