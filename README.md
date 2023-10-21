## Features

- **Authentication:**
- **Payments:** 
- **Creating Accounts with Foreign Currency:**
- **Transfers:**
- **Exchange by Current Rate:**

## Installation

1. Clone this repository to your local machine.

   ```shell
   git clone https://github.com/FuuKowatty/bank_app .
   ```

2. Database configuring
    ```shell
    docker pull mysql
    ```
   
    ```shell
    docker run --name db -e MYSQL_DATABASE=jpa -e MYSQL_ROOT_PASSWORD=root -d -p 3306:3306 mysql
    ```
   
## Usage
   ```shell
    .\mvnw spring-boot:run
   ```  

## Contact
For support, feedback, or inquiries, please contact us at [bartoszmech0@gmail.com].

