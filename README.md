### HTTP API DOCS

> [Link](https://iotcomm.github.io/LibraEnd/)

### WebSocket Form

> Only admin can connect/use WS session.

#### Send Data

| Field     | DataType |
|-----------|----------|
| seat_id   | Int      |
| is_active | Boolean  |

> Example
> ```json
> {
> "seat_id": 2,
> "is_active": false
> }
> ```
>

#### Get Data

> Only on timeout situation.

| Field       | DataType |
|-------------|----------|
| seat_id     | Int      |
| description | String   |

> Example
> ```json
> {
> "seat_id": 2,
> "description": "Seat Timeout"
> }
> ```
