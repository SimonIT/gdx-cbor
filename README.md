# gdx-cbor

`gdx-cbor` is a Java library for encoding and decoding CBOR (Concise Binary Object Representation) data, specifically designed to work with libGDX.
It uses the [cbor-java](https://github.com/cbor-java/cbor-java) library under the hood to encode and decode CBOR data.

## Features

- Encode and decode CBOR data
- Seamless integration with libGDX
- Supports various data types including strings, numbers, arrays, and maps

## Installation

Add the following to your `build.gradle` file:

```groovy
repositories {
    maven { url "https://oss.sonatype.org/content/repositories/releases/" }
}
```

```groovy
dependencies {
    implementation "dev.simonit:gdx-cbor:0.0.1"
}
```

## Usage

Because `gdx-cbor` is designed to work with libGDX, you can use the `Json` class to encode and decode CBOR data. The `CborReader` and `CborWriter` classes are used to read and write CBOR data, respectively. [More about libGDX Serializing and Deserializing](https://libgdx.com/wiki/utils/reading-and-writing-json)

### Encoding Data

```java
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import com.badlogic.gdx.utils.Json;
import dev.simonit.gdx.cbor.CborWriter;

public void encodeData(Object data) {
	Json json = new Json();
	OutputStream outputStream = new ByteArrayOutputStream();
	CborWriter writer = new CborWriter(outputStream);
	json.toJson(data, writer);
	byte[] encodedData = outputStream.toByteArray();
}
```

### Decoding Data

```java
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import com.badlogic.gdx.utils.Json;
import dev.simonit.gdx.cbor.CborReader;

public void decodeData(byte[] data) {
	Json json = new Json();
	json.setReader(new CborReader());
	InputStream inputStream = new ByteArrayInputStream(data);
	Object obj = json.fromJson(Object.class, inputStream);
}
```

#### Reading to DOM

```java
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import dev.simonit.gdx.cbor.CborReader;
import dev.simonit.gdx.cbor.CborValue;

public void readCborData(byte[] data) {
	InputStream inputStream = new ByteArrayInputStream(data);
	CborValue root = new CborReader().parse(inputStream);
}
```

## Contributing

Contributions are welcome! Please open an issue or submit a pull request on GitHub.

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for more information.

