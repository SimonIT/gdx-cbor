
package dev.simonit.gdx.cbor;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.SerializationException;
import com.badlogic.gdx.utils.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CborValue extends JsonValue {
	public CborValue (ValueType type) {
		super(type);
	}

	public CborValue (String value) {
		super(value);
	}

	public CborValue (double value) {
		super(value);
	}

	public CborValue (long value) {
		super(value);
	}

	public CborValue (double value, String stringValue) {
		super(value, stringValue);
	}

	public CborValue (long value, String stringValue) {
		super(value, stringValue);
	}

	public CborValue (boolean value) {
		super(value);
	}

	public CborValue (JsonValue value) {
		super(value.type());
		if (value.isObject()) {
			for (JsonValue child = value.child(); child != null; child = child.next()) {
				addChild(child.name(), new CborValue(child));
			}
		} else if (value.isArray()) {
			for (JsonValue child = value.child(); child != null; child = child.next()) {
				addChild(new CborValue(child));
			}
		} else if (value.isBoolean()) {
			this.set(value.asBoolean());
		} else if (value.isLong()) {
			this.set(value.asLong(), value.asString());
		} else if (value.isDouble()) {
			this.set(value.asDouble(), value.asString());
		} else if (value.isString()) {
			this.set(value.asString());
		}
	}

	public byte[] toCbor (JsonWriter.OutputType outputType) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
		CborWriter writer = new CborWriter(outputStream);
		writer.setOutputType(outputType);
		cbor(this, writer);
		StreamUtils.closeQuietly(writer);
		return outputStream.toByteArray();
	}

	private void cbor (CborValue object, CborWriter writer) {
		try {
			if (object.name() != null) {
				writer.name(object.name());
			}
			if (object.isObject()) {
				writer.object();
				for (CborValue child = (CborValue)object.child(); child != null; child = (CborValue)child.next()) {
					cbor(child, writer);
				}
				writer.pop();
			} else if (object.isArray()) {
				writer.array();
				for (CborValue child = (CborValue)object.child(); child != null; child = (CborValue)child.next()) {
					cbor(child, writer);
				}
				writer.pop();
			} else if (object.isString()) {
				writer.value(object.asString());
			} else if (object.isDouble()) {
				writer.value(object.asDouble());
			} else if (object.isLong()) {
				writer.value(object.asLong());
			} else if (object.isBoolean()) {
				writer.value(object.asBoolean());
			} else if (object.isNull()) {
				writer.value(null);
			} else
				throw new SerializationException("Unknown object type: " + object);
		} catch (IOException e) {
			throw new SerializationException("Error writing CBOR.", e);
		}
	}
}
