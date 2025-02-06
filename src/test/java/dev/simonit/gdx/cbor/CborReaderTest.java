
package dev.simonit.gdx.cbor;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class CborReaderTest {
	Json json;
	CborReader cborReader;

	@BeforeEach
	public void setUp () {
		json = new Json();
		cborReader = new CborReader();
		json.setReader(cborReader);
	}

	@Test
	public void testEmptyBytes () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {});
		Object cborValue = json.fromJson(Object.class, inputStream);
		assertNull(cborValue);
	}

	@Test
	public void testEmptyObject () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {(byte)0xA0});
		Object cborValue = json.fromJson(Object.class, inputStream);
		assertNotNull(cborValue);
	}

	@Test
	public void testObject () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
			new byte[] {(byte)0xA1, 0x63, 0x6B, 0x65, 0x79, 0x65, 0x76, 0x61, 0x6C, 0x75, 0x65});
		ObjectMap<String, String> cborValue = json.fromJson(ObjectMap.class, inputStream);
		assertEquals("value", cborValue.get("key"));
	}

	@Test
	public void testEmptyArray () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {(byte)0x80});
		Array<?> cborValue = json.fromJson(Array.class, inputStream);
		assertEquals(0, cborValue.size);
	}

	@Test
	public void testArray () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {(byte)0x81, 0x65, 0x76, 0x61, 0x6C, 0x75, 0x65});
		Array<String> cborValue = json.fromJson(Array.class, inputStream);
		assertEquals(1, cborValue.size);
		assertEquals("value", cborValue.get(0));
	}

	@Test
	public void testString () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {0x66, 0x53, 0x74, 0x72, 0x69, 0x6E, 0x67});
		String cborValue = json.fromJson(String.class, inputStream);
		assertEquals("String", cborValue);
	}

	@Test
	public void testHalf () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {(byte)0xF9, 0x3C, 0x00});
		float cborValue = json.fromJson(float.class, inputStream);
		assertEquals(1.0f, cborValue, 0.0001);
	}

	@Test
	public void testFloat () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {(byte)0xfa, 0x47, (byte)0xc3, 0x50, 0x00});
		float cborValue = json.fromJson(float.class, inputStream);
		assertEquals(100000.0, cborValue, 0.0001);
	}

	@Test
	public void testDouble () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
			new byte[] {(byte)0xFB, 0x40, 0x09, 0x21, (byte)0xFB, 0x54, 0x44, 0x2D, 0x18});
		double cborValue = json.fromJson(double.class, inputStream);
		assertEquals(3.141592653589793, cborValue, 0.0001);
	}

	@Test
	public void testPositiveInt () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {0x18, 0x3A});
		int cborValue = json.fromJson(int.class, inputStream);
		assertEquals(58, cborValue);
	}

	@Test
	public void testNegativeInt () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {0x38, 0x3A});
		int cborValue = json.fromJson(int.class, inputStream);
		assertEquals(-59, cborValue);
	}

	@Test
	public void testLong () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {0x1A, (byte)0x80, 0x00, 0x00, 0x00});
		long cborValue = json.fromJson(long.class, inputStream);
		assertEquals(2147483648L, cborValue);
	}

	@Test
	public void testByteString () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {0x46, 0x53, 0x74, 0x72, 0x69, 0x6E, 0x67});
		byte[] cborValue = json.fromJson(byte[].class, inputStream);
		assertArrayEquals(new byte[] {0x53, 0x74, 0x72, 0x69, 0x6E, 0x67}, cborValue);
	}

	@Test
	public void testBooleanTrue () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {(byte)0xF5});
		boolean cborValue = json.fromJson(boolean.class, inputStream);
		assertTrue(cborValue);
	}

	@Test
	public void testBooleanFalse () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {(byte)0xF4});
		boolean cborValue = json.fromJson(boolean.class, inputStream);
		assertFalse(cborValue);
	}

	@Test
	public void testNull () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {(byte)0xF6});
		Object cborValue = json.fromJson(Object.class, inputStream);
		assertNull(cborValue);
	}

	@Test
	public void testCharFromString () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {0x61, 0x5A});
		char cborValue = json.fromJson(char.class, inputStream);
		assertEquals('Z', cborValue);
	}

	@Test
	public void testCharFromInt () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {0x18, 0x5A});
		char cborValue = json.fromJson(char.class, inputStream);
		assertEquals('Z', cborValue);
	}

	@Test
	public void testMap () {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] {(byte)0xA4, 0x65, 0x76, 0x61, 0x6C, 0x75, 0x65,
			0x63, 0x6B, 0x65, 0x79, (byte)0xF9, 0x3E, 0x00, 0x22, (byte)0xF5, (byte)0xF6, (byte)0xF4, (byte)0xA0});
		assertThrows(SerializationException.class, () -> json.fromJson(ObjectMap.class, inputStream));
// ObjectMap<Object, Object> cborValue =
// assertEquals("key", cborValue.get("value"));
// assertEquals(-3, cborValue.get(1.5f));
// assertNull(cborValue.get(true));
// assertEquals(JsonValue.ValueType.object, ((CborValue) cborValue.get(false)).type());
	}

	@Test
	public void testFromReader () {
		Reader reader = new StringReader(
			"g6JlY2xhc3NxamF2YS5sYW5nLkludGVnZXJldmFsdWX7P/AAAAAAAACiZWNsYXNzcWphdmEubGFuZy5JbnRlZ2VyZXZhbHVl+0AAAAAAAAAAomVjbGFzc3FqYXZhLmxhbmcuSW50ZWdlcmV2YWx1ZftACAAAAAAAAA==");
		Array<Integer> cborValue = json.fromJson(Array.class, reader);
		assertEquals(1, cborValue.get(0));
		assertEquals(2, cborValue.get(1));
		assertEquals(3, cborValue.get(2));
	}

	@Test
	public void testFromString () {
		String base64 = "g6JlY2xhc3NxamF2YS5sYW5nLkludGVnZXJldmFsdWX7P/AAAAAAAACiZWNsYXNzcWphdmEubGFuZy5JbnRlZ2VyZXZhbHVl+0AAAAAAAAAAomVjbGFzc3FqYXZhLmxhbmcuSW50ZWdlcmV2YWx1ZftACAAAAAAAAA==";
		Array<Integer> cborValue = json.fromJson(Array.class, base64);
		assertEquals(1, cborValue.get(0));
		assertEquals(2, cborValue.get(1));
		assertEquals(3, cborValue.get(2));
	}

	@Test
	public void testFromFileHandle () {
		FileHandle file = new FileHandle(CborReaderTest.class.getResource("array1.cbor").getFile());
		Array<Integer> cborValue = json.fromJson(Array.class, file);
		assertEquals(1, cborValue.get(0));
		assertEquals(2, cborValue.get(1));
		assertEquals(3, cborValue.get(2));
	}
}
