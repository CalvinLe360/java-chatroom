# java-chatroom

Repository for understanding Client-Server interaction with Sockets.

## Server

The server holds a server socket, which continuously accepts new connections on its designated port.

Once a server obtains a socket created on its port, it encapsulates the socket in a client handler, and gives the handler a thread of execution.

## Client

Acts as an interface for the client, in order to interact with the server.

A bufferedreader and bufferedwriter is tied to this socket's input and output streams respectively.

Uses multithreading to both listen for input and handle output.

## Client Handler

Reads from the client's socket's output stream.

Repeats the client's output and writes it to other client's input streams.

## Status

Completed.
