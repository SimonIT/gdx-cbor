
package dev.simonit.gdx.websocket.cbor.serialization;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.*;
import com.github.czyzby.websocket.serialization.ArrayProvider;
import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Size;

import java.util.List;

import static co.nstant.in.cbor.model.MajorType.SPECIAL;
import static co.nstant.in.cbor.model.SimpleValueType.NULL;
import static co.nstant.in.cbor.model.SpecialType.SIMPLE_VALUE;

public class CborDeserializer extends Deserializer {
	// Package-private as most deserialization methods are in Size enum.
	List<DataItem> serializedData;
	int currentByteArrayIndex;

	public CborDeserializer () {
		this(null);
	}

	public CborDeserializer (final byte[] serializedData) {
		if (serializedData == null) return;
		try {
			this.serializedData = CborDecoder.decode(serializedData);
		} catch (CborException e) {
			throw new RuntimeException(e);
		}
	}

	/** Resets the deserializer, allowing to use it for another serialized object.
	 *
	 * @param serializedData will replace current serialized object.
	 * @return this, for chaining. */
	public Deserializer setSerializedData (final byte[] serializedData) {
		try {
			this.serializedData = CborDecoder.decode(serializedData);
		} catch (CborException e) {
			throw new RuntimeException(e);
		}
		currentByteArrayIndex = 0;
		return this;
	}

	/** @param currentByteArrayIndex byte index at which the deserializer should read values. Can be set manually if many
	 *           serialized objects are merged into one byte array. */
	public void setCurrentByteArrayIndex (final int currentByteArrayIndex) {
		this.currentByteArrayIndex = currentByteArrayIndex;
	}

	/** @return value deserialized from 1 byte.
	 * @throws SerializationException if too few bytes to deserialize the value. */
	public boolean deserializeBoolean () throws SerializationException {
		return ((SimpleValue)serializedData.get(currentByteArrayIndex++)).getSimpleValueType() == SimpleValueType.TRUE;
	}

	/** @return value deserialized from 1 byte.
	 * @throws SerializationException if too few bytes to deserialize the value. */
	public byte deserializeByte () throws SerializationException {
		switch (serializedData.get(currentByteArrayIndex).getMajorType()) {
		case UNSIGNED_INTEGER:
			return ((UnsignedInteger)serializedData.get(currentByteArrayIndex++)).getValue().byteValueExact();
		case NEGATIVE_INTEGER:
			return ((NegativeInteger)serializedData.get(currentByteArrayIndex++)).getValue().byteValueExact();
		default:
			throw new SerializationException("Invalid major type: " + serializedData.get(currentByteArrayIndex).getMajorType());

		}
	}

	/** @return value deserialized from 2 bytes.
	 * @throws SerializationException if too few bytes to deserialize the value. */
	public short deserializeShort () throws SerializationException {
		return deserializeShort(Size.SHORT);
	}

	/** @param size amount of bytes used to serialize the number. Note that if the number of bytes is bigger than the actual
	 *           number, the actual number of number's bytes will be used instead.
	 * @return deserialized value.
	 * @throws SerializationException if too few bytes to deserialize the value. */
	public short deserializeShort (final Size size) throws SerializationException {
		switch (serializedData.get(currentByteArrayIndex).getMajorType()) {
		case UNSIGNED_INTEGER:
			return ((UnsignedInteger)serializedData.get(currentByteArrayIndex++)).getValue().shortValueExact();
		case NEGATIVE_INTEGER:
			return ((NegativeInteger)serializedData.get(currentByteArrayIndex++)).getValue().shortValueExact();
		default:
			throw new SerializationException("Invalid major type: " + serializedData.get(currentByteArrayIndex).getMajorType());

		}
	}

	/** @return value deserialized from 4 bytes.
	 * @throws SerializationException if too few bytes to deserialize the value. */
	public int deserializeInt () throws SerializationException {
		return deserializeInt(Size.INT);
	}

	/** @param size amount of bytes used to serialize the number. Note that if the number of bytes is bigger than the actual
	 *           number, the actual number of number's bytes will be used instead.
	 * @return deserialized value.
	 * @throws SerializationException if too few bytes to deserialize the value. */
	public int deserializeInt (final Size size) throws SerializationException {
		switch (serializedData.get(currentByteArrayIndex).getMajorType()) {
		case UNSIGNED_INTEGER:
			return ((UnsignedInteger)serializedData.get(currentByteArrayIndex++)).getValue().intValueExact();
		case NEGATIVE_INTEGER:
			return ((NegativeInteger)serializedData.get(currentByteArrayIndex++)).getValue().intValueExact();
		default:
			throw new SerializationException("Invalid major type: " + serializedData.get(currentByteArrayIndex).getMajorType());

		}
	}

