# ProductManagementSystem

This is the Product Management System that allows User creation and user can create Products.

User Journey is describe as follows.
- Register
- Login
- Add Products
- Logout

CRUD operaions allowed for 
- User
- Product

User CRUD operations:
- User Registration (Create user)
- User Login
- Update User info
- Delete User

 Product CRUD operations':
 - Product creation
 - Product updation
 - Get products
 - Delete products

Basic service class test cases written using Junit and Mockito.  

Note: 
- After Login, session of that user is mantained untill that user perform logout operation. 
- Products are always associated under particular user.
- User login is must for product creation/ updation/ deletion/ retrieving products data.
