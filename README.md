# BankApp - Backend
This project is a console application created in spring-boot which allows user to authenticate, create account and make transfers between accounts.



## Technologies

* Code: Java 21, Spring Boot 3, MySQL, OKHttp
* Other: Docker, Maven

## Solved Problems
During the development of this project I had to face of a bunch of problems. These are a few of them.

* Use external API to download currencies exchange
* Create foreign account
* Make transfers between users by actual currency
* Authentication
* Organize code with conventional commits

## Pre requirements
1. Ensure that you have installed java on your computer by
```shell
java --version
```
2. Ensure that you have installed docker on your computer by
```shell
java --version
```
3. Open docker service or docker-desktop
```shell
sudo service docker start 
```


## How to build the project on your own
1. Clone this repository
```shell
git clone https://github.com/FuuKowatty/bank_app.git
```
2. Go to the folder with cloned repository
3. Run the application
```shell
.\mvnw spring-boot:run
```