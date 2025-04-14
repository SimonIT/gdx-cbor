
package dev.simonit.gdx.websocket.cbor.serialization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CborDeserializerTest {
	CborDeserializer cborDeserializer = new CborDeserializer();

	@Test
	void testDeserializeBoolean () {
		byte[] data = new byte[] {(byte)0xF5};
		cborDeserializer.setSerializedData(data);
		assertTrue(cborDeserializer.deserializeBoolean());
	}

	@Test
	void testDeserializeByte () {
		byte[] data = new byte[] {0x01};
		cborDeserializer.setSerializedData(data);
		assertEquals(1, cborDeserializer.deserializeByte());
	}

	@Test
	void testDeserializeShort () {
		byte[] data = new byte[] {0x19, 0x7F, (byte)0xFF};
		cborDeserializer.setSerializedData(data);
		assertEquals(32767, cborDeserializer.deserializeShort());
	}

	@Test
	void testDeserializeInt () {
		byte[] data = new byte[] {0x1A, 0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFF};
		cborDeserializer.setSerializedData(data);
		assertEquals(2147483647, cborDeserializer.deserializeInt());
	}

	@Test
	void testDeserializeLong () {
		byte[] data = new byte[] {0x1B, (byte)0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
			(byte)0xFF};
		cborDeserializer.setSerializedData(data);
		assertEquals(9223372036854775807L, cborDeserializer.deserializeLong());
	}

	@Test
	void testDeserializeFloat () {
		byte[] data = new byte[] {(byte)0xFA, (byte)0x7F, (byte)0x80, (byte)0x00, (byte)0x00};
		cborDeserializer.setSerializedData(data);
		assertEquals(Float.intBitsToFloat(2139095040), cborDeserializer.deserializeFloat());
	}

	@Test
	void testDeserializeDouble () {
		byte[] data = new byte[] {(byte)0xFB, (byte)0x3F, (byte)0xF1, (byte)0x99, (byte)0x99, (byte)0x99, (byte)0x99, (byte)0x99,
			(byte)0x9A};
		cborDeserializer.setSerializedData(data);
		assertEquals(1.1, cborDeserializer.deserializeDouble());
	}

	@Test
	void testDeserializeEnum () {
		byte[] data = new byte[] {0x00};
		cborDeserializer.setSerializedData(data);
		assertEquals(SomeEnum.a, cborDeserializer.deserializeEnum(SomeEnum.values()));
	}

	@Test
	void testDeserializeBooleanArray () {
		byte[] data = new byte[] {(byte)0x83, (byte)0xF5, (byte)0xF4, (byte)0xF5};
		cborDeserializer.setSerializedData(data);
		boolean[] result = cborDeserializer.deserializeBooleanArray();
		assertArrayEquals(new boolean[] {true, false, true}, result);
	}

	@Test
	void testDeserializeByteArray () {
		byte[] data = new byte[] {(byte)0x43, 0x01, 0x02, 0x03};
		cborDeserializer.setSerializedData(data);
		byte[] result = cborDeserializer.deserializeByteArray();
		assertArrayEquals(new byte[] {1, 2, 3}, result);
	}

	@Test
	void testDeserializeShortArray () {
		byte[] data = new byte[] {(byte)0x82, 0x19, 0x7F, (byte)0xFF, 0x39, 0x7F, (byte)0xFF};
		cborDeserializer.setSerializedData(data);
		short[] result = cborDeserializer.deserializeShortArray();
		assertArrayEquals(new short[] {Short.MAX_VALUE, Short.MIN_VALUE}, result);
	}

	@Test
	void testDeserializeIntArray () {
		byte[] data = new byte[] {(byte)0x82, 0x1A, 0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFF, 0x3A, 0x7F, (byte)0xFF, (byte)0xFF,
			(byte)0xFF};
		cborDeserializer.setSerializedData(data);
		int[] result = cborDeserializer.deserializeIntArray();
		assertArrayEquals(new int[] {Integer.MAX_VALUE, Integer.MIN_VALUE}, result);
	}

	@Test
	void testDeserializeLongArray () {
		byte[] data = new byte[] {(byte)0x82, (byte)0x1B, (byte)0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
			(byte)0xFF, (byte)0xFF, (byte)0x3B, (byte)0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
			(byte)0xFF};
		cborDeserializer.setSerializedData(data);
		long[] result = cborDeserializer.deserializeLongArray();
		assertArrayEquals(new long[] {Long.MAX_VALUE, Long.MIN_VALUE}, result);
	}

	@Test
	void testDeserializeFloatArray () {
		byte[] data = new byte[] {(byte)0x86, (byte)0xFA, (byte)0x7F, (byte)0x7F, (byte)0xFF, (byte)0xFF, (byte)0xFA, (byte)0x00,
			(byte)0x00, (byte)0x00, (byte)0x01, (byte)0xFA, (byte)0x00, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0xFA, (byte)0x7F,
			(byte)0xC0, (byte)0x00, (byte)0x00, (byte)0xFA, (byte)0x7F, (byte)0x80, (byte)0x00, (byte)0x00, (byte)0xFA, (byte)0xFF,
			(byte)0x80, (byte)0x00, (byte)0x00};
		cborDeserializer.setSerializedData(data);
		float[] result = cborDeserializer.deserializeFloatArray();
		assertArrayEquals(new float[] {Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_NORMAL, Float.NaN, Float.POSITIVE_INFINITY,
			Float.NEGATIVE_INFINITY}, result);
	}

	@Test
	void testDeserializeDoubleArray () {
		byte[] data = new byte[] {(byte)0x86, (byte)0xFB, (byte)0x7F, (byte)0xEF, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF,
			(byte)0xFF, (byte)0xFF, (byte)0xFB, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
			(byte)0x01, (byte)0xFB, (byte)0x00, (byte)0x10, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
			(byte)0xFB, (byte)0x7F, (byte)0xF8, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFB,
			(byte)0x7F, (byte)0xF0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFB, (byte)0xFF,
			(byte)0xF0, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
		cborDeserializer.setSerializedData(data);
		double[] result = cborDeserializer.deserializeDoubleArray();
		assertArrayEquals(new double[] {Double.MAX_VALUE, Double.MIN_VALUE, Double.MIN_NORMAL, Double.NaN, Double.POSITIVE_INFINITY,
			Double.NEGATIVE_INFINITY}, result);
	}

	@Test
	void testDeserializeString () {
		byte[] data = new byte[] {0x66, 0x53, 0x74, 0x72, 0x69, 0x6E, 0x67};
		cborDeserializer.setSerializedData(data);
		assertEquals("String", cborDeserializer.deserializeString());
	}

	@Test
	void testDeserializeStringArray () {
		byte[] data = new byte[] {(byte)0x82, 0x66, 0x53, 0x74, 0x72, 0x69, 0x6E, 0x67, (byte)0x64, 0x54, 0x65, 0x73, 0x74};
		cborDeserializer.setSerializedData(data);
		String[] result = cborDeserializer.deserializeStringArray();
		assertArrayEquals(new String[] {"String", "Test"}, result);
	}

	@Test
	void testDeserializeTransferable () {
		byte[] data = new byte[] {(byte)0x82, 0x07, (byte)0xF5};
		cborDeserializer.setSerializedData(data);
		Ping result = cborDeserializer.deserializeTransferable(new Ping());
		assertEquals(7, result.getValue());
		assertTrue(result.isClient());
	}

	@Test
	void testDeserializeTransferableArray () {
		byte[] data = new byte[] {(byte)0x82, (byte)0x82, 0x07, (byte)0xF5, (byte)0x82, 0x08, (byte)0xF4};
		cborDeserializer.setSerializedData(data);
		Ping[] result = cborDeserializer.deserializeTransferableArray(new Ping(), Ping[]::new);
		assertEquals(7, result[0].getValue());
		assertTrue(result[0].isClient());
		assertEquals(8, result[1].getValue());
		assertFalse(result[1].isClient());
	}

	@Test
	void testDeserializeTransferableArrayWithPossibleNulls () {
		byte[] data = new byte[] {(byte)0x82, (byte)0x82, 0x07, (byte)0xF5, (byte)0xF6};
		cborDeserializer.setSerializedData(data);
		Ping[] result = cborDeserializer.deserializeTransferableArrayWithPossibleNulls(new Ping(), Ping[]::new);
		assertEquals(7, result[0].getValue());
		assertTrue(result[0].isClient());
		assertNull(result[1]);
	}
}
