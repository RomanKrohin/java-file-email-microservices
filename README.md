# File Manager and Email Notification Microservices

## Описание проекта

Этот проект состоит из двух микросервисов - `core` и `mail`. Микросервис `core` отвечает за авторизацию и аутентификацию пользователей, а также за загрузку и управление изображениями. Микросервис `mail` отвечает за отправку уведомлений по электронной почте при выполнении определенных действий, таких как загрузка и скачивание изображений.

## Хранение данных

- **Данные пользователей**: Хранятся в таблице `users` базы данных PostgreSQL. Таблица содержит информацию о пользователях, включая их email, зашифрованный пароль и роль.
- **Изображения**: Хранятся в виде байтовых массивов в таблице `file_metadata` базы данных PostgreSQL. Таблица содержит метаданные файлов, включая имя файла, размер, время загрузки и email пользователя, которому принадлежит файл.

### Команда для создания таблицы пользователей

```sql
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    is_blocked BOOLEAN DEFAULT FALSE
);
```

### Команда для создания таблицы метаданных файлов

```sql
CREATE TABLE file_metadata (
    id SERIAL PRIMARY KEY,
    file_name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    upload_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data BYTEA NOT NULL,
    user_email VARCHAR(255) REFERENCES users(email)
);
```

## Примеры использования

### Регистрация пользователя

```http
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "email": "newuser@example.com",
  "password": "securepassword",
  "role": "USER"
}
```

### Вход в систему

```http
POST http://localhost:8080/auth/login?email=newuser@example.com&password=securepassword
```

### Загрузка изображений

```http
POST http://localhost:8080/files/upload
Authorization: Bearer <your_jwt_token>
Content-Type: multipart/form-data; boundary=boundary

--boundary
Content-Disposition: form-data; name="files"; filename="image.png"
Content-Type: image/png

< D:/image.png
--boundary--
```

### Скачивание изображения

```http
GET http://localhost:8080/files/{id}
Authorization: Bearer <your_jwt_token>
```

### Получение списка файлов с сортировкой

```http
GET http://localhost:8080/files?sortBy=size
Authorization: Bearer <your_jwt_token>
```

### Получение списка всех файлов (для модератора)

```http
GET http://localhost:8080/files/mod/files
Authorization: Bearer <your_jwt_token>
```

### Разблокировка пользователя (для модератора)

```http
POST http://localhost:8080/auth/mod/unblock-user?email=newuser@example.com
Authorization: Bearer <your_jwt_token>
```

### Блокировка пользователя (для модератора)

```http
POST http://localhost:8080/auth/mod/block-user?email=newuser@example.com
Authorization: Bearer <your_jwt_token>
```

## Основные особенности кода

- **Авторизация и аутентификация**: Реализованы с использованием Spring Security и JWT токенов.
- **Управление файлами**: Пользователи могут загружать, скачивать и просматривать свои файлы, а также получать списки файлов с сортировкой.
- **Уведомления по электронной почте**: Уведомления отправляются при загрузке и скачивании файлов через сервис RabbitMQ и доставляются с использованием JavaMailSender.
- **Права доступа модераторов**: Модераторы могут просматривать все файлы, а также блокировать и разблокировать пользователей.

