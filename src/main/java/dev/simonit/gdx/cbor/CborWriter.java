
package dev.simonit.gdx.cbor;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.builder.AbstractBuilder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.builder.MapBuilder;
import co.nstant.in.cbor.builder.MapEntryBuilder;
import co.nstant.in.cbor.model.SimpleValue;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.Null;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;

public class CborWriter extends JsonWriter {
	protected AbstractBuilder<?> builder;
	protected CborEncoder encoder;
	/** If true, writes char values as strings, otherwise as integers. */
	protected boolean writeCharAsString = true;

	public CborWriter (OutputStream outputStream) {
		super(new OutputStreamWriter(outputStream));
		encoder = new CborEncoder(outputStream);
		builder = new CborBuilder();
	}

	public AbstractBuilder<?> getBuilder () {
		return builder;
	}

	public CborEncoder getEncoder () {
		return encoder;
	}

	public boolean isWriteCharAsString () {
		return writeCharAsString;
	}

	public CborWriter writeCharAsString (boolean writeCharAsString) {
		this.writeCharAsString = writeCharAsString;
		return this;
	}

	@Override
	public CborWriter name (String name) throws IOException {
		if (builder instanceof CborBuilder) {
			throw new IllegalStateException("Cannot set name \"" + name + "\" on root");
		} else if (builder instanceof ArrayBuilder) {
			throw new IllegalStateException("Cannot set name \"" + name + "\" on array");
		} else if (builder instanceof MapBuilder) {
			builder = ((MapBuilder<?>)builder).addKey(name);
		} else {
			throw new IllegalStateException("Unexpected builder: " + builder.getClass());
		}
		return this;
	}

	@Override
	public CborWriter object () throws IOException {
		if (builder instanceof CborBuilder) {
			builder = ((CborBuilder)builder).addMap();
		} else if (builder instanceof ArrayBuilder) {
			builder = ((ArrayBuilder<?>)builder).addMap();
		} else if (builder instanceof MapEntryBuilder) {
			builder = ((MapEntryBuilder<?>)builder).valueMap();
		} else {
			throw new IllegalStateException("Unexpected builder: " + builder.getClass());
		}
		return this;
	}

	@Override
	public CborWriter array () throws IOException {
		if (builder instanceof CborBuilder) {
			builder = ((CborBuilder)builder).addArray();
		} else if (builder instanceof ArrayBuilder) {
			builder = ((ArrayBuilder<?>)builder).addArray();
		} else if (builder instanceof MapEntryBuilder) {
			builder = ((MapEntryBuilder<?>)builder).valueArray();
		} else {
			throw new IllegalStateException("Unexpected builder: " + builder.getClass());
		}
		return this;
	}

	@Override
	public CborWriter value (@Null Object value) throws IOException {
		if (builder instanceof CborBuilder) {
			if (value == null) {
				builder = ((CborBuilder)builder).add(SimpleValue.NULL);
			} else if (value instanceof BigInteger) {
				builder = ((CborBuilder)builder).add((BigInteger)value);
			} else if (value instanceof Float) {
				builder = ((CborBuilder)builder).add((float)value);
			} else if (value instanceof Double) {
				builder = ((CborBuilder)builder).add((double)value);
			} else if (value instanceof Number) {
				builder = ((CborBuilder)builder).add(((Number)value).longValue());
			} else if (value instanceof Boolean) {
				builder = ((CborBuilder)builder).add((boolean)value);
			} else if (value instanceof String) {
				builder = ((CborBuilder)builder).add((String)value);
			} else if (value instanceof byte[]) {
				builder = ((CborBuilder)builder).add((byte[])value);
			} else if (value instanceof Character) {
				if (writeCharAsString) {
					builder = ((CborBuilder)builder).add(value.toString());
				} else {
					builder = ((CborBuilder)builder).add((char)value);
				}
			} else {
				throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
			}
		} else if (builder instanceof ArrayBuilder) {
			if (value == null) {
				builder = ((ArrayBuilder<?>)builder).add(SimpleValue.NULL);
			} else if (value instanceof Float) {
				builder = ((ArrayBuilder<?>)builder).add((float)value);
			} else if (value instanceof Double) {
				builder = ((ArrayBuilder<?>)builder).add((double)value);
			} else if (value instanceof Number) {
				builder = ((ArrayBuilder<?>)builder).add(((Number)value).longValue());
			} else if (value instanceof Boolean) {
				builder = ((ArrayBuilder<?>)builder).add((boolean)value);
			} else if (value instanceof String) {
				builder = ((ArrayBuilder<?>)builder).add((String)value);
			} else if (value instanceof byte[]) {
				builder = ((ArrayBuilder<?>)builder).add((byte[])value);
			} else if (value instanceof Character) {
				if (writeCharAsString) {
					builder = ((ArrayBuilder<?>)builder).add(value.toString());
				} else {
					builder = ((ArrayBuilder<?>)builder).add((char)value);
				}
			} else {
				throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
			}
		} else if (builder instanceof MapEntryBuilder) {
			if (value == null) {
				builder = ((MapEntryBuilder<?>)builder).value(SimpleValue.NULL);
			} else if (value instanceof Float) {
				builder = ((MapEntryBuilder<?>)builder).value((float)value);
			} else if (value instanceof Double) {
				builder = ((MapEntryBuilder<?>)builder).value((double)value);
			} else if (value instanceof Number) {
				builder = ((MapEntryBuilder<?>)builder).value(((Number)value).longValue());
			} else if (value instanceof Boolean) {
				builder = ((MapEntryBuilder<?>)builder).value((boolean)value);
			} else if (value instanceof String) {
				builder = ((MapEntryBuilder<?>)builder).value((String)value);
			} else if (value instanceof byte[]) {
				builder = ((MapEntryBuilder<?>)builder).value((byte[])value);
			} else if (value instanceof Character) {
				if (writeCharAsString) {
					builder = ((MapEntryBuilder<?>)builder).value(value.toString());
				} else {
					builder = ((MapEntryBuilder<?>)builder).value((char)value);
				}
			} else {
				throw new IllegalArgumentException("Unsupported value type: " + value.getClass());
			}
		} else {
			throw new IllegalStateException("Unexpected builder: " + builder.getClass());
		}
		return this;
	}

	@Override
	public CborWriter json (String json) throws IOException {
		return this.value(json);
	}

	@Override
	public CborWriter pop () throws IOException {
		if (builder instanceof CborBuilder) {
			throw new IllegalStateException("Cannot pop root builder");
		} else if (builder instanceof ArrayBuilder) {
			builder = ((ArrayBuilder<?>)builder).end();
		} else if (builder instanceof MapBuilder) {
			builder = ((MapBuilder<?>)builder).end();
		} else {
			throw new IllegalStateException("Unexpected builder: " + builder.getClass());
		}
		return this;
	}

	@Override
	public void close () throws IOException {
		while (!(builder instanceof CborBuilder))
			pop();
		try {
			encoder.encode(((CborBuilder)builder).build());
		} catch (CborException e) {
			throw new IOException(e);
		}
		((CborBuilder)builder).reset();
		super.close();
	}
}
