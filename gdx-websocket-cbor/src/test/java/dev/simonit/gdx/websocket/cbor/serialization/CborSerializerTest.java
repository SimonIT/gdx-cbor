
package dev.simonit.gdx.websocket.cbor.serialization;

import com.github.czyzby.websocket.serialization.impl.Size;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CborSerializerTest {

	CborSerializer cborSerializer = new CborSerializer();

	@AfterEach
	void reset () {
		cborSerializer.reset();
	}

	@Test
	void testSerializeBoolean () {
		boolean value = true;
		cborSerializer.serializeBoolean(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {(byte)0xF5}, serialized);
	}

	@Test
	void testSerializeByte () {
		byte value = 1;
		cborSerializer.serializeByte(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {0x01}, serialized);
	}

	@Test
	void testSerializeShort () {
		short value = Short.MAX_VALUE;
		cborSerializer.serializeShort(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {0x19, 0x7F, (byte)0xFF}, serialized);
	}

	@Test
	void testSerializeInt () {
		int value = Integer.MAX_VALUE;
		cborSerializer.serializeInt(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {0x1A, 0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFF}, serialized);
	}

	@Test
	void testSerializeLong () {
		long value = Long.MAX_VALUE;
		cborSerializer.serializeLong(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(
			new byte[] {0x1B, (byte)0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF},
			serialized);
	}

	@Test
	void testSerializeFloatShort () {
		float value = 1.0f;
		cborSerializer.serializeFloat(value, Size.SHORT);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {(byte)0xF9, 0x3C, 0x00}, serialized);
	}

	@Test
	void testSerializeFloatInt () {
		float value = 100000.0f;
		cborSerializer.serializeFloat(value, Size.INT);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {(byte)0xfa, 0x47, (byte)0xc3, 0x50, 0x00}, serialized);
	}

	@Test
	void testSerializeFloatLong () {
		float value = 1.1f;
		cborSerializer.serializeFloat(value, Size.LONG);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(
			new byte[] {(byte)0xfb, 0x3f, (byte)0xf1, (byte)0x99, (byte)0x99, (byte)0xA0, (byte)0x00, (byte)0x00, (byte)0x00},
			serialized);
	}

	@Test
	void testSerializeDouble () {
		double value = 3.141592653589793;
		cborSerializer.serializeDouble(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {(byte)0xfb, 0x40, 0x09, 0x21, (byte)0xfb, 0x54, 0x44, 0x2D, 0x18}, serialized);
	}

	@Test
	void testSerializeEnum () {
		Enum<?> value = SomeEnum.b;
		cborSerializer.serializeEnum(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {0x01}, serialized);
	}

	@Test
	void testSerializeBooleanArray () {
		boolean[] value = {true, false, true};
		cborSerializer.serializeBooleanArray(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {(byte)0x83, (byte)0xF5, (byte)0xF4, (byte)0xF5}, serialized);
	}

	@Test
	void testSerializeByteArray () {
		byte[] value = {1, 2, 3};
		cborSerializer.serializeByteArray(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {(byte)0x43, 0x01, 0x02, 0x03}, serialized);
	}

	@Test
	void testSerializeShortArray () {
		short[] value = {Short.MAX_VALUE, Short.MIN_VALUE};
		cborSerializer.serializeShortArray(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {(byte)0x82, 0x19, 0x7F, (byte)0xFF, 0x39, 0x7F, (byte)0xFF}, serialized);
	}

	@Test
	void testSerializeIntArray () {
		int[] value = {Integer.MAX_VALUE, Integer.MIN_VALUE};
		cborSerializer.serializeIntArray(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(
			new byte[] {(byte)0x82, 0x1a, 0x7f, (byte)0xff, (byte)0xff, (byte)0xff, 0x3a, 0x7f, (byte)0xff, (byte)0xff, (byte)0xff},
			serialized);
	}

	@Test
	void testSerializeLongArray () {
		long[] value = {Long.MAX_VALUE, Long.MIN_VALUE};
		cborSerializer.serializeLongArray(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {(byte)0x82, (byte)0x1B, (byte)0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
			(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x3B, (byte)0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
			(byte)0xFF, (byte)0xFF}, serialized);
	}

	@Test
	void testSerializeFloatArray () {
		float[] value = {Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_NORMAL, Float.NaN, Float.POSITIVE_INFINITY,
			Float.NEGATIVE_INFINITY};
		cborSerializer.serializeFloatArray(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {(byte)0x86, (byte)0xFA, (byte)0x7F, (byte)0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFA,
			(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0xFA, (byte)0x00, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0xFA,
			(byte)0x7F, (byte)0xC0, (byte)0x00, (byte)0x00, (byte)0xFA, (byte)0x7F, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0xFA,
			(byte)0xFF, (byte)0x80, (byte)0x00, (byte)0x00}, serialized);
	}

	@Test
	void testSerializeDoubleArray () {
		double[] value = {Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_NORMAL, Double.NaN, Double.POSITIVE_INFINITY,
			Double.NEGATIVE_INFINITY};
		cborSerializer.serializeDoubleArray(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {(byte)0x86, (byte)0xFB, (byte)0x7F, (byte)0xEF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
			(byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFB, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
			(byte)0x00, (byte)0x01, (byte)0xFB, (byte)0x00, (byte)0x10, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
			(byte)0x00, (byte)0xFB, (byte)0x7F, (byte)0xF8, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
			(byte)0xFB, (byte)0x7F, (byte)0xF0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFB,
			(byte)0xFF, (byte)0xF0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00}, serialized);
	}

	@Test
	void testSerializeString () {
		String value = "Hello, World!";
		cborSerializer.serializeString(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {0x6D, 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x2C, 0x20, 0x57, 0x6F, 0x72, 0x6C, 0x64, 0x21},
			serialized);
	}

	@Test
	void testSerializeStringArray () {
		String[] value = {"Hello", "World"};
		cborSerializer.serializeStringArray(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {(byte)0x82, 0x65, 0x48, 0x65, 0x6C, 0x6C, 0x6F, 0x65, 0x57, 0x6F, 0x72, 0x6C, 0x64},
			serialized);
	}

	@Test
	void testSerializeTransferable () {
		Ping value = new Ping(7, true);
		cborSerializer.serializeTransferable(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {(byte)0x82, 0x07, (byte)0xF5}, serialized);
	}

	@Test
	void testSerializeTransferableArray () {
		Ping[] value = {new Ping(7, true), new Ping(8, false)};
		cborSerializer.serializeTransferableArray(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {(byte)0x82, (byte)0x82, 0x07, (byte)0xF5, (byte)0x82, 0x08, (byte)0xF4}, serialized);
	}

	@Test
	void testSerializeTransferableArrayWithPossibleNulls () {
		Ping[] value = {new Ping(7, true), null};
		cborSerializer.serializeTransferableArrayWithPossibleNulls(value);
		byte[] serialized = cborSerializer.serialize();
		assertArrayEquals(new byte[] {(byte)0x82, (byte)0x82, 0x07, (byte)0xF5, (byte)0xF6}, serialized);
	}
}
