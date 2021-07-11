# ZIO Playground project

A POC involving ZIO types and Layers.

The g8 templated used to this project was the [DevInsideYou Scala2 Template](https://github.com/DevInsideYou/scala-seed.g8).

The motivation to use ZIO and ZLayers come from [Scala Monster](https://scala.monster/welcome-zio/).

The idea is to evolve this exercise to allow saving messages to a Database and to stream them to a Queue.

I started using this [Adrian Filip's post](https://adrianfilip.com/2020/03/15/spring-to-zio-101/) as the base to a Console version ZIO program.

Next I evolved the UI to be a simple REST API and refactored ZIO Types to RIO instances as I've used Throwable as error.

## TODO

- Implement ZLayer for database.
- Check if the dependencies can be combined in a better way using Zlayers.
- Implement support for queue and/or streaming.
