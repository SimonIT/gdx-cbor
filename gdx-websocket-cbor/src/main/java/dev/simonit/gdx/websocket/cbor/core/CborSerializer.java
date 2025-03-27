
package dev.simonit.gdx.websocket.cbor.core;

import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.impl.AbstractBinarySerializer;
import dev.simonit.gdx.cbor.Cbor;

public class CborSerializer extends AbstractBinarySerializer {

	private final Cbor cbor = new Cbor();

	/** @param preserveClassName if true, object's class name will be added to the CBOR representation under "class" key. If false,
	 *           class data will not be preserved. Default to true. Note that if you set this value to false, deserialization might
	 *           fail or return {@link com.badlogic.gdx.utils.JsonValue} instead of instance of the actual serialized object. Set
	 *           to false only if you don't need the class data or your server is not using the same serialization and does not
	 *           need class data. */
	public void setPreserveClassName (final boolean preserveClassName) {
		cbor.setTypeName(preserveClassName ? "class" : null);
	}

	@Override
	public byte[] serialize (final Object object) {
		try {
			return cbor.toCbor(object, Object.class);
		} catch (final Exception exception) {
			throw new SerializationException("Unable to serialize object to CBOR.", exception);
		}
	}

	@Override
	public Object deserialize (final byte[] data) {
		try {
			return cbor.fromCbor(null, data);
		} catch (final Exception exception) {
			throw new SerializationException("Unable to deserialize object from CBOR.", exception);
		}
	}

	/** @return direct reference to the {@link Cbor} serializer, which can be used to change serialization settings. */
	public Cbor getCbor () {
		return cbor;
	}
}
