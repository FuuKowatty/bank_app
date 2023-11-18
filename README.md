# BankApp - Backend
This project is a console application created in spring-boot which allows user to authenticate, create account and make transfers between accounts.

![APP-GIF](https://media.giphy.com/media/v1.Y2lkPTc5MGI3NjExOW9oMTQ3ZWRqOHFpaDg2NWN4Y2gzamRzYmptNHZhbTh1MngwcGdmbCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/DdSKXHQdc0PMfwpd6N/source.gif)

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
docker --version
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
3. Build docker image
```shell
docker build -t bank-app .
```
4. Open database
```shell
docker run -d -p 3306:3306 bank-app 
```
5. Run the application
```shell
.\mvnw spring-boot:run
```
