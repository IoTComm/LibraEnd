## HTTP API DOCS

> `HOMEPAGE_PATH` : [iot.kuro9.dev/iot-comm](http://iot.kuro9.dev/iot-comm)    
> `API_BASE_PATH` : [iot.kuro9.dev/iot-comm/api](http://iot.kuro9.dev/iot-comm/api)

- [/member](#member)
    - `POST` [`/signup`](#post-membersignup)
    - `POST` [`/login`](#post-memberlogin)
    - `GET` [`/my-id`](#get-membermy-id)
    - `GET` [`/my-seat`](#get-membermy-seat)
- [/library](#library)
    - `GET` `POST` [`/seat-list`](#get-post-libraryseat-list)
    - `POST` [`/logout`](#post-librarylogout)
    - `POST` [`/login`](#post-librarylogin)
- [/admin](#admin)
    - `POST` [`/sudo-logout`](#post-adminsudo-logout)

### `/member`

#### `POST` /member/signup

<hr>

> 회원가입

##### Parameters

| Name | Located in       | Description | Required | Schema |
|------|------------------|-------------|----------|--------|
| id   | application/json | ID          | Yes      | int    |
| pw   | application/json | Password    | Yes      | string |

> Example
> ```json
> {
>   "id": 2100123,
>   "pw": "q1w2e3r4!"
> }
> ```

##### Responses

###### Type

| Name        | Schema |
|-------------|--------|
| code        | int    |
| description | string |

> Example
> ```json
> {
>   "code": 200,
>   "description": "OK"
> }
> ```

###### Response Code

| Code | Description         |
|------|---------------------|
| 200  | Signup success      |
| 409  | User already Exists |

#### `POST` /member/login

<hr>

> 로그인

##### Parameters

| Name | Located in       | Description | Required | Schema |
|------|------------------|-------------|----------|--------|
| id   | application/json | ID          | Yes      | int    |
| pw   | application/json | Password    | Yes      | string |

> Example
> ```json
> {
>   "id": 2100123,
>   "pw": "q1w2e3r4!"
> }
> ```

##### Responses

###### Type

| Name        | Schema |
|-------------|--------|
| code        | int    |
| description | string |

- **This API Call Set Access Key in Client's Cookie**

> Example
> ```json
> {
>   "code": 200,
>   "description": "OK"
> }
> ```

###### Response Code

| Code | Description       |
|------|-------------------|
| 200  | Login success     |
| 401  | ID/PW not correct |

#### `GET` /member/my-id

<hr>

> 내 아이디 가져오기

##### Parameters

| Name    | Located in | Description | Required | Schema |
|---------|------------|-------------|----------|--------|
| sess_id | cookie     | Access Key  | Yes      | string |

##### Responses

###### Type

| Name        | Schema           |
|-------------|------------------|
| code        | int              |
| description | string           |
| data        | UserIdReturnForm |

###### UserIdReturnForm Type

| Name    | Schema |
|---------|--------|
| user_id | int    |

> Example
> ```json
> {
>   "code": 200,
>   "description": "OK",
>   "data": {
>     "user_id": 2171012
>   } 
> }
> ```
>

###### Response Code

| Code | Description         |
|------|---------------------|
| 200  | 성공                  |
| 400  | 유저의 sessId가 유효하지 않음 |
| 401  | sessId 없음           |

#### `GET` /member/my-seat

<hr>

> 내 좌석번호 가져오기

##### Parameters

| Name    | Located in | Description | Required | Schema |
|---------|------------|-------------|----------|--------|
| sess_id | cookie     | Access Key  | Yes      | string |

##### Responses

###### Type

| Name        | Schema           |
|-------------|------------------|
| code        | int              |
| description | string           |
| data        | SeatIdReturnForm |

###### SeatIdReturnForm Type

| Name    | Schema |
|---------|--------|
| seat_id | int    |

> Example
> ```json
> {
>   "code": 200,
>   "description": "OK",
>   "data": {
>     "seat_id": 2
>   } 
> }
> ```
> 유저의 좌석 정보 없음
> ```json
> {
>   "code": 200,
>   "description": "OK",
>   "data": {
>     "seat_id": null
>   }
> }
> ```
>

###### Response Code

| Code | Description         |
|------|---------------------|
| 200  | 성공                  |
| 400  | 유저의 sessId가 유효하지 않음 |
| 401  | sessId 없음           |

### `/library`

#### `GET` `POST` /library/seat-list

<hr>

> `GET`  현재 좌석 정보 출력   
> `POST`  현재 좌석 정보 검색

##### Parameters

| Name     | Located in       | Description            | Required | Schema |
|----------|------------------|------------------------|----------|--------|
| seat_id  | application/json | seat id to find        | No       | int    |
| is_using | application/json | find using/!using seat | No       | bool   |
| desk_id  | application/json | find seat with desk id | No       | int    |

> Example
> ```json
> {
>   "desk_id": 1,
>   "is_using": true
> }
> ```

##### Responses

###### Type

| Name        | Schema           |
|-------------|------------------|
| code        | int              |
| description | string           |
| data        | List\<SeatTable> |

###### SeatTable Type

| Name     | Schema |
|----------|--------|
| seat_id  | int    |
| is_using | bool   |
| desk_id  | int    |

> Example
> ```json
> {
>   "code": 200,
>   "description": "OK",
>   "data": [
>     {
>       "seat_id": 1,
>       "is_using": false,
>      "desk_id": 1
>     },
>     {
>       "seat_id": 2,
>       "is_using": true,
>       "desk_id": 1
>     },
>     {
>       "seat_id": 3,
>       "is_using": true,
>       "desk_id": 2
>     }
>   ]
> }
> ```

###### Response Code

| Code | Description     |
|------|-----------------|
| 200  | Request success |

#### `POST` /library/logout

<hr>

> 사용종료 요청

##### Parameters

| Name    | Located in | Description | Required | Schema |
|---------|------------|-------------|----------|--------|
| sess_id | cookie     | Access Key  | Yes      | string |

##### Responses

| Code | Description     |
|------|-----------------|
| 200  | Request success |

#### `POST` /library/login

<hr>
> 자리에 앉고 사용시작 요청

##### Parameters

| Name    | Located in       | Description    | Required | Schema |
|---------|------------------|----------------|----------|--------|
| sess_id | cookie           | Access Key     | Yes      | string |
| seat_id | application/json | seat id to use | Yes      | string |

##### Responses

###### Type

| Name        | Schema |
|-------------|--------|
| code        | int    |
| description | string |

> Example
> ```json
> {
>   "code": 200,
>   "description": "OK"
> }
> ```

###### Response Code

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

<hr>

> 강제 로그아웃

##### Parameters

| Name    | Located in       | Description             | Required | Schema |
|---------|------------------|-------------------------|----------|--------|
| sess_id | cookie           | Access Key              | Yes      | string |
| seat_id | application/json | seat id to force-logout | Yes      | string |

##### Responses

###### Type

| Name        | Schema             |
|-------------|--------------------|
| code        | int                |
| description | string             |
| data        | LastUsedReturnForm |

###### LastUsedReturnForm Type

| Name              | Schema |
|-------------------|--------|
| last_used_user_id | int    |

> Example
> ```json
> {
>   "code": 200,
>   "description": "OK",
>   "data": {
>     "last_used_user_id": 2100123
>   }
> }
> ```
>  ```json
> {
>   "code": 403,
>   "description": "Access Denied",
>   "data": null
> }
> ```

###### Response Code

| Code | Description                                   |
|------|-----------------------------------------------|
| 200  | Request success                               |
| 401  | Not valid certification                       |
| 403  | Access Denied                                 |
| 404  | Not valid parameter[seat_id] (seat not exist) |

## WebSocket

> Only admin can connect/use WS session.

#### Send Data

| Name      | Schema |
|-----------|--------|
| seat_id   | int    |
| is_active | bool   |

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

| Name        | Schema |
|-------------|--------|
| seat_id     | int    |
| description | string |

> Example
> ```json
> {
> "seat_id": 2,
> "description": "Seat Timeout"
> }
> ```
