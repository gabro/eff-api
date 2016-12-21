# eff-api
A sample project for experimenting with Eff.

## Structure
### Router
The router layer uses akka-http. This part is the less structured, as it was of little interest for this project. Everything is implemented in `Boot.scala`.
The JSON (de)serialization is fully automatic thanks to circe.

### Controller
The `UserController` interface publicly exposes methods returning `Future[Result[?]]` (where `Result[?]` is `Either[LogicError, ?]]`.
Internally it uses `Eff` for composing effects, which are then run upon request.

The effect stack in which the effects are run and the `runEffect` method are defined in `package.scala`.

### Data
The data layer exposes methods returning `Eff[R, ?]` and it uses Slick to access the database and perform SQL queries.
Since Slick operations return `Future`s, the `fromFuture` method from Eff's `AsyncFutureInterpreter` is used to send them into an `Async` effect.

## Errors
Errors are part of the effect stack, using the `Either` effect. Currently their serialization is naive (they are returned as a 200).

## Try this out

Just clone the project and then

```bash
sbt run
```

The API uses to an in-memory H2 db, and no configuration is required.

Once the server has started, you can try the following HTTP requests (the examples below use [`httpie`](https://httpie.org/))

```bash
http GET localhost:8080/users/1
http PUT localhost:8080/users/2 id:=2 username=Test
```
