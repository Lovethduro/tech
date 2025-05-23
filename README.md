Art Gallery Web Application
Project Description
The Art Gallery Web Application is a Spring Boot-based platform for managing, purchasing and displaying artworks. 
It allows administrators to add, edit, delete, and update stock for artworks, with a user-friendly 
interface for viewing and sorting artworks. The application uses MongoDB for data storage and is deployed on Heroku. Key features include:

Admin Dashboard: Manage artworks, orders, and stock.
Artwork Management: Add, edit, delete artworks, and update stock levels.
Responsive UI: Toggle between grid and list views, sort artworks, and confirm deletions via a modal.
Security: Role-based access (ADMIN role required for /admin/** endpoints).
Image Support: Display artwork images via /api/product-images/{id}.

Technologies
Backend: Spring Boot, Spring Security, Spring Data MongoDB
Frontend: Thymeleaf, Bootstrap, JavaScript
Database: MongoDB (MongoDB Atlas)
Deployment: Heroku
Build Tool: Maven

Prerequisites
Java: JDK 17 or later
Maven: 3.8.0 or later
MongoDB: MongoDB Atlas account or local MongoDB instance
Heroku CLI: For deployment
Git: For version control

Setup Instructions
Clone the Repository
git clone https://github.com/your-username/art-gallery.git
cd art-gallery


Configure MongoDB
Create a MongoDB Atlas cluster or use a local MongoDB instance.
Update src/main/resources/application.properties:spring.data.mongodb.uri=mongodb+srv://clients:Rufus1122@cluster0.sfffjuu.mongodb.net/webtech?retryWrites=true&w=majority
spring.data.mongodb.database=webtech

Replace the URI with your MongoDB connection string.
Install Dependencies
mvn clean install

Run the Application Locally
mvn spring-boot:run
Access the application at http://localhost:8080.

Admin Access
Log in with an admin account (configure users via UserService or a default admin user).
Navigate to http://localhost:8080/admin to access the dashboard.


Deploy to Heroku
Install Heroku CLI: Heroku CLI.
Log in to Heroku:heroku login
Create a Heroku app:heroku create art-gallery-app
Set MongoDB URI:heroku config:set SPRING_DATA_MONGODB_URI="mongodb+srv://clients:Rufus1122@cluster0.sfffjuu.mongodb.net/webtech?retryWrites=true&w=majority" --app art-gallery-app
Deploy:git push heroku main
Open the app:heroku open --app art-gallery-app

Key Endpoints

GET /admin/artworks: View all artworks (requires ADMIN role).
GET /admin/products/add: Form to add a new artwork.
POST /admin/products/save: Save a new or edited artwork.
POST /admin/products/{id}/stock: Update stock for an artwork.
POST /admin/products/delete/{id}: Delete an artwork.
GET /api/product-images/{id}: Retrieve artwork images.
Running the Application

Local: After setup, run mvn spring-boot:run and access http://localhost:8080.
Heroku: Deployed app is available at https://art-gallery-app.herokuapp.com (replace with your Heroku app URL).
Admin Features:
Add artwork via /admin/products/add.
Edit or delete artworks via /admin/artworks.
Update stock via the stock form on the artworks page.
Use the grid/list view toggle and sort dropdown for better navigation.

Notes
Images: Configure ProductImageService to handle image storage/retrieval.
MongoDB: Secure your MongoDB credentials and restrict network access in MongoDB Atlas.

Link
https://artgalleryy-da65337bdd76.herokuapp.com/landing
