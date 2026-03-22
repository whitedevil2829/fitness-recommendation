# AI-Based Fitness Recommendation System (Gemini API Powered)

An AI-powered fitness recommendation system built using **Spring Boot Microservices Architecture**.  
This system analyzes user activities such as walking, running, and other exercises along with parameters like **duration** and **intensity**, and generates **personalized fitness recommendations using Google Gemini API**.

---

##  Features

- 🧑 User Management Service  
  - Handles user registration, authentication, and profile management  

- 🏃 Activity Tracking Service  
  - Captures activity data (walking, running, duration, intensity)  

- 🤖 AI Recommendation Service (Gemini API)  
  - Integrates with **Google Gemini API**  
  - Generates intelligent, context-aware fitness suggestions  
  - No need for custom ML model training  

- 🔍 Eureka Server (Service Discovery)  
  - Enables dynamic communication between microservices  

- ⚙️ Config Server (Centralized Configuration)  
  - Manages configuration across all services  

---

## 🏗️ Microservices Architecture

This project is designed using **Spring Boot + Spring Cloud**:

- Independent, loosely coupled services  
- REST API-based communication  
- Scalable and maintainable design  

### Services Included:
- User Service  
- Activity Service  
- AI Recommendation Service  
- Eureka Server  
- Config Server
- RabbitMQ

---

## Workflow

1. User submits activity details (type, duration, intensity)  
2. Activity Service processes and stores the data  
3. AI Service sends structured prompt to **Gemini API**  
4. Gemini generates personalized fitness recommendations  
5. Response is returned to the user via API  

---

## 🛠️ Tech Stack

- **Backend:** Spring Boot, Spring Cloud  
- **Microservices:** Eureka Server, Config Server  
- **AI Integration:** Google Gemini API  
- **Database:** (PostgreSQL & MongoDB)  
- **Communication:** REST APIs  

---

## 🔐 Environment Variables

To run this project, you need to configure your Gemini API key:

```bash
GEMINI_API_KEY=your_api_key_here
