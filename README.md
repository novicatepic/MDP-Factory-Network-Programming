# Network and Distributed Programming Course Project

Create a system that simulates a simplified confectionery factory's operations. The factory utilizes a GUI application with options for working with users (view, delete, block), reviewing and confirming or rejecting orders sent by clients, and managing products (CRUD = create, read, update, delete). In addition to the factory application, there are GUI applications for customers and raw material distributors. The following processes and applications are explained in the system:

The customer application is delivered to various customers/business partners of the factory. Upon launching the application, a login form appears where users can enter their username and password. Additionally, there is an option to open a registration form. During registration, the customer's company name, address, contact phone, username, and password (entered twice for verification) are provided. Registration and login to the system are performed through appropriate RESTful endpoints of the factory. After a customer sends a registration request, the account will not be automatically activated; an operator at the factory must confirm the registration request. Once the user has an active account, they can log in to the system, view all products (in a tabular format), and create orders. Products are obtained from a REST service. When creating an order, the customer selects products (which can be multiple), and the created order is sent to the factory's MQ in XML format. Determine the XML document format for orders independently and create the corresponding XML schema. If an order is not valid, it cannot be processed at the factory.

Working with users in the factory application enables a tabular display of all client accounts, with options to approve or reject requests, delete and block. User accounts are stored in a file named users.json on the server. The factory application provides CRUD options for working with products, which are stored in a Redis database. Writing code to automatically add test data when the application is launched is allowed. Users can write a promotional message that is multicast to all clients. Client applications display this message somewhere on their interface.

In addition to the main application, the factory has an application for viewing all orders. Operators log into this application by entering their names. User accounts are stored in a file named factory_users.json, and login is performed using Secure Sockets. After logging in, operators have the option to retrieve and process orders. Orders are retrieved based on the time sent by the client from the MQ (starting with the oldest). After an operator reviews an order, they can either reject or approve it. An email (with the email address from the order) is automatically sent to the client to inform them of the order status. Following this, the user can retrieve the next order. Multiple operators can work simultaneously at the factory. After completing the order processing, information about the status is sent via Secure Sockets and stored in TXT format on the factory's server.

The main GUI application for the factory includes an option to order raw materials from distributors. Distributors are connected to the factory using RMI. Each distributor has their own application where they enter the company name and generate their products. A factory worker can view a list of all clients and the products of a selected client that can be purchased by choosing the appropriate option and entering the quantity. All communication mentioned is done using RMI, and it's necessary to independently determine how the factory can know about all raw material distributors and connect with them.

Consider that the system can have only one running factory application, multiple customer applications, and multiple applications for raw material distributors. Any aspects not precisely defined in the text should be implemented in an arbitrary manner. Use loggers and properties files.


![MDP-1git](https://github.com/novicatepic/MDP-Factory-Network-Programming/assets/62095336/7ad64086-1282-4815-808c-6d18c09813e5)

![MDP-2git](https://github.com/novicatepic/MDP-Factory-Network-Programming/assets/62095336/eb00c542-1d9d-4163-925b-f81d49c2c2cc)

![MDP-3git](https://github.com/novicatepic/MDP-Factory-Network-Programming/assets/62095336/e56eacef-d77c-4d04-8c31-62ffa9e069a4)

![MDP-4git](https://github.com/novicatepic/MDP-Factory-Network-Programming/assets/62095336/361cfd90-7dce-41e5-a6c8-23c4f20bd8e8)

![MDP-5git](https://github.com/novicatepic/MDP-Factory-Network-Programming/assets/62095336/e6cd05b5-d464-4de1-b453-7ade4b2b7d37)

![MDP-6git](https://github.com/novicatepic/MDP-Factory-Network-Programming/assets/62095336/f634cc83-6d74-4490-bfb7-e2dff077c719)
