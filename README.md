# 🌟 **E-Commerce Microservices Platform - Backend Repository** 🌟

Welcome to the **backend repository** of our **E-Commerce Microservices Platform**! 🚀
This system provides a **robust, scalable, and modular e-commerce experience**, including shopping, inventory management, wallet transactions, service discovery, and observability. Below is a breakdown of the key features and microservices powering this platform.

---

## 🎉 **Key Features Overview** 🎉

### 🔐 **Authentication & Security**

The platform offers a **secure and flexible authentication system**:

* **JWT-based authentication** for all services 🔑
* **User registration and login**
* **Password reset** via email OTP 🔄
* Secure routing and access management through **API Gateway**
* **Centralized authentication handling** via API Gateway filters

---

### 🛒 **Shop Service**

Manage products and orders efficiently:

* **Order management** with multiple items per order
* **Caching** with Caffeine for faster product retrieval
* **Integration with Inventory and Wallet services** via OpenFeign
* **JWT-protected endpoints** for security
* **Database:** `shopdb` (MySQL)

---

### 📦 **Inventory Service**

Track stock levels and ensure availability:

* **Product stock management** (add, update, remove)
* **Integration with Shop Service** for real-time stock updates
* **Secure endpoints** using JWT
* **Database:** `inventorydb` (MySQL)

---

### 💰 **Wallet Service**

Handle user wallets and transactions:

* **Deposit, withdraw, and transfer funds**
* **Transaction history tracking**
* **JWT-secured APIs**
* **Integrated with Shop Service for payment processing**
* **Database:** `walletdb` (MySQL)

---

### 🌐 **API Gateway**

The single entry point for all requests:

* Routes incoming requests to the appropriate microservice
* Handles **JWT verification** for protected routes
* Provides **load balancing**, **centralized logging**, and **observability**
* Built with **Spring Cloud Gateway** and **WebFlux**

---

### 🏷️ **Service Discovery & Config Management**

* **Eureka Server** for service discovery (`namingServer`) 🕵️‍♂️
* **Config Server** centralizes configuration (`configServer`) ⚙️
* Services automatically fetch properties from **Config Server**
* Dynamic service registration and discovery for **scalability**
* Health checks for all microservices
* Proper Docker networking for inter-service communication

---

### 📊 **Observability & Logging**

* **Grafana** for dashboards
* **Prometheus** for metrics collection
* **Loki** for centralized logging (read/write backends)
* **Tempo** for distributed tracing
* **Nginx gateway** for Loki endpoints

---

### 🗄️ **Database Management**

* Each microservice uses its **own MySQL database**
* Databases: `shopdb`, `inventorydb`, `walletdb`
* **JPA/Hibernate** auto-generates tables (DDL auto-update)
* MySQL connector included for runtime

---

### 🛠️ **Developer-Friendly Features**

* **Spring Boot Actuator** enabled for monitoring
* **OpenFeign** for inter-service communication
* **Caffeine caching** for fast reads
* Observability enabled via **OpenTelemetry Java Agent**
* Fully **Dockerized microservices** with healthchecks

---

## 🌟 **Thank you for exploring my E-Commerce Microservices Platform!**
