### Http Polling type 통신 규약

> Content-type: application/json

#### Client to Server

- `URL`: http://iot.kuro9.dev/iot-comm/ws/fallback/seats
- `Method`: PUT
- `Content-type`: application/json

```json
{
  "key": "string",
  "seats": [
    {
      "seat_id": "number",
      "is_active": "bool"
    }
  ]
}
```

> 자리 변화(Diff)만 전송
> key: verysecretadminkey

#### Server to Client(Response)

```json
{
  "seat_state_change": [
    {
      "seat_id": "number",
      "is_active": "bool"
    }
  ],
  "seat_timeout": [
    {
      "seat_id": "number",
      "description": "string"
    }
  ],
  "desk": [
    {
      "id": "number",
      "state": "boolean"
    }
  ]
}
```

> 이전 전송 당시의 state에 대해 변화한 정보만 전송
