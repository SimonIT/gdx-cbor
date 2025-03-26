# gdx-cbor

![Maven Central Version](https://img.shields.io/maven-central/v/dev.simonit/gdx-cbor)
[![Build and publish](https://github.com/SimonIT/gdx-cbor/actions/workflows/build-and-publish.yml/badge.svg)](https://github.com/SimonIT/gdx-cbor/actions/workflows/build-and-publish.yml)
[![javadoc](https://javadoc.io/badge2/dev.simonit/gdx-cbor/javadoc.svg)](https://javadoc.io/doc/dev.simonit/gdx-cbor)

`gdx-cbor` is a Java library for encoding and decoding [CBOR (Concise Binary Object Representation)](https://cbor.io) data, specifically designed to work with libGDX.
It uses the [cbor-java](https://github.com/cbor-java/cbor-java) library under the hood to encode and decode CBOR data.

> “The Concise Binary Object Representation (CBOR) is a data format whose design goals include the possibility of extremely small code size, fairly small message size, and extensibility without the need for version negotiation.”

## Features

- Encode and decode CBOR data
- Seamless integration with libGDX
- Supports various data types including strings, numbers, arrays, and maps

## Installation

Add the dependency to your core project:

```groovy
dependencies {
    implementation "dev.simonit:gdx-cbor:0.0.2"
}
```

### GWT

To use `gdx-cbor` with GWT, you need to include the sources in your gwt project:

```groovy
dependencies {
    implementation "dev.simonit:gdx-cbor:0.0.2:sources"
}
```

Then, add the following line to your GWT module (.gwt.xml) file:

```xml
<inherits name="dev.simonit.gdx-cbor"/>
```

## Usage

Because `gdx-cbor` is designed to work with libGDX, you can use the `Json` class to encode and decode CBOR data. The `CborReader` and `CborWriter` classes are used to read and write CBOR data, respectively. [More about libGDX Serializing and Deserializing](https://libgdx.com/wiki/utils/reading-and-writing-json)

### Encoding Data

```java
import dev.simonit.gdx.cbor.Cbor;

public void encodeData(Object data) {
	Cbor cbor = new Cbor();
	byte[] encodedData = cbor.toCbor(data);
}
```

(all other `cbor.toJson()` methods also produce CBOR data!)

### Decoding Data

```java
import dev.simonit.gdx.cbor.Cbor;

public void decodeData(byte[] data) {
	Cbor cbor = new Cbor();
	Object obj = cbor.fromCbor(Object.class, data);
}
```

(all other `cbor.fromJson()` methods also read CBOR data!)

#### Reading to DOM

```java
import dev.simonit.gdx.cbor.CborReader;
import dev.simonit.gdx.cbor.CborValue;

public void readCborData(byte[] data) {
	CborValue root = new CborReader().parse(data);
}
```

## Contributing

Contributions are welcome! Please open an issue or submit a pull request on GitHub.

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for more information.

