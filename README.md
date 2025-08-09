# Weather RESTful APIs Sample

A Spring Boot application that provides a RESTful API for weather information, integrating with OpenWeatherMap service.

## Features

- **API Key Authentication**: 5 predefined API keys with rate limiting (5 requests per hour per key)
- **Weather Data Retrieval**: Get weather information by city and country
- **Data Persistence**: Store weather data in H2 in-memory database
- **Rate Limiting**: Hourly rate limiting per API key
- **RESTful Design**: Follows REST API conventions
- **Clear Spring Layers**: Controller, Service, Repository, and Domain layers

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- OpenWeatherMap API key (free tier available at https://openweathermap.org/api)

## Configuration

1. Update the OpenWeatherMap API key in `src/main/resources/application.yml`:
```yaml
openweathermap:
  api:
    key: YOUR_OPENWEATHERMAP_API_KEY  # Replace with your actual API key
```

2. The application comes with 5 predefined API keys for testing:
   - `5b595d4b-01d3-4d2c-9c51-508d344f2022`
   - `8c610030-ae5c-4e61-a455-d6e57af180c4`
   - `6682b565-f821-42e6-b2f5-46987514919a`
   - `8a82ecc8-54d6-43bc-a68b-8015251e09e6`
   - `f6e167de-a301-417d-8637-097f961d2946`

## Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### 1. Get Weather Information (Path Variables)
```
GET /api/v1/weather/{city}/{country}?apiKey={apiKey}
```

**Example:**
```bash
curl "http://localhost:8080/api/v1/weather/London/uk?apiKey=5b595d4b-01d3-4d2c-9c51-508d344f2022"
```

### 2. Get Weather Information (Query Parameters)
```
GET /api/v1/weather/query?city={city}&country={country}&apiKey={apiKey}
```

**Example:**
```bash
curl "http://localhost:8080/api/v1/weather/query?city=London&country=uk&apiKey=5b595d4b-01d3-4d2c-9c51-508d344f2022"
```

### 3. Get Weather History by City and Country
```
GET /api/v1/weather/history/{city}/{country}
```

**Example:**
```bash
curl "http://localhost:8080/api/v1/weather/history/London/uk"
```

### 4. Get Weather History by API Key
```
GET /api/v1/weather/history?apiKey={apiKey}
```

**Example:**
```bash
curl "http://localhost:8080/api/v1/weather/history?apiKey=5b595d4b-01d3-4d2c-9c51-508d344f2022"
```

### 5. Health Check
```
GET /api/v1/weather/health
```

**Example:**
```bash
curl "http://localhost:8080/api/v1/weather/health"
```

## Response Format

### Success Response
```json
{
  "id": "800",
  "main": "Clear",
  "description": "clear sky",
  "icon": "01d",
  "city": "London",
  "country": "uk",
  "apiKey": "5b595d4b-01d3-4d2c-9c51-508d344f2022",
  "timestamp": "2025-01-08 10:30:00"
}
```

### Error Response
```json
{
  "timestamp": "2025-01-08T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid API key"
}
```

## Rate Limiting

- Each API key is limited to 5 requests per hour
- Rate limit is tracked per hour (resets at the start of each hour)
- When rate limit is exceeded, the API returns an error response

## Database

The application uses H2 in-memory database with the following tables:
- `weather_data`: Stores weather information
- `api_key_usage`: Tracks API key usage for rate limiting

### H2 Console
Access the H2 console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:weatherdb`
- Username: `sa`
- Password: `password`

## Project Structure

```
src/main/java/com/kg2s/
├── Application.java                 # Main application class
├── config/
│   └── WeatherApiConfig.java       # Configuration properties
├── controller/
│   └── WeatherController.java      # REST API endpoints
├── service/
│   ├── WeatherService.java         # Service interface
│   └── impl/
│       └── WeatherServiceImpl.java # Service implementation
├── repository/
│   ├── WeatherDataRepository.java  # Weather data repository
│   └── ApiKeyUsageRepository.java  # API key usage repository
├── domain/
│   ├── WeatherInfoResp.java        # Response DTO
│   ├── WeatherData.java            # Database entity
│   ├── ApiKeyUsage.java            # API key usage entity
├── client/
│   └── OpenWeatherMapClient.java   # OpenWeatherMap API client
└── exception/
    └── GlobalExceptionHandler.java # Global exception handler
```

## Testing

Run the tests:
```bash
mvn test
```

## Error Handling

The application includes comprehensive error handling:
- Invalid API keys
- Rate limit exceeded
- Invalid input parameters
- External API errors
- Database errors

## Technologies Used

- **Spring Boot 3.2.0**: Main framework
- **Spring Data JPA**: Database operations
- **H2 Database**: In-memory database
- **OpenFeign**: HTTP client for external API calls
- **Spring Validation**: Input validation
- **Jackson**: JSON processing

## License

This project is for educational purposes.
 
