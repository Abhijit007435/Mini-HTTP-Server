# Mini HTTP Server

A lightweight HTTP server built from scratch using Java Socket Programming.

This project was created to understand what happens underneath an HTTP request-response cycle, including TCP connection establishment, HTTP request parsing, static file resolution, HTTP response generation, and concurrent client handling.

---

## Overview

Instead of using a high-level web framework or an existing HTTP server such as Spring Boot/Tomcat, this project implements the basic HTTP server flow directly using Java networking APIs.

The server:

- Listens for TCP connections on port `8080`
- Accepts client connections using `ServerSocket`
- Handles clients using a fixed thread pool
- Reads and parses HTTP requests
- Supports `GET` requests
- Maps request paths to files inside the `static` directory
- Reads files as raw bytes
- Generates HTTP responses
- Sets appropriate HTTP headers
- Handles common HTTP errors
- Protects against path traversal attacks

The main purpose of the project is educational: to understand how HTTP works underneath frameworks and web servers.

---

## Request-Response Flow

The complete flow of the server is:

```text
                         CLIENT
                           |
                           | 1. TCP Connection
                           v
                    +--------------+
                    | ServerSocket |
                    |   Port 8080  |
                    +--------------+
                           |
                           | accept()
                           v
                        Socket
                           |
                           | 2. HTTP Request
                           v
                  +-------------------+
                  | Read InputStream  |
                  +-------------------+
                           |
                           v
                  +-------------------+
                  | Parse HTTP Request|
                  +-------------------+
                           |
                    GET + Path
                           |
                           v
                +----------------------+
                | Resolve Static File  |
                +----------------------+
                           |
                           v
                +----------------------+
                | Path Security Check  |
                +----------------------+
                           |
                           v
                  Read File as Bytes
                           |
                           v
                +----------------------+
                | Generate HTTP        |
                | Response             |
                +----------------------+
                           |
                           | OutputStream
                           v
                        Socket
                           |
                           | 3. HTTP Response
                           v
                         CLIENT