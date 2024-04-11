### Configuration

The splitty.properties file under the client folder is responsible for the configuration of the client side of the application. 
The following are the properties that can be configured:

(The client.language property can be changed through the UI so there is no need to change it through the config file.)

**How to add another language:**
This can be done in through the drop-down UI that displays the current language.
Language should be in format: <languageCode>_<countryCode>. (E.g. en_GB)
languageCode must be a ISO 639 alpha-2 language code.
countryCode must be ISO 3166 alpha-2 country code.

**How to change the default server**

* connection.URL={your server URL e.g. http://localhost:8080}

**How to configure your email credentials:**

 * spring.mail.host = {The host of your email server e.g. smtp.gmail.com}

 * spring.mail.password = {your password, for gmail an app password can be used that is configured in your google account under "app password"}

 * spring.mail.port = {The port of your email server e.g. 587 for gmail}
 * spring.mail.username= {add your email here e.g. myemail@gmail.com}

_**These default values work for gmail:**_

 * spring.mail.properties.mail.smtp.auth=true (use basic authentication)

 * spring.mail.properties.mail.smtp.starttls.enable=true (use TLS)

### **Implemented Shortcuts** 

**Main Menu:**
* Ctrl + A: Goes to admin overview password
* Ctrl + E: Joins the most recently joined event from the history

**Event Overview:**
* Ctrl + T: Test email invite 
* Ctrl + S: Switch to statistics screen 
* Ctrl + E: Edit the title of the event 
* Ctrl + Q: Add a new expense 
* Ctrl + P: Add a new participant 
* Ctrl + I: Invite by email 
* Ctrl + B: Go to main screen 
* Ctrl + F: Add tag 
* Ctrl + D: Transfer movie 
* Ctrl + G: Settle debts 
