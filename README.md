This project demonstrates how two microservices communicate using **gRPC** in Java.

## Services

### Order Service (`localhost:50052`)
- Returns a **list of orders** for a given user ID.

### User Service (`localhost:50051`)
- Returns **user details** along with the **total number of orders** placed by the user.
- Internally makes a **gRPC call to the Order Service** using a generated client stub.

---

## How to Run the Project

1. **Start both services** via your IDE:
   - Run the `main` method in the `OrderServer` class.
   - Run the `main` method in the `UserServer` class.

2. Confirm the services are listening on:
   - **Order Service** → `localhost:50052`
   - **User Service** → `localhost:50051`

3. **Open [BloomRPC](https://github.com/bloomrpc/bloomrpc/releases)** (or any gRPC testing tool).

4. **Import the `.proto` file** used in the project.

5. **Make a unary call** to the User Service at `localhost:50051`.

### Sample Response (BloomRPC)
![User Service Response](./images/user-service-response.png)
