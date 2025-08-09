# Weather RESTful APIs Sample

A Spring Boot application that provides a RESTful API for weather information, integrating with OpenWeatherMap service using RestTemplate.

## Features

- **API Key Authentication**: 5 predefined API keys with rate limiting (5 requests per hour per key)
- **Weather Data Retrieval**: Get real weather information by city and country from OpenWeatherMap API
- **Data Persistence**: Store weather data in H2 in-memory database
- **Rate Limiting**: Hourly rate limiting per API key
- **RESTful Design**: Follows REST API conventions
- **Clear Spring Layers**: Controller, Service, Repository, and Domain layers
- **OpenWeatherMap Integration**: Real-time weather data from OpenWeatherMap API
- **Data Caching**: 1-hour cache for same city requests to reduce API calls

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- OpenWeatherMap API key (free tier available at https://openweathermap.org/api)

## Getting OpenWeatherMap API Key

### Step 1: Register Account
1. Visit [OpenWeatherMap](https://openweathermap.org/)
2. Click "Sign Up" to create a free account
3. Verify your email address

### Step 2: Get API Key
1. Log in to your account
2. Navigate to "API keys" section
3. Copy your default API key
4. **Note**: New API keys may take a few hours to activate

### Step 3: Free Tier Limits
- **Daily calls**: 1,000 requests per day
- **Minute calls**: 60 requests per minute
- **Data**: Current weather, 5-day forecast, and more

## Configuration

### 1. Update OpenWeatherMap API Configuration
Edit `src/main/resources/application.yml`:
```yaml
# OpenWeatherMap Configuration
openweathermap:
  api:
    key: YOUR_OPENWEATHERMAP_API_KEY  # Replace with your actual API key
    base-url: http://api.openweathermap.org/data/2.5
    units: metric  # metric, imperial, kelvin
```

### 2. Predefined API Keys
The application comes with 5 predefined API keys for testing:
- `5b595d4b-01d3-4d2c-9c51-508d344f2022`
- `8c610030-ae5c-4e61-a455-d6e57af180c4`
- `6682b565-f821-42e6-b2f5-46987514919a`
- `8a82ecc8-54d6-43bc-a68b-8015251e09e6`
- `f6e167de-a301-417d-8637-097f961d2946`

## Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Update the OpenWeatherMap API key in `application.yml`
4. Run the application:
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

## Data Caching Strategy

- **Cache Duration**: 1 hour for same city/country requests
- **Cache Storage**: H2 in-memory database
- **Cache Invalidation**: Automatic after 1 hour
- **Benefits**: Reduces OpenWeatherMap API calls and improves response time

## Database

The application uses H2 in-memory database with the following tables:
- `weather_data`: Stores weather information from OpenWeatherMap API
- `api_key_usage`: Tracks API key usage for rate limiting

### H2 Console
Access the H2 console at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:weatherdb`
- Username: `sa`
- Password: `password`

## OpenWeatherMap API Integration

### Features
- **RestTemplate**: HTTP client for API calls
- **Timeout Configuration**: 10s connect, 30s read timeout
- **Error Handling**: Comprehensive error handling for API failures
- **Response Mapping**: Automatic JSON to DTO mapping
- **Logging**: Detailed logging for API calls and responses

### API Endpoint Used
```
GET http://api.openweathermap.org/data/2.5/weather?q={city},{country}&appid={apiKey}&units={units}
```

### Response Processing
- Extracts weather description (main requirement)
- Stores complete weather data in database
- Handles API errors gracefully

## Technologies Used

- **Spring Boot 3.2.0**: Main framework
- **Spring Data JPA**: Database operations
- **H2 Database**: In-memory database
- **RestTemplate**: HTTP client for OpenWeatherMap API calls
- **Spring Validation**: Input validation
- **Jackson**: JSON processing
- **Spring Boot Configuration Properties**: Configuration management

## Testing

### Manual Testing
Use the provided test script:
```bash
./test-api.sh
```

### Automated Testing
Run the tests:
```bash
mvn test
```

## Troubleshooting

### Common Issues

1. **API Key Not Working**
   - Ensure API key is correctly configured in `application.yml`
   - New API keys may take 2-3 hours to activate
   - Check OpenWeatherMap account for API key status

2. **Rate Limit Exceeded**
   - Each API key is limited to 5 requests per hour
   - Use different API keys for testing
   - Check H2 console for usage tracking

3. **Network Errors**
   - Check internet connection
   - Verify OpenWeatherMap API is accessible
   - Check firewall settings

4. **Invalid City/Country**
   - Use valid city and country combinations
   - Check OpenWeatherMap documentation for supported locations

## License

This project is for educational purposes. 