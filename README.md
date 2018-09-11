# WebSite Checker

## Program Description: 
	'Website Checker' is a simple webpage that checks availability of a certain webpage!
		if site's response code is equal 'OK' it takes a screenshot.

## Program Details and Requirements
	-Requires java 8.
	-Requires MYSQL(mariadb) for storing user and website information.
	-ScreenShot is based on chromedriver so it requires chromeium/chrome browser.
	-Linux(ubuntu) only.
	
![Simple Example1](https://i.imgur.com/ySi2jiA.png)
	
## What can this program do:
	- Auto setup with admin account creation.
	- Header with added websites status (Up/Down/Total) and MYSQL connection status.
	- Basic login/signup.	
	![Simple Example1](https://i.imgur.com/FTUHw1P.png)
	- Server search.
	- Adding a website to check
		- Auto protocol checking
		- Auto 'www.' checking/adding.
		- Checking for invalid website url as you type.
	- Displaying checked servers
		- with the checked date.
		- with statistics of it's availablity.
		- with delete button if you are the adder(admins can delete servers regardless of it's adder)

### Note :
	Developed with Vaadin 8.
	
