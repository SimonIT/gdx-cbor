
package dev.simonit.gdx.websocket.cbor.serialization;

import co.nstant.in.cbor.CborBuilder;
import co.nstant.in.cbor.CborEncoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.builder.AbstractBuilder;
import co.nstant.in.cbor.builder.ArrayBuilder;
import co.nstant.in.cbor.model.DoublePrecisionFloat;
import co.nstant.in.cbor.model.HalfPrecisionFloat;
import co.nstant.in.cbor.model.SimpleValue;
import co.nstant.in.cbor.model.SinglePrecisionFloat;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Serializer;
import com.github.czyzby.websocket.serialization.impl.Size;
import com.github.czyzby.websocket.serialization.SerializationException;

import java.io.ByteArrayOutputStream;

public class CborSerializer extends Serializer {
	private static final int DEFAULT_BYTES_AMOUNT_ESTIMATION = 32;

	// Package private, as most serialization methods are in Size enum.
	ByteArrayOutputStream serializedData;
	AbstractBuilder<?> builder;

	public CborSerializer () {
		this(DEFAULT_BYTES_AMOUNT_ESTIMATION);
	}

	/** @param estimatedBytesAmount will create an array of bytes using this size. If the serialized object will have more bytes,
	 *           the array will be resized. If it has less - the relevant bytes will be copied to a new, shorter array upon final
	 *           serializing method. Has to be positive. */
	public CborSerializer (final int estimatedBytesAmount) {
		serializedData = new ByteArrayOutputStream(estimatedBytesAmount);
		builder = new CborBuilder();
	}

	/** Changes current byte index, effectively using current wrapped byte array to serialize another object. */
	public void reset () {
		serializedData.reset();
		((CborBuilder)builder).reset();
	}

