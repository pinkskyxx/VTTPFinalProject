# **SHANGE**

SHANGE is a comprehensive event management web application that enables users to search, filter, and interact with events based on various criteria such as location, type, and mode. It is built using modern web technologies and follows best practices to ensure a seamless and secure user experience.

## **Table of Contents**
- [Project Overview](#project-overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Installation and Setup](#installation-and-setup)
- [Usage](#usage)
- [Deployment](#deployment)
- [Known Issues](#known-issues)

## **Project Overview**

SHANGE provides users with a platform to discover events that match their interests and preferences. The application allows for robust searching and filtering of events, helping users to find exactly what they’re looking for with ease. The backend is built with Spring Boot, while the frontend is developed using Angular, resulting in a full-stack solution that is both powerful and user-friendly.

## **Features**

- **Search and Filter Events**: Users can search for events using a search bar and filter them by location (North, South, East, West, Central), type (One-time Event, Recurring Event), and mode (Physical, Online).
- **Responsive Design**: The application is designed to be fully responsive, ensuring a smooth experience across all devices.
- **Secure API Integration**: The backend APIs are securely integrated with the frontend, with proper handling of CORS and security configurations.
- **Dockerized Deployment**: The application is containerized using Docker, allowing for easy deployment and scalability.

## **Technology Stack**

### **Frontend**
- **Framework**: Angular
- **Styling**: Bootstrap
- **Build Tool**: Node.js
- **Deployment**: Railway

### **Backend**
- **Framework**: Spring Boot
- **Language**: Java
- **Build Tool**: Maven
- **Database**: Configured for MongoDB and MySQL
- **Deployment**: Docker, Railway

## **Installation and Setup**

### **Prerequisites**
- **Node.js**: Ensure that Node.js is installed.
- **Maven**: Maven should be installed for building the backend.
- **Docker**: Docker is required for containerization and deployment.

### **Clone the Repository**
```bash
git clone https://github.com/yourusername/shange.git
cd shange
```

### **Build and Run**

1. **Frontend**
   - Navigate to the frontend directory and install dependencies:
     ```bash
     cd ShangeFront
     npm ci --legacy-peer-deps
     ```
   - Build the frontend:
     ```bash
     ng build
     ```

2. **Backend**
   - Navigate to the backend directory and build the application:
     ```bash
     cd ../ShangeBackEnd
     ./mvnw package -Dmaven.test.skip=true
     ```

3. **Docker**
   - Build and run the Docker container:
     ```bash
     docker build -t shange .
     docker run -p 8080:8080 shange
     ```

### **Environment Variables**
Configure the following environment variables in your `.env` file:
- `SPRING_DATASOOURCE_URL=jdbc:mysql://<your-mysql-url>`
- `MONGODB_URL=mongodb://<your-mongodb-url>`

## **Usage**

Once the application is running, navigate to `http://localhost:8080` in your browser. From there, you can search for events, filter them by various criteria, and interact with the results.

## **Deployment**

SHANGE is designed to be easily deployable using Docker. The multi-stage Dockerfile ensures that both the frontend and backend are built and deployed efficiently. The application is hosted on [Railway](https://railway.app/), providing seamless integration and management of the deployed services.

## **Known Issues**

- **CORS Issues**: There may be issues related to CORS configuration, especially when deploying to production environments. Ensure that the backend is properly configured to handle cross-origin requests.
- **Mixed Content**: Ensure that all resources are loaded over HTTPS to avoid mixed content errors.

