## HTTP API DOCS

> `BASE_PATH` : [/iot-comm/api](http://localhost:8080/iot-comm/api)

- [/member](#member)
    - `POST` [`/signup`](#post-membersignup)
    - `POST` [`/login`](#post-memberlogin)
- [/library](#library)
    - `GET` `POST` [`/seat-list`](#get-post-libraryseat-list)
    - `POST` [`/logout`](#post-librarylogout)
    - `POST` [`/login`](#post-librarylogin)
- [/admin](#admin)
    - `POST` [`/sudo-logout`](#post-adminsudo-logout)

### `/member`

#### `POST` /member/signup

> 회원가입

##### Responses

| Code | Description         |
|------|---------------------|
| 200  | Signup success      |
| 409  | User already Exists |

#### `POST` /member/login

> 로그인

##### Responses

| Code | Description       |
|------|-------------------|
| 200  | Login success     |
| 401  | ID/PW not correct |

### `/library`

#### `GET` `POST` /library/seat-list

> `GET`  현재 좌석 정보 출력   
> `POST`  현재 좌석 정보 검색

##### Responses

| Code | Description     |
|------|-----------------|
| 200  | Request success |

#### `POST` /library/logout

> 사용종료 요청

##### Parameters

| Name    | Located in | Description | Required | Schema |
|---------|------------|-------------|----------|--------|
| sess_id | cookie     |             | Yes      | string |

##### Responses

| Code | Description     |
|------|-----------------|
| 200  | Request success |

#### `POST` /library/login

> 자리에 앉고 사용시작 요청

##### Parameters

| Name    | Located in | Description | Required | Schema |
|---------|------------|-------------|----------|--------|
| sess_id | cookie     |             | Yes      | string |

##### Responses

| Code | Description                                       |
|------|---------------------------------------------------|
| 200  | Request success                                   |
| 400  | Not valid parameter[time] (maybe not return this) |
| 401  | Not valid certification                           |
| 403  | Already using other seat                          |
| 404  | Not valid parameter[seat_id] (seat not exist)     |
| 409  | Seat not available now (already using)            |

### `/admin`

#### `POST` /admin/sudo-logout

> 강제 로그아웃

##### Parameters

| Name    | Located in | Description | Required | Schema |
|---------|------------|-------------|----------|--------|
| sess_id | cookie     |             | Yes      | string |

##### Responses

| Code | Description                                   |
|------|-----------------------------------------------|
| 200  | Request success                               |
| 401  | Not valid certification                       |
| 403  | Access Denied                                 |
| 404  | Not valid parameter[seat_id] (seat not exist) |

## WebSocket

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