	/** @return value deserialized from 8 bytes.
	 * @throws SerializationException if too few bytes to deserialize the value. */
	public long deserializeLong () throws SerializationException {
		return deserializeLong(Size.LONG);
	}

	/** @param size amount of bytes used to serialize the number.
	 * @return deserialized value.
	 * @throws SerializationException if too few bytes to deserialize the value. */
	public long deserializeLong (final Size size) throws SerializationException {
		switch (serializedData.get(currentByteArrayIndex).getMajorType()) {
		case UNSIGNED_INTEGER:
			return ((UnsignedInteger)serializedData.get(currentByteArrayIndex++)).getValue().longValue();
		case NEGATIVE_INTEGER:
			return ((NegativeInteger)serializedData.get(currentByteArrayIndex++)).getValue().longValue();
		default:
			throw new SerializationException("Invalid major type: " + serializedData.get(currentByteArrayIndex).getMajorType());

		}
	}

	/** @return value deserialized from 4 bytes.
	 * @throws SerializationException if too few bytes to deserialize the value. */
	public float deserializeFloat () throws SerializationException {
		return deserializeFloat(Size.INT);
	}

	/** @param size amount of bytes used to serialize the number.
	 * @return deserialized value.
	 * @throws SerializationException if too few bytes to deserialize the value or size too small to store the number. */
	public float deserializeFloat (final Size size) throws SerializationException {
		switch (size) {
		case SHORT:
			return ((HalfPrecisionFloat)serializedData.get(currentByteArrayIndex++)).getValue();
		case INT:
			return ((SinglePrecisionFloat)serializedData.get(currentByteArrayIndex++)).getValue();
		case LONG:
			return (float)((DoublePrecisionFloat)serializedData.get(currentByteArrayIndex++)).getValue();
		default:
			throw new SerializationException("Invalid size: " + size);
		}
	}

	/** @return value deserialized from 8 bytes.
	 * @throws SerializationException if too few bytes to deserialize the value. */
	public double deserializeDouble () throws SerializationException {
		return deserializeDouble(Size.LONG);
	}

	/** @param size amount of bytes used to serialize the number.
	 * @return deserialized value.
	 * @throws SerializationException if too few bytes to deserialize the value or size too small to store the number. */
	public double deserializeDouble (final Size size) throws SerializationException {
		switch (size) {
		case SHORT:
			return ((HalfPrecisionFloat)serializedData.get(currentByteArrayIndex++)).getValue();
		case INT:
			return ((SinglePrecisionFloat)serializedData.get(currentByteArrayIndex++)).getValue();
		case LONG:
			return ((DoublePrecisionFloat)serializedData.get(currentByteArrayIndex++)).getValue();
		default:
			throw new SerializationException("Invalid size: " + size);
		}
	}

	/** @param enumValues all values of the enum. Should be retrieved with {@link java.lang.Enum}.values().
	 * @return enum constant with the ordinal deserialized from 4 bytes.
	 * @param <Type> type of enum.
	 * @throws SerializationException if unable to deserialize enum value. */
	public <Type extends Enum<Type>> Type deserializeEnum (final Type[] enumValues) throws SerializationException {
		return deserializeEnum(enumValues, Size.INT);
	}

	/** @param enumValues all values of the enum. Should be retrieved with {@link java.lang.Enum}.values().
	 * @param enumLengthSize amount of bytes used to serialize enum ordinal. Defaults to 4 (int size).
	 * @return enum constant with the ordinal deserialized from the data.
	 * @param <Type> type of enum.
	 * @throws SerializationException if unable to deserialize enum value. */
	public <Type extends Enum<Type>> Type deserializeEnum (final Type[] enumValues, final Size enumLengthSize)
		throws SerializationException {
		final int ordinal = deserializeInt(enumLengthSize);
		validateEnumOrdinal(ordinal, enumValues);
		return enumValues[ordinal];
	}

	private static void validateEnumOrdinal (final int ordinal, final Enum<?>[] enumValues) throws SerializationException {
		if (ordinal < 0 || enumValues.length <= ordinal) {
			throw new SerializationException("Invalid enum ordinal: " + ordinal);
		}
	}