	/** @param value will be serialized with 1 byte. Custom, more efficient boolean serializations must be implemented manually.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeBoolean (final boolean value) {
		if (builder instanceof CborBuilder) {
			((CborBuilder)builder).add(value);
		} else if (builder instanceof ArrayBuilder) {
			((ArrayBuilder<?>)builder).add(value);
		}
		return this;
	}

	/** @param value will be serialized with 1 byte.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeByte (final byte value) {
		if (builder instanceof CborBuilder) {
			((CborBuilder)builder).add(value);
		} else if (builder instanceof ArrayBuilder) {
			((ArrayBuilder<?>)builder).add(value);
		}
		return this;
	}

	/** @param value will be serialized with 2 bytes.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeShort (final short value) {
		return serializeShort(value, Size.SHORT);
	}

	@Override
	public CborSerializer serializeShort (final short value, final Size size) {
		if (builder instanceof CborBuilder) {
			((CborBuilder)builder).add(value);
		} else if (builder instanceof ArrayBuilder) {
			((ArrayBuilder<?>)builder).add(value);
		}
		return this;
	}

	/** @param value will be serialized with 4 bytes.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeInt (final int value) {
		return serializeInt(value, Size.INT);
	}

	/** @param value will be serialized.
	 * @param size amount of bytes used to serialize the number. If smaller than actual number size, will truncate. If bigger than
	 *           actual number size, will ignore the size and serialize with the actual size needed to store all bytes of number
	 *           data.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeInt (final int value, final Size size) {
		if (builder instanceof CborBuilder) {
			builder = ((CborBuilder)builder).add(value);
		} else if (builder instanceof ArrayBuilder) {
			builder = ((ArrayBuilder<?>)builder).add(value);
		}
		return this;
	}

	/** @param value will be serialized with 8 bytes.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeLong (final long value) {
		return serializeLong(value, Size.LONG);
	}

	/** @param value will be serialized.
	 * @param size amount of bytes used to serialize the number. If smaller than actual number size, will truncate.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeLong (final long value, final Size size) {
		if (builder instanceof CborBuilder) {
			((CborBuilder)builder).add(value);
		} else if (builder instanceof ArrayBuilder) {
			((ArrayBuilder<?>)builder).add(value);
		}
		return this;
	}

	/** @param value will be serialized with 4 bytes.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeFloat (final float value) {
		try {
			return serializeFloat(value, Size.INT);
		} catch (final SerializationException exception) {
			// Should never happen, INT is big enough to store a float.
			throw new RuntimeException("Unexpected serialization exception.", exception);
		}
	}

	/** @param value will be serialized.
	 * @param size amount of bytes used to serialize the number.
	 * @throws SerializationException if the size is too small to store this number.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeFloat (final float value, final Size size) throws SerializationException {
		if (builder instanceof CborBuilder) {
			switch (size) {
			case BYTE:
				throw new SerializationException("Byte is too small to store float data. Cannot truncate.");
			case SHORT:
				((CborBuilder)builder).add(new HalfPrecisionFloat(value));
				break;
			case INT:
				((CborBuilder)builder).add(new SinglePrecisionFloat(value));
				break;
			case LONG:
				((CborBuilder)builder).add(new DoublePrecisionFloat(value));
				break;
			}
		} else if (builder instanceof ArrayBuilder) {
			switch (size) {
			case BYTE:
				throw new SerializationException("Byte is too small to store float data. Cannot truncate.");
			case SHORT:
				((ArrayBuilder<?>)builder).add(new HalfPrecisionFloat(value));
				break;
			case INT:
				((ArrayBuilder<?>)builder).add(new SinglePrecisionFloat(value));
				break;
			case LONG:
				((ArrayBuilder<?>)builder).add(new DoublePrecisionFloat(value));
				break;
			}
		}
		return this;
	}

	/** @param value will be serialized with 8 bytes.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeDouble (final double value) {
		try {
			return serializeDouble(value, Size.LONG);
		} catch (final SerializationException exception) {
			// Should never happen, LONG is big enough to store a double.
			throw new RuntimeException("Unexpected serialization exception.", exception);
		}
	}

	/** @param value will be serialized.
	 * @param size amount of bytes used to serialize the number. If smaller than actual number size, will truncate.
	 * @throws SerializationException if the size is too small to store this number.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeDouble (final double value, final Size size) throws SerializationException {
		if (builder instanceof CborBuilder) {
			switch (size) {
			case BYTE:
				throw new SerializationException("Byte is too small to store double data. Cannot truncate");
			case SHORT:
				((CborBuilder)builder).add(new HalfPrecisionFloat((float)value));
				break;
			case INT:
				((CborBuilder)builder).add(new SinglePrecisionFloat((float)value));
				break;
			case LONG:
				((CborBuilder)builder).add(new DoublePrecisionFloat(value));
				break;
			}
		} else if (builder instanceof ArrayBuilder) {
			switch (size) {
			case BYTE:
				throw new SerializationException("Byte is too small to store double data. Cannot truncate");
			case SHORT:
				((ArrayBuilder<?>)builder).add(new HalfPrecisionFloat((float)value));
				break;
			case INT:
				((ArrayBuilder<?>)builder).add(new SinglePrecisionFloat((float)value));
				break;
			case LONG:
				((ArrayBuilder<?>)builder).add(new DoublePrecisionFloat(value));
				break;
			}
		}
		return this;
	}

	/** @param value will serialize its ordinal number using 4 bytes.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeEnum (final Enum<?> value) {
		return serializeInt(value.ordinal());
	}

	/** @param value will serialize its ordinal number.
	 * @param enumLengthSize should be able to hold the biggest ordinal number of the passed enum. One byte is enough to serialize
	 *           most enums and in most applications there are hardly ever enough enum constants to surpass two bytes length.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeEnum (final Enum<?> value, final Size enumLengthSize) {
		return serializeInt(value.ordinal(), enumLengthSize);
	}

	/** @param array will be serialized, using 1 byte to store each value and 4 bytes to store array size. Note that more efficient
	 *           implementations need to be custom.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeBooleanArray (final boolean[] array) {
		try {
			return serializeBooleanArray(array, Size.getDefaultArrayLengthSize());
		} catch (final SerializationException exception) {
			// Should never happen. Default array length size is big enough to store any array size.
			throw new RuntimeException("Unexpected exception. Unable to serialize array.", exception);
		}
	}

	/** @param array will be serialized, using 1 byte to store each value. Note that more efficient implementations need to be
	 *           custom.
	 * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
	 *           {@link Size#getMaxArrayLength()} .
	 * @throws SerializationException if array length is longer than the maximum expected array length.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeBooleanArray (final boolean[] array, final Size arrayLengthSize) throws SerializationException {
		startArray();
		for (final boolean value : array) {
			serializeBoolean(value);
		}
		endArray();
		return this;
	}

	/** @param array will be serialized, using 1 byte to store each value and 4 bytes to store array size.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeByteArray (final byte[] array) {
		try {
			return serializeByteArray(array, Size.getDefaultArrayLengthSize());
		} catch (final SerializationException exception) {
			// Should never happen. Default array length size is big enough to store any array size.
			throw new RuntimeException("Unexpected exception. Unable to serialize array.", exception);
		}
	}

	/** @param array will be serialized, using 1 byte to store each value.
	 * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
	 *           {@link Size#getMaxArrayLength()} .
	 * @throws SerializationException if array length is longer than the maximum expected array length.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeByteArray (final byte[] array, final Size arrayLengthSize) throws SerializationException {
		if (builder instanceof CborBuilder) {
			((CborBuilder)builder).add(array);
		} else if (builder instanceof ArrayBuilder) {
			((ArrayBuilder<?>)builder).add(array);
		}
		return this;
	}

	/** @param array will be serialized, using 2 bytes to store each value and 4 bytes to store array size.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeShortArray (final short[] array) {
		try {
			return serializeShortArray(array, Size.getDefaultArrayLengthSize());
		} catch (final SerializationException exception) {
			// Should never happen. Default array length size is big enough to store any array size.
			throw new RuntimeException("Unexpected exception. Unable to serialize array.", exception);
		}
	}

	/** @param array will be serialized, using 2 bytes to store each value.
	 * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
	 *           {@link Size#getMaxArrayLength()} .
	 * @throws SerializationException if array length is longer than the maximum expected array length.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeShortArray (final short[] array, final Size arrayLengthSize) throws SerializationException {
		return serializeShortArray(array, arrayLengthSize, Size.SHORT);
	}

	/** @param array will be serialized.
	 * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
	 *           {@link Size#getMaxArrayLength()} .
	 * @param elementSize amount of bytes used to serialize each array element. If smaller than actual number size, will truncate.
	 *           If bigger than actual number size, will ignore the size and serialize with the actual size needed to store all
	 *           bytes of number data.
	 * @throws SerializationException if array length is longer than the maximum expected array length.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeShortArray (final short[] array, final Size arrayLengthSize, final Size elementSize)
		throws SerializationException {
		startArray();
		for (final short value : array) {
			serializeShort(value, elementSize);
		}
		endArray();
		return this;
	}

	/** @param array will be serialized, using 4 bytes to store each value and 4 bytes to store array size.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeIntArray (final int[] array) {
		try {
			return serializeIntArray(array, Size.getDefaultArrayLengthSize());
		} catch (final SerializationException exception) {
			// Should never happen. Default array length size is big enough to store any array size.
			throw new RuntimeException("Unexpected exception. Unable to serialize array.", exception);
		}
	}

	/** @param array will be serialized, using 4 bytes to store each value.
	 * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
	 *           {@link Size#getMaxArrayLength()} .
	 * @throws SerializationException if array length is longer than the maximum expected array length.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeIntArray (final int[] array, final Size arrayLengthSize) throws SerializationException {
		return serializeIntArray(array, arrayLengthSize, Size.INT);
	}

	/** @param array will be serialized.
	 * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
	 *           {@link Size#getMaxArrayLength()} .
	 * @param elementSize amount of bytes used to serialize each array element. If smaller than actual number size, will truncate.
	 *           If bigger than actual number size, will ignore the size and serialize with the actual size needed to store all
	 *           bytes of number data.
	 * @throws SerializationException if array length is longer than the maximum expected array length.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeIntArray (final int[] array, final Size arrayLengthSize, final Size elementSize)
		throws SerializationException {
		startArray();
		for (final int value : array) {
			serializeInt(value, elementSize);
		}
		endArray();
		return this;
	}

	/** @param array will be serialized, using 8 bytes to store each value and 4 bytes to store array size.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeLongArray (final long[] array) {
		try {
			return serializeLongArray(array, Size.getDefaultArrayLengthSize());
		} catch (final SerializationException exception) {
			// Should never happen. Default array length size is big enough to store any array size.
			throw new RuntimeException("Unexpected exception. Unable to serialize array.", exception);
		}
	}

	/** @param array will be serialized, using 8 bytes to store each value.
	 * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
	 *           {@link Size#getMaxArrayLength()} .
	 * @throws SerializationException if array length is longer than the maximum expected array length.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeLongArray (final long[] array, final Size arrayLengthSize) throws SerializationException {
		return serializeLongArray(array, arrayLengthSize, Size.LONG);
	}

	/** @param array will be serialized.
	 * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
	 *           {@link Size#getMaxArrayLength()} .
	 * @param elementSize amount of bytes used to serialize each array element. If smaller than actual number size, will truncate.
	 * @throws SerializationException if array length is longer than the maximum expected array length.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeLongArray (final long[] array, final Size arrayLengthSize, final Size elementSize)
		throws SerializationException {
		startArray();
		for (final long value : array) {
			serializeLong(value, elementSize);
		}
		endArray();
		return this;
	}

	/** @param array will be serialized, using 4 bytes to store each value and 4 bytes to store array size.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeFloatArray (final float[] array) {
		try {
			return serializeFloatArray(array, Size.getDefaultArrayLengthSize());
		} catch (final SerializationException exception) {
			// Should never happen. Default array length size is big enough to store any array size.
			throw new RuntimeException("Unexpected exception. Unable to serialize array.", exception);
		}
	}

	/** @param array will be serialized, using 4 bytes to store each value.
	 * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
	 *           {@link Size#getMaxArrayLength()} .
	 * @throws SerializationException if array length is longer than the maximum expected array length.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeFloatArray (final float[] array, final Size arrayLengthSize) throws SerializationException {
		return serializeFloatArray(array, arrayLengthSize, Size.INT);
	}

	/** @param array will be serialized.
	 * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
	 *           {@link Size#getMaxArrayLength()} .
	 * @param elementSize amount of bytes used to serialize each array element.
	 * @throws SerializationException if array length is longer than the maximum expected array length or selected element size is
	 *            to small to store the number.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeFloatArray (final float[] array, final Size arrayLengthSize, final Size elementSize)
		throws SerializationException {
		startArray();
		for (final float value : array) {
			serializeFloat(value, elementSize);
		}
		endArray();
		return this;
	}

	/** @param array will be serialized, using 8 bytes to store each value and 4 bytes to store array size.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeDoubleArray (final double[] array) {
		try {
			return serializeDoubleArray(array, Size.getDefaultArrayLengthSize());
		} catch (final SerializationException exception) {
			// Should never happen. Default array length size is big enough to store any array size.
			throw new RuntimeException("Unexpected exception. Unable to serialize array.", exception);
		}
	}

	/** @param array will be serialized, using 8 bytes to store each value.
	 * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
	 *           {@link Size#getMaxArrayLength()} .
	 * @throws SerializationException if array length is longer than the maximum expected array length.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeDoubleArray (final double[] array, final Size arrayLengthSize) throws SerializationException {
		return serializeDoubleArray(array, arrayLengthSize, Size.LONG);
	}

	/** @param array will be serialized.
	 * @param arrayLengthSize amount of bytes used to serialized array length. Array length cannot exceed
	 *           {@link Size#getMaxArrayLength()} .
	 * @param elementSize amount of bytes used to serialize each array element. If smaller than actual number size, will truncate.
	 * @throws SerializationException if array length is longer than the maximum expected array length or selected element size is
	 *            to small to store the number.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeDoubleArray (final double[] array, final Size arrayLengthSize, final Size elementSize)
		throws SerializationException {
		startArray();
		for (final double value : array) {
			serializeDouble(value, elementSize);
		}
		endArray();
		return this;
	}

	/** @param value will be serialized as byte array with 4 bytes to store array length.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeString (final String value) {
		return serializeString(value, null);
	}

	/** @param value will be serialized as byte array.
	 * @param stringLengthSize estimated size needed to store maximum amount of bytes needed to store the string. Each character
	 *           translates roughly to 1 byte.
	 * @throws SerializationException if string's byte array is longer than the estimated max size.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeString (final String value, final Size stringLengthSize) throws SerializationException {
		if (builder instanceof CborBuilder) {
			((CborBuilder)builder).add(value);
		} else if (builder instanceof ArrayBuilder) {
			((ArrayBuilder<?>)builder).add(value);
		}
		return this;
	}

	/** @param array will be serialized as array of byte arrays using 4 bytes to store byte array lengths and 4 bytes to store main
	 *           array length.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeStringArray (final String[] array) {
		try {
			return serializeStringArray(array, Size.getDefaultArrayLengthSize(), Size.getDefaultArrayLengthSize());
		} catch (final SerializationException exception) {
			// Should never happen. Array length sizes should be able to store any string arrays.
			throw new RuntimeException("Unexpected exception. Unable to serialize string array.", exception);
		}
	}

	/** @param array will be serialized as array of byte arrays using 4 bytes to store each byte array length.
	 * @param arrayLengthSize estimated maximum amount of bytes needed to store array's length. Array length cannot exceed
	 *           {@link Size#getMaxArrayLength()}
	 * @throws SerializationException if string's byte array is longer than the estimated max size.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeStringArray (final String[] array, final Size arrayLengthSize) throws SerializationException {
		return serializeStringArray(array, arrayLengthSize, Size.getDefaultArrayLengthSize());
	}

	/** @param array will be serialized as array of byte arrays.
	 * @param arrayLengthSize estimated maximum amount of bytes needed to store array's length. Array length cannot exceed
	 *           {@link Size#getMaxArrayLength()} .
	 * @param stringLengthSize estimated size needed to store maximum amount of bytes needed to store each string. Each character
	 *           translates roughly to 1 byte.
	 * @throws SerializationException if string's byte array is longer than the estimated max size or any of the strings is too
	 *            long for the given string length size.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeStringArray (final String[] array, final Size arrayLengthSize, final Size stringLengthSize)
		throws SerializationException {
		serializeStringArray(array, arrayLengthSize, stringLengthSize, 0, array.length);
		return this;
	}

	/** @param array will be serialized as array of byte arrays.
	 * @param arrayLengthSize estimated maximum amount of bytes needed to store array's length. Array length cannot exceed
	 *           {@link Size#getMaxArrayLength()} .
	 * @param stringLengthSize estimated size needed to store maximum amount of bytes needed to store each string. Each character
	 *           translates roughly to 1 byte.
	 * @param start first index from which strings should be serialized.
	 * @param count amount of strings to serialize.
	 * @throws SerializationException if string's byte array is longer than the estimated max size or any of the strings is too
	 *            long for the given string length size.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeStringArray (final String[] array, final Size arrayLengthSize, final Size stringLengthSize,
		final int start, final int count) throws SerializationException {
		startArray();
		for (int i = start; i < start + count; i++) {
			serializeString(array[i], stringLengthSize);
		}
		endArray();
		return this;
	}

	/** @param transferable will be serialized. Cannot be null.
	 * @throws SerializationException if unable to serialize object or the object is null.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeTransferable (final Transferable<?> transferable) throws SerializationException {
		if (transferable == null) {
			throw new SerializationException("Cannot serialize transferable: null object received.", new NullPointerException());
		}
		startArray();
		transferable.serialize(this);
		endArray();
		return this;
	}

	/** @param transferables will be serialized using 4 bytes to store array length. None of the transferables can be null.
	 * @throws SerializationException if unable to serialize any of the transferables.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeTransferableArray (final Transferable<?>[] transferables) throws SerializationException {
		return serializeTransferableArray(transferables, 0, transferables.length, Size.getDefaultArrayLengthSize());
	}

	/** @param transferables will be serialized. None of the transferables can be null.
	 * @param arrayLengthSize estimated amount of bytes needed to store length of the array.
	 * @throws SerializationException is array length size is too small to store array's length or if unable to serialize any of
	 *            the transferables.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeTransferableArray (final Transferable<?>[] transferables, final Size arrayLengthSize)
		throws SerializationException {
		return serializeTransferableArray(transferables, 0, transferables.length, arrayLengthSize);
	}

	/** @param transferables will be serialized. None of the transferables can be null.
	 * @param start index from which the transferables should be serialized.
	 * @param count amount of transferables to serialize.
	 * @param arrayLengthSize estimated amount of bytes needed to store length of the array.
	 * @throws SerializationException is array length size is too small to store array's length or if unable to serialize any of
	 *            the transferables.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeTransferableArray (final Transferable<?>[] transferables, final int start, final int count,
		final Size arrayLengthSize) throws SerializationException {
		if (transferables == null) {
			if (builder instanceof CborBuilder) {
				((CborBuilder)builder).add(SimpleValue.NULL);
			} else if (builder instanceof ArrayBuilder) {
				((ArrayBuilder<?>)builder).add(SimpleValue.NULL);
			}
			return this;
		}
		final int length = start + count;
		startArray();
		for (int index = start; index < length; index++) {
			serializeTransferable(transferables[index]);
		}
		endArray();
		return this;
	}

	/** @param transferables will be serialized using 4 bytes to store array length. Will use 1 extra byte for each transferable to
	 *           detect nullability.
	 * @throws SerializationException if unable to serialize any of the transferables.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeTransferableArrayWithPossibleNulls (final Transferable<?>[] transferables)
		throws SerializationException {
		return serializeTransferableArrayWithPossibleNulls(transferables, Size.getDefaultArrayLengthSize());
	}

	/** @param transferables will be serialized. Will use 1 extra byte for each transferable to detect nullability.
	 * @param arrayLengthSize estimated amount of bytes needed to store length of the array.
	 * @throws SerializationException is array length size is too small to store array's length or if unable to serialize any of
	 *            the transferables.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeTransferableArrayWithPossibleNulls (final Transferable<?>[] transferables,
		final Size arrayLengthSize) throws SerializationException {
		return serializeTransferableArrayWithPossibleNulls(transferables, 0, transferables.length, arrayLengthSize);
	}

	/** @param transferables will be serialized. Will use 1 extra byte for each transferable to detect nullability.
	 * @param start index from which the transferables should be serialized.
	 * @param count amount of transferables to serialize.
	 * @param arrayLengthSize estimated amount of bytes needed to store length of the array.
	 * @throws SerializationException is array length size is too small to store array's length or if unable to serialize any of
	 *            the transferables.
	 * @return this (for chaining). */
	@Override
	public CborSerializer serializeTransferableArrayWithPossibleNulls (final Transferable<?>[] transferables, final int start,
		final int count, final Size arrayLengthSize) throws SerializationException {
		if (transferables == null) {
			if (builder instanceof CborBuilder) {
				((CborBuilder)builder).add(SimpleValue.NULL);
			} else if (builder instanceof ArrayBuilder) {
				((ArrayBuilder<?>)builder).add(SimpleValue.NULL);
			}
			return this;
		}
		startArray();
		for (int i = start; i < start + count; i++) {
			if (transferables[i] != null) {
				serializeTransferable(transferables[i]);
			} else {
				if (builder instanceof CborBuilder) {
					((CborBuilder)builder).add(SimpleValue.NULL);
				} else if (builder instanceof ArrayBuilder) {
					((ArrayBuilder<?>)builder).add(SimpleValue.NULL);
				}
			}
		}
		endArray();
		return this;
	}

	/** Finishes serialization, returning the object as a byte array.
	 *
	 * @return serialized object as byte array. */
	@Override
	public byte[] serialize () {
		try {
			new CborEncoder(serializedData).encode(((CborBuilder)builder).build());
			return serializedData.toByteArray();
		} catch (CborException e) {
			throw new SerializationException(e);
		}
	}

	/** Finishes serialization, returning the object as a byte array. Contrary to {@link #serialize()}, this method might return
	 * the internal serializer's byte array reference, which might get modified if the serializer is reset and used to serialize
	 * another object. Safe to use if a new instance of Serializer is used for each serialization.
	 *
	 * @return serialized object as byte array. Might be the internal serializer's byte array. */
	@Override
	public byte[] serializeUnsafe () {
		return serialize();
	}

	private void startArray () {
		if (builder instanceof CborBuilder) {
			builder = ((CborBuilder)builder).addArray();
		} else if (builder instanceof ArrayBuilder) {
			builder = ((ArrayBuilder<?>)builder).addArray();
		}
	}

	private void endArray () {
		builder = ((ArrayBuilder<?>)builder).end();
	}
}
