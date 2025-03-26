
package dev.simonit.gdx.cbor;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class Cbor extends Json {
	protected ByteArrayOutputStream output = new ByteArrayOutputStream();
	protected CborWriter writer = new CborWriter(output);

	public Cbor () {
		setReader(new CborReader());
		setWriter(writer);
	}

	/** @param knownType May be null if the type is unknown.
	 * @param elementType May be null if the type is unknown.
	 * @return CBOR representation of the object encoded as a base64 string. */
	@Override
	public String toJson (@Null Object object, @Null Class knownType, @Null Class elementType) {
		return new String(Base64Coder.encode(toCbor(object, knownType, elementType)));
	}

	/** @param knownType May be null if the type is unknown.
	 * @param elementType May be null if the type is unknown. */
	@Override
	public void toJson (@Null Object object, @Null Class knownType, @Null Class elementType, FileHandle file) {
		try (OutputStream out = file.write(false)) {
			CborWriter writer = new CborWriter(out);
			toJson(object, knownType, elementType, writer);
		} catch (Exception ex) {
			throw new SerializationException("Error writing file: " + file, ex);
		}
	}

	public byte[] toCbor () {
		StreamUtils.closeQuietly(this.writer);
		byte[] bytes = output.toByteArray();
		output.reset();
		return bytes;
	}

	public byte[] toCbor (@Null Object object) {
		return toCbor(object, object == null ? null : object.getClass(), null);
	}

	/** @param knownType May be null if the type is unknown. */
	public byte[] toCbor (@Null Object object, @Null Class knownType) {
		return toCbor(object, knownType, null);
	}

	/** @param knownType May be null if the type is unknown.
	 * @param elementType May be null if the type is unknown. */
	public byte[] toCbor (@Null Object object, @Null Class knownType, @Null Class elementType) {
		toJson(object, knownType, elementType, writer);
		byte[] bytes = output.toByteArray();
		output.reset();
		return bytes;
	}

	/** @param type May be null if the type is unknown.
	 * @return May be null. */
	@Null
	public <T> T fromCbor (Class<T> type, byte[] cbor) {
		return fromJson(type, new ByteArrayInputStream(cbor));
	}
}