	/** @return deserialized array of value, with length serialized with 4 bytes and each element serialized with 1 byte.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public boolean[] deserializeBooleanArray () throws SerializationException {
		return deserializeBooleanArray(Size.getDefaultArrayLengthSize());
	}

	/** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @return deserialized array of values, each stored in 1 byte.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public boolean[] deserializeBooleanArray (final Size arrayLengthSize) throws SerializationException {
		Array array = getArray();
		boolean[] booleans = new boolean[array.getDataItems().size()];
		for (int i = 0; i < booleans.length; i++) {
			booleans[i] = ((SimpleValue)array.getDataItems().get(i)).getSimpleValueType() == SimpleValueType.TRUE;
		}
		return booleans;
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeBooleanArray (final boolean[] result) throws SerializationException {
		return deserializeBooleanArray(result, Size.getDefaultArrayLengthSize());
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeBooleanArray (final boolean[] result, final Size arrayLengthSize) throws SerializationException {
		Array array = getArray();
		for (int i = 0; i < array.getDataItems().size(); i++) {
			result[i] = ((SimpleValue)array.getDataItems().get(i)).getSimpleValueType() == SimpleValueType.TRUE;
		}
		return array.getDataItems().size();
	}

	/** @return deserialized array of value, with length serialized with 4 bytes and each element serialized with 1 byte.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public byte[] deserializeByteArray () throws SerializationException {
		return deserializeByteArray(Size.getDefaultArrayLengthSize());
	}

	/** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @return deserialized array of values, each stored in 1 byte.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public byte[] deserializeByteArray (final Size arrayLengthSize) throws SerializationException {
		return ((ByteString)serializedData.get(currentByteArrayIndex++)).getBytes();
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeByteArray (final byte[] result) throws SerializationException {
		return deserializeByteArray(result, Size.getDefaultArrayLengthSize());
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeByteArray (final byte[] result, final Size arrayLengthSize) throws SerializationException {
		byte[] bytes = deserializeByteArray(arrayLengthSize);
		System.arraycopy(bytes, 0, result, 0, bytes.length);
		return bytes.length;
	}

	/** @return deserialized array of value, with length serialized with 4 bytes and each element serialized with 2 bytes.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public short[] deserializeShortArray () throws SerializationException {
		return deserializeShortArray(Size.getDefaultArrayLengthSize());
	}

	/** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @return deserialized array of values, each stored in 2 bytes.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public short[] deserializeShortArray (final Size arrayLengthSize) throws SerializationException {
		return deserializeShortArray(arrayLengthSize, Size.SHORT);
	}

	/** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @param elementSize amount of bytes used to store each value. If bigger than the actual bytes amount of the number, actual
	 *           bytes amount will be used.
	 * @return deserialized array of values.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public short[] deserializeShortArray (final Size arrayLengthSize, final Size elementSize) throws SerializationException {
		Array array = getArray();
		short[] shorts = new short[array.getDataItems().size()];
		for (int i = 0; i < shorts.length; i++) {
			switch (array.getDataItems().get(i).getMajorType()) {
			case UNSIGNED_INTEGER:
				shorts[i] = ((UnsignedInteger)array.getDataItems().get(i)).getValue().shortValueExact();
				break;
			case NEGATIVE_INTEGER:
				shorts[i] = ((NegativeInteger)array.getDataItems().get(i)).getValue().shortValueExact();
				break;
			default:
				throw new SerializationException("Invalid major type: " + array.getDataItems().get(i).getMajorType());
			}
		}
		return shorts;
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeShortArray (final short[] result) throws SerializationException {
		return deserializeShortArray(result, Size.getDefaultArrayLengthSize(), Size.SHORT);
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeShortArray (final short[] result, final Size arrayLengthSize) throws SerializationException {
		return deserializeShortArray(result, arrayLengthSize, Size.SHORT);
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @param elementSize amount of bytes used to store each value. If bigger than the actual bytes amount of the number, actual
	 *           bytes amount will be used.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeShortArray (final short[] result, final Size arrayLengthSize, final Size elementSize)
		throws SerializationException {
		Array array = getArray();
		for (int i = 0; i < array.getDataItems().size(); i++) {
			switch (array.getDataItems().get(i).getMajorType()) {
			case UNSIGNED_INTEGER:
				result[i] = ((UnsignedInteger)array.getDataItems().get(i)).getValue().shortValueExact();
				break;
			case NEGATIVE_INTEGER:
				result[i] = ((NegativeInteger)array.getDataItems().get(i)).getValue().shortValueExact();
				break;
			default:
				throw new SerializationException("Invalid major type: " + array.getDataItems().get(i).getMajorType());
			}
		}
		return array.getDataItems().size();
	}

	/** @return deserialized array of value, with length serialized with 4 bytes and each element serialized with 4 bytes.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public int[] deserializeIntArray () throws SerializationException {
		return deserializeIntArray(Size.getDefaultArrayLengthSize());
	}

	/** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @return deserialized array of values, each stored in 4 bytes.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public int[] deserializeIntArray (final Size arrayLengthSize) throws SerializationException {
		return deserializeIntArray(arrayLengthSize, Size.INT);
	}

	/** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @param elementSize amount of bytes used to store each value. If bigger than the actual bytes amount of the number, actual
	 *           bytes amount will be used.
	 * @return deserialized array of values.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public int[] deserializeIntArray (final Size arrayLengthSize, final Size elementSize) throws SerializationException {
		Array array = getArray();
		int[] ints = new int[array.getDataItems().size()];
		for (int i = 0; i < ints.length; i++) {
			switch (array.getDataItems().get(i).getMajorType()) {
			case UNSIGNED_INTEGER:
				ints[i] = ((UnsignedInteger)array.getDataItems().get(i)).getValue().intValueExact();
				break;
			case NEGATIVE_INTEGER:
				ints[i] = ((NegativeInteger)array.getDataItems().get(i)).getValue().intValueExact();
				break;
			default:
				throw new SerializationException("Invalid major type: " + array.getDataItems().get(i).getMajorType());
			}
		}
		return ints;
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeIntArray (final int[] result) throws SerializationException {
		return deserializeIntArray(result, Size.getDefaultArrayLengthSize(), Size.INT);
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeIntArray (final int[] result, final Size arrayLengthSize) throws SerializationException {
		return deserializeIntArray(result, arrayLengthSize, Size.INT);
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @param elementSize amount of bytes used to store each value. If bigger than the actual bytes amount of the number, actual
	 *           bytes amount will be used.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeIntArray (final int[] result, final Size arrayLengthSize, final Size elementSize)
		throws SerializationException {
		Array array = getArray();
		for (int i = 0; i < array.getDataItems().size(); i++) {
			switch (array.getDataItems().get(i).getMajorType()) {
			case UNSIGNED_INTEGER:
				result[i] = ((UnsignedInteger)array.getDataItems().get(i)).getValue().intValueExact();
				break;
			case NEGATIVE_INTEGER:
				result[i] = ((NegativeInteger)array.getDataItems().get(i)).getValue().intValueExact();
				break;
			default:
				throw new SerializationException("Invalid major type: " + array.getDataItems().get(i).getMajorType());
			}
		}
		return array.getDataItems().size();
	}

	/** @return deserialized array of value, with length serialized with 4 bytes and each element serialized with 8 bytes.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public long[] deserializeLongArray () throws SerializationException {
		return deserializeLongArray(Size.getDefaultArrayLengthSize());
	}

	/** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @return deserialized array of values, each stored in 8 bytes.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public long[] deserializeLongArray (final Size arrayLengthSize) throws SerializationException {
		return deserializeLongArray(arrayLengthSize, Size.LONG);
	}

	/** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @param elementSize amount of bytes used to store each value.
	 * @return deserialized array of values.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public long[] deserializeLongArray (final Size arrayLengthSize, final Size elementSize) throws SerializationException {
		Array array = getArray();
		long[] longs = new long[array.getDataItems().size()];
		for (int i = 0; i < longs.length; i++) {
			switch (array.getDataItems().get(i).getMajorType()) {
			case UNSIGNED_INTEGER:
				longs[i] = ((UnsignedInteger)array.getDataItems().get(i)).getValue().longValueExact();
				break;
			case NEGATIVE_INTEGER:
				longs[i] = ((NegativeInteger)array.getDataItems().get(i)).getValue().longValueExact();
				break;
			default:
				throw new SerializationException("Invalid major type: " + array.getDataItems().get(i).getMajorType());
			}
		}
		return longs;
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeLongArray (final long[] result) throws SerializationException {
		return deserializeLongArray(result, Size.getDefaultArrayLengthSize(), Size.LONG);
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeLongArray (final long[] result, final Size arrayLengthSize) throws SerializationException {
		return deserializeLongArray(result, arrayLengthSize, Size.LONG);
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @param elementSize amount of bytes used to store each value. If bigger than the actual bytes amount of the number, actual
	 *           bytes amount will be used.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeLongArray (final long[] result, final Size arrayLengthSize, final Size elementSize)
		throws SerializationException {
		Array array = getArray();
		for (int i = 0; i < array.getDataItems().size(); i++) {
			switch (array.getDataItems().get(i).getMajorType()) {
			case UNSIGNED_INTEGER:
				result[i] = ((UnsignedInteger)array.getDataItems().get(i)).getValue().longValueExact();
				break;
			case NEGATIVE_INTEGER:
				result[i] = ((NegativeInteger)array.getDataItems().get(i)).getValue().longValueExact();
				break;
			default:
				throw new SerializationException("Invalid major type: " + array.getDataItems().get(i).getMajorType());
			}
		}
		return array.getDataItems().size();
	}

	private Array getArray () {
		return (Array)serializedData.get(currentByteArrayIndex++);
	}

	/** @return deserialized array of value, with length serialized with 4 bytes and each element serialized with 4 bytes.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public float[] deserializeFloatArray () throws SerializationException {
		return deserializeFloatArray(Size.getDefaultArrayLengthSize());
	}

	/** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @return deserialized array of values, each stored in 4 bytes.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public float[] deserializeFloatArray (final Size arrayLengthSize) throws SerializationException {
		return deserializeFloatArray(arrayLengthSize, Size.INT);
	}

	/** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @param elementSize amount of bytes used to store each value.
	 * @return deserialized array of values.
	 * @throws SerializationException if too few bytes to deserialize the array or element size is to small to store the number. */
	public float[] deserializeFloatArray (final Size arrayLengthSize, final Size elementSize) throws SerializationException {
		Array array = getArray();
		float[] floats = new float[array.getDataItems().size()];
		for (int i = 0; i < array.getDataItems().size(); i++) {
			switch (elementSize) {
			case SHORT:
				floats[i] = ((HalfPrecisionFloat)array.getDataItems().get(i)).getValue();
				break;
			case INT:
				floats[i] = ((SinglePrecisionFloat)array.getDataItems().get(i)).getValue();
				break;
			case LONG:
				floats[i] = (float)((DoublePrecisionFloat)array.getDataItems().get(i)).getValue();
				break;
			default:
				throw new SerializationException("Invalid size: " + elementSize);
			}
		}
		return floats;
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeFloatArray (final float[] result) throws SerializationException {
		return deserializeFloatArray(result, Size.getDefaultArrayLengthSize(), Size.INT);
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeFloatArray (final float[] result, final Size arrayLengthSize) throws SerializationException {
		return deserializeFloatArray(result, arrayLengthSize, Size.INT);
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @param elementSize amount of bytes used to store each value. If bigger than the actual bytes amount of the number, actual
	 *           bytes amount will be used.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeFloatArray (final float[] result, final Size arrayLengthSize, final Size elementSize)
		throws SerializationException {
		Array array = getArray();
		for (int i = 0; i < array.getDataItems().size(); i++) {
			switch (elementSize) {
			case SHORT:
				result[i] = ((HalfPrecisionFloat)array.getDataItems().get(i)).getValue();
				break;
			case INT:
				result[i] = ((SinglePrecisionFloat)array.getDataItems().get(i)).getValue();
				break;
			case LONG:
				result[i] = (float)((DoublePrecisionFloat)array.getDataItems().get(i)).getValue();
				break;
			default:
				throw new SerializationException("Invalid size: " + elementSize);
			}
		}
		return array.getDataItems().size();
	}

	/** @return deserialized array of value, with length serialized with 4 bytes and each element serialized with 8 bytes.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public double[] deserializeDoubleArray () throws SerializationException {
		return deserializeDoubleArray(Size.getDefaultArrayLengthSize());
	}

	/** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @return deserialized array of values, each stored in 8 bytes.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public double[] deserializeDoubleArray (final Size arrayLengthSize) throws SerializationException {
		return deserializeDoubleArray(arrayLengthSize, Size.LONG);
	}

	/** @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @param elementSize amount of bytes used to store each value.
	 * @return deserialized array of values.
	 * @throws SerializationException if too few bytes to deserialize the array or element size is to small to store the number. */
	public double[] deserializeDoubleArray (final Size arrayLengthSize, final Size elementSize) throws SerializationException {
		Array array = getArray();
		double[] doubles = new double[array.getDataItems().size()];
		for (int i = 0; i < array.getDataItems().size(); i++) {
			switch (elementSize) {
			case SHORT:
				doubles[i] = ((HalfPrecisionFloat)array.getDataItems().get(i)).getValue();
				break;
			case INT:
				doubles[i] = ((SinglePrecisionFloat)array.getDataItems().get(i)).getValue();
				break;
			case LONG:
				doubles[i] = ((DoublePrecisionFloat)array.getDataItems().get(i)).getValue();
				break;
			default:
				throw new SerializationException("Invalid size: " + elementSize);
			}
		}
		return doubles;
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeDoubleArray (final double[] result) throws SerializationException {
		return deserializeDoubleArray(result, Size.getDefaultArrayLengthSize(), Size.LONG);
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeDoubleArray (final double[] result, final Size arrayLengthSize) throws SerializationException {
		return deserializeDoubleArray(result, arrayLengthSize, Size.LONG);
	}

	/** @param result cached array. Will be filled with deserialized values; note that values above the original serialized index
	 *           are not changed. This means that if serialized array had 3 element and result array's length is 16, only first 3
	 *           values will be changed.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array's length.
	 * @param elementSize amount of bytes used to store each value. If bigger than the actual bytes amount of the number, actual
	 *           bytes amount will be used.
	 * @return length of the deserialized array. 0 if the serialized array was empty or null.
	 * @throws SerializationException if unable to deserialize array or the passed result array was too small. */
	public int deserializeDoubleArray (final double[] result, final Size arrayLengthSize, final Size elementSize)
		throws SerializationException {
		Array array = getArray();
		for (int i = 0; i < array.getDataItems().size(); i++) {
			switch (elementSize) {
			case SHORT:
				result[i] = ((HalfPrecisionFloat)array.getDataItems().get(i)).getValue();
				break;
			case INT:
				result[i] = ((SinglePrecisionFloat)array.getDataItems().get(i)).getValue();
				break;
			case LONG:
				result[i] = ((DoublePrecisionFloat)array.getDataItems().get(i)).getValue();
				break;
			default:
				throw new SerializationException("Invalid size: " + elementSize);
			}
		}
		return array.getDataItems().size();
	}

	/** @return string deserialized from byte array, with 4 bytes used to deserialize array length.
	 * @throws SerializationException if too few bytes to deserialize the string. */
	public String deserializeString () throws SerializationException {
		return deserializeString(Size.getDefaultArrayLengthSize());
	}

	/** @param stringLengthSize estimated amount of bytes needed to serialize length of string's byte array. Each character
	 *           translates roughly to 1 byte.
	 * @return string deserialized from byte array.
	 * @throws SerializationException if too few bytes to deserialize the string. */
	public String deserializeString (final Size stringLengthSize) throws SerializationException {
		return ((UnicodeString)serializedData.get(currentByteArrayIndex++)).getString();
	}

	/** @return string array deserialized from array of byte arrays, using 4 bytes to determine main array length and 4 bytes for
	 *         each byte array length.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public String[] deserializeStringArray () throws SerializationException {
		return deserializeStringArray(Size.getDefaultArrayLengthSize(), Size.getDefaultArrayLengthSize());
	}

	/** @param arrayLengthSize estimated amount of bytes needed to serialize array length.
	 * @return string array deserialized from array of byte arrays, using 4 bytes for each byte array length.
	 * @throws SerializationException if too few bytes to deserialize the array. */
	public String[] deserializeStringArray (final Size arrayLengthSize) throws SerializationException {
		return deserializeStringArray(arrayLengthSize, Size.getDefaultArrayLengthSize());
	}

	/** @param arrayLengthSize estimated amount of bytes needed to serialize array length.
	 * @param stringLengthSize estimated amount of bytes needed to serialize length of each string's byte array. Each character
	 *           translates roughly to 1 byte.
	 * @return deserialized string array.
	 * @throws SerializationException if too few bytes to deserialize the array; */
	public String[] deserializeStringArray (final Size arrayLengthSize, final Size stringLengthSize)
		throws SerializationException {
		Array array = getArray();
		String[] strings = new String[array.getDataItems().size()];
		for (int i = 0; i < strings.length; i++) {
			strings[i] = ((UnicodeString)array.getDataItems().get(i)).getString();
		}
		return strings;
	}

	/** @param result will contain the deserialized strings. If this array turns out to be longer than the serialized array, all
	 *           the values above original array max index will be set as null.
	 * @return length of the deserialized array. 0 if empty or null.
	 * @throws SerializationException if unable to deserialize of the passed array was too small. */
	public int deserializeStringArray (final String[] result) throws SerializationException {
		return deserializeStringArray(result, Size.getDefaultArrayLengthSize(), Size.getDefaultArrayLengthSize());
	}

	/** @param result will contain the deserialized strings. If this array turns out to be longer than the serialized array, all
	 *           the values above original array max index will be set as null.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array length.
	 * @return length of the deserialized array. 0 if empty or null.
	 * @throws SerializationException if unable to deserialize of the passed array was too small. */
	public int deserializeStringArray (final String[] result, final Size arrayLengthSize) throws SerializationException {
		return deserializeStringArray(result, arrayLengthSize, Size.getDefaultArrayLengthSize());
	}

	/** @param result will contain the deserialized strings. If this array turns out to be longer than the serialized array, all
	 *           the values above original array max index will be set as null.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array length.
	 * @param stringLengthSize estimated amount of bytes needed to serialize length of each string's byte array. Each character
	 *           translates roughly to 1 byte.
	 * @return length of the deserialized array. 0 if empty or null.
	 * @throws SerializationException if unable to deserialize of the passed array was too small. */
	public int deserializeStringArray (final String[] result, final Size arrayLengthSize, final Size stringLengthSize)
		throws SerializationException {
		Array array = getArray();
		for (int i = 0; i < array.getDataItems().size(); i++) {
			result[i] = ((UnicodeString)array.getDataItems().get(i)).getString();
		}
		return array.getDataItems().size();
	}

	/** @param transferable example instance of transferable, used to invoke deserialization method.
	 * @return new instance of transferable deserialized with this deserializer.
	 * @throws SerializationException if unable to deserialize transferable.
	 * @param <Type> type of transferable. */
	public <Type extends Transferable<Type>> Type deserializeTransferable (final Transferable<Type> transferable)
		throws SerializationException {
		List<DataItem> previousDataItems = serializedData;
		Array array = getArray();
		int previousCurrentByteArrayIndex = currentByteArrayIndex;
		currentByteArrayIndex = 0;
		serializedData = array.getDataItems();
		Type t = transferable.deserialize(this);
		serializedData = previousDataItems;
		currentByteArrayIndex = previousCurrentByteArrayIndex;
		return t;
	}

	/** @param transferable example instance of transferable, used to invoke deserialization method.
	 * @param arrayProvider provides typed arrays. Utility class which allows to return something more specific than Object or
	 *           Transferable array.
	 * @return deserialized transferables array, using 4 bytes to determine array length.
	 * @param <Type> type of transferable.
	 * @throws SerializationException if too few bytes to deserialize array or unable to deserialize any of its elements. */
	public <Type extends Transferable<Type>> Type[] deserializeTransferableArray (final Transferable<Type> transferable,
		final ArrayProvider<Type> arrayProvider) throws SerializationException {
		return deserializeTransferableArray(transferable, arrayProvider, Size.getDefaultArrayLengthSize());
	}

	/** @param transferable example instance of transferable, used to invoke deserialization method.
	 * @param arrayProvider provides typed arrays. Utility class which allows to return something more specific than Object or
	 *           Transferable array.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array length.
	 * @return deserialized transferables array.
	 * @param <Type> type of transferable.
	 * @throws SerializationException if too few bytes to deserialize array or unable to deserialize any of its elements. */
	public <Type extends Transferable<Type>> Type[] deserializeTransferableArray (final Transferable<Type> transferable,
		final ArrayProvider<Type> arrayProvider, final Size arrayLengthSize) throws SerializationException {
		Array array = getArray();
		final int arraySize = array.getDataItems().size();
		final Type[] transferables = arrayProvider.getArray(arraySize);
		if (arraySize == 0) {
			return transferables;
		}
		List<DataItem> previousDataItems = serializedData;
		int previousCurrentByteArrayIndex = currentByteArrayIndex;
		serializedData = array.getDataItems();
		for (int i = 0; i < arraySize; i++) {
			currentByteArrayIndex = i;
			transferables[i] = deserializeTransferable(transferable);
		}
		serializedData = previousDataItems;
		currentByteArrayIndex = previousCurrentByteArrayIndex;
		return transferables;
	}

	/** @param result will contain the deserialized transferables. If this array turns out to be longer than the serialized array,
	 *           all the values above original array max index will be set as null.
	 * @param transferable example instance of transferable, used to invoke deserialization method.
	 * @return length of the deserialized array. 0 if empty or null.
	 * @throws SerializationException if unable to deserialize of the passed array was too small. */
	public int deserializeTransferableArray (final Transferable<?>[] result, final Transferable<?> transferable)
		throws SerializationException {
		return deserializeTransferableArray(result, transferable, Size.getDefaultArrayLengthSize());
	}

	/** @param result will contain the deserialized transferables. If this array turns out to be longer than the serialized array,
	 *           all the values above original array max index will be set as null.
	 * @param transferable example instance of transferable, used to invoke deserialization method.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array length.
	 * @return length of the deserialized array. 0 if empty or null.
	 * @throws SerializationException if unable to deserialize of the passed array was too small. */
	public int deserializeTransferableArray (final Transferable<?>[] result, final Transferable<?> transferable,
		final Size arrayLengthSize) throws SerializationException {
		Array array = getArray();
		final int arraySize = array.getDataItems().size();
		if (arraySize == 0) {
			return arraySize;
		}
		for (int index = 0; index < arraySize; index++) {
			result[index] = deserializeTransferable(transferable);
		}
		return arraySize;
	}

	/** @param transferable example instance of transferable, used to invoke deserialization method.
	 * @param arrayProvider provides typed arrays. Utility class which allows to return something more specific than Object or
	 *           Transferable array.
	 * @return deserialized transferables array, using 4 bytes to determine array length.
	 * @param <Type> type of transferable.
	 * @throws SerializationException if too few bytes to deserialize array or unable to deserialize any of its elements. */
	public <Type extends Transferable<Type>> Type[] deserializeTransferableArrayWithPossibleNulls (
		final Transferable<Type> transferable, final ArrayProvider<Type> arrayProvider) throws SerializationException {
		return deserializeTransferableArrayWithPossibleNulls(transferable, arrayProvider, Size.getDefaultArrayLengthSize());
	}

	/** @param transferable example instance of transferable, used to invoke deserialization method.
	 * @param arrayProvider provides typed arrays. Utility class which allows to return something more specific than Object or
	 *           Transferable array.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array length.
	 * @return deserialized transferables array.
	 * @param <Type> type of transferable.
	 * @throws SerializationException if too few bytes to deserialize array or unable to deserialize any of its elements. */
	public <Type extends Transferable<Type>> Type[] deserializeTransferableArrayWithPossibleNulls (
		final Transferable<Type> transferable, final ArrayProvider<Type> arrayProvider, final Size arrayLengthSize)
		throws SerializationException {
		Array array = getArray();
		final int arraySize = array.getDataItems().size();
		final Type[] transferables = arrayProvider.getArray(arraySize);
		if (arraySize == 0) {
			return transferables;
		}
		List<DataItem> previousDataItems = serializedData;
		int previousCurrentByteArrayIndex = currentByteArrayIndex;
		serializedData = array.getDataItems();
		for (int index = 0; index < arraySize; index++) {
			DataItem dataItem = array.getDataItems().get(index);
			if (dataItem.getMajorType() == SPECIAL && ((Special)dataItem).getSpecialType() == SIMPLE_VALUE
				&& ((SimpleValue)dataItem).getSimpleValueType() == NULL) {
				transferables[index] = null;
			} else {
				currentByteArrayIndex = index;
				transferables[index] = deserializeTransferable(transferable);
			}
		}
		serializedData = previousDataItems;
		currentByteArrayIndex = previousCurrentByteArrayIndex;
		return transferables;
	}

	/** @param result will contain the deserialized transferables. If this array turns out to be longer than the serialized array,
	 *           all the values above original array max index will be set as null.
	 * @param transferable example instance of transferable, used to invoke deserialization method.
	 * @return length of the deserialized array. 0 if empty or null.
	 * @throws SerializationException if unable to deserialize of the passed array was too small. */
	public int deserializeTransferableArrayWithPossibleNulls (final Transferable<?>[] result, final Transferable<?> transferable)
		throws SerializationException {
		return deserializeTransferableArrayWithPossibleNulls(result, transferable, Size.getDefaultArrayLengthSize());
	}

	/** @param result will contain the deserialized transferables. If this array turns out to be longer than the serialized array,
	 *           all the values above original array max index will be set as null.
	 * @param transferable example instance of transferable, used to invoke deserialization method.
	 * @param arrayLengthSize estimated amount of bytes needed to serialize array length.
	 * @return length of the deserialized array. 0 if empty or null.
	 * @throws SerializationException if unable to deserialize of the passed array was too small. */
	public int deserializeTransferableArrayWithPossibleNulls (final Transferable<?>[] result, final Transferable<?> transferable,
		final Size arrayLengthSize) throws SerializationException {
		Array array = getArray();
		final int arraySize = array.getDataItems().size();
		if (arraySize == 0) {
			return 0;
		}
		List<DataItem> previousDataItems = serializedData;
		int previousCurrentByteArrayIndex = currentByteArrayIndex;
		serializedData = array.getDataItems();
		for (int index = 0; index < arraySize; index++) {
			DataItem dataItem = array.getDataItems().get(index);
			if (dataItem.getMajorType() == SPECIAL && ((Special)dataItem).getSpecialType() == SIMPLE_VALUE
				&& ((SimpleValue)dataItem).getSimpleValueType() == NULL) {
				result[index] = null;
			} else {
				currentByteArrayIndex = index;
				result[index] = deserializeTransferable(transferable);
			}
		}
		serializedData = previousDataItems;
		currentByteArrayIndex = previousCurrentByteArrayIndex;
		return arraySize;
	}
}
