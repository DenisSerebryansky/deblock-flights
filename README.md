## ✈️ Deblock Flights Service

Some parts of this documentation and code comments were generated with the help of AI tools (e.g., ChatGPT) to speed up routine work and improve readability.
Usage, Assumptions and Future Improvements sections were originally written by the author and then refined with AI assistance

### 📌 Usage

The service exposes a single endpoint:

```bash
POST /api/v1/search
```

This endpoint returns a list of one-way flights, sorted by ascending price, based on the provided search criteria

The POST method is used (instead of GET) due to the complex structure of the request payload and its potential future
expansion

The architecture of the service is designed to make it easy to integrate new flight suppliers with minimal changes

### 🧠 Assumptions

1. Each leg of a round-trip flight is assumed to cost half of the total round-trip fare
2. For simplicity, the arrival time of each leg of a round-trip is assumed to be equal to its departure time
3. Responses from flight suppliers are considered valid and do not require additional validation
4. Complex multi-stop itineraries are not supported

### 🧪 Example Request & Response

#### ✍️ Request

```bash
POST /api/v1/search
Content-Type: application/json
```

```json
{
  "origin": "LHR",
  "destination": "CDG",
  "departureDate": "2025-10-15",
  "returnDate": "2025-10-20",
  "numberOfPassengers": 1
}
```

| Field                | Type    | Required | Description                                      |
|----------------------|---------|----------|--------------------------------------------------|
| `origin`             | string  | ✅        | IATA code of the departure airport (3 letters)   |
| `destination`        | string  | ✅        | IATA code of the destination airport (3 letters) |
| `departureDate`      | date    | ✅        | Departure date (YYYY-MM-DD)                      |
| `returnDate`         | date    | ✅        | Return date (must be ≥ departure date)           |
| `numberOfPassengers` | integer | ✅        | Number of passengers (1–4)                       |

#### 📬 Successful Response (200 OK)

```json
[
  {
    "airline": "Ryanair",
    "supplier": "ToughJet",
    "fare": 12.34,
    "departureAirportCode": "LHR",
    "destinationAirportCode": "CDG",
    "departureDate": "2025-10-15T12:44:00Z",
    "arrivalDate": "2025-10-15T12:44:00Z"
  },
  {
    "airline": "WizzAir",
    "supplier": "CrazyAir",
    "fare": 25.00,
    "departureAirportCode": "LHR",
    "destinationAirportCode": "CDG",
    "departureDate": "2025-10-15T11:23:45Z",
    "arrivalDate": "2025-10-15T14:23:45Z"
  }
]
```

| Field                    | Type     | Description                                                                |
|--------------------------|----------|----------------------------------------------------------------------------|
| `airline`                | string   | Name of the airline                                                        |
| `supplier`               | string   | Source flight supplier (e.g., `CrazyAir`, `ToughJet`)                      |
| `fare`                   | number   | Price of the one-way flight                                                |
| `departureAirportCode`   | string   | IATA code of the departure airport                                         |
| `destinationAirportCode` | string   | IATA code of the destination airport                                       |
| `departureDate`          | datetime | Departure date and time (ISO 8601)                                         |
| `arrivalDate`            | datetime | Arrival date and time (ISO 8601). For round-trip legs, may equal departure |

#### ❌ Validation Error (400 Bad Request)

```json
{
  "timestamp": "2025-10-10T12:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Return date must be on/after departure date",
  "path": "/api/v1/search"
}
```

### 🚀 Future Improvements

1. Add more flight suppliers to broaden coverage
2. Improve fare and duration calculations for round-trip flights to provide accurate arrival times
3. Implement caching for popular search requests (e.g., using Redis), with LRU and LFU eviction policies
4. Add JVM (CPU, I/O, memory) and business metrics (errors, SLIs, etc.) via InfluxDB or Prometheus, and visualize them
   with Grafana dashboards and alerting
5. Containerize the application with Docker and set up a CI/CD pipeline
6. Deploy behind a load balancer (reverse proxy, firewall) and scale horizontally by adding more instances when load
   increases
7. Use proxy services to avoid abusive request patterns when interacting with flight suppliers
8. Log requests via a message broker (Kafka) and store them in cold storage (e.g., Cassandra, ClickHouse) for further analytics
   and reporting