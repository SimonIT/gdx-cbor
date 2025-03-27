# gdx-websocket-cbor

![Maven Central Version](https://img.shields.io/maven-central/v/dev.simonit/gdx-websocket-cbor)
[![Build and publish](https://github.com/SimonIT/gdx-cbor/actions/workflows/build-and-publish.yml/badge.svg)](https://github.com/SimonIT/gdx-cbor/actions/workflows/build-and-publish.yml)
[![javadoc](https://javadoc.io/badge2/dev.simonit/gdx-cbor/javadoc.svg)](https://javadoc.io/doc/dev.simonit/gdx-websocket-cbor)

`gdx-websocket-cbor` is a Java library for serializing and deserializing data using [CBOR (Concise Binary Object Representation)](https://cbor.io) in WebSocket communication, specifically designed to work with [gdx-websockets](https://github.com/MrStahlfelge/gdx-websockets).

## Features

- Encode and decode CBOR data
- Seamless integration with gdx-websockets
- Supports various data types including strings, numbers, arrays, and maps

## Installation

Add the dependency to your core project:

```groovy
dependencies {
    implementation "dev.simonit:gdx-websocket-cbor:0.0.2"
}
```

### GWT

To use `gdx-websocket-cbor` with GWT, you need to include the sources in your gwt project:

```groovy
dependencies {
    implementation "dev.simonit:gdx-websocket-cbor:0.0.2:sources"
}
```

Then, add the following line to your GWT module (.gwt.xml) file:

```xml
<inherits name="dev.simonit.gdx-websocket-cbor"/>
```

## Usage

Unsure what you need? Read [Custom serialization for libGDX Web Sockets](https://github.com/MrStahlfelge/gdx-websockets/blob/master/serialization/README.md) for the differences between the two serializers.

### Reflection-based Serialization

`gdx-websocket-cbor` you can use reflection-based serialization provided by libGDX to automatically serialize and deserialize your custom objects:

```java
import dev.simonit.gdx.websocket.cbor.core.CborSerializer;

void createWebSocket() {
	WebSocket webSocket = ...;
	CborSerializer serializer = new CborSerializer();
	webSocket.setSerializer(serializer);
}
```

### `ManualSerializer` Serialization

If you want to have more control over the serialization process, you can use the `ManualSerializer`:

```java
import dev.simonit.gdx.websocket.cbor.serialization.CborDeserializer;
import dev.simonit.gdx.websocket.cbor.serialization.CborSerializer;

void createWebSocket() {
	WebSocket webSocket = ...;
	ManualSerializer serializer = new ManualSerializer(new CborSerializer(), new CborDeserializer());
	webSocket.setSerializer(serializer);
}
```

More information on how to use the [Custom serialization for libGDX Web Sockets](https://github.com/MrStahlfelge/gdx-websockets/blob/master/serialization/README.md).
