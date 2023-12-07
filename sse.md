### SSE Notify channel 통신 규약

#### 채널 오픈(init session)

> `URL`: https://iot.kuro9.dev/noti/open-channel   
> `Method`: GET

- **REQUIRE SESSION ID TO USE SSE NOTIFY**
  - Cookie: sess_id(로그인 시 쿠키에 저장되는 세션 키값)

#### 알림 수신

- event name으로 알림 종류 구분

  - `ping`: 서버에서 보내는 연결 확인용 핑(T=300sec)
  - `info`: 중요하지 않은 메시지
  - `state_change`: 좌석 변화
  - `user_warn`: 강제 퇴실 경고 메시지(반드시 유저에게 알림 필요)

- 스키마
  - `ping`, `info`, `user_warn`: string
  - `state_change`:
    ```json
    {
      "seat_id": "number",
      "is_active": "bool"
    }
    ```

#### 서버 연결 확인

> `URL`: https://iot.kuro9.dev/noti/heartbeat   
> `Method`: PUT

- **REQUIRE SESSION ID TO USE SSE NOTIFY**

  - Cookie: sess_id(로그인 시 쿠키에 저장되는 세션 키값)

- body 없음

- Response with 200 OK(No body)
