
package dev.simonit.gdx.cbor;

import com.badlogic.gdx.utils.JsonWriter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class CborWriterTest {
	Cbor json = new Cbor();

	@Test
	public void testWriteObject () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeObjectStart();
		json.writeValue("key", "value");
		json.writeObjectEnd();
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {(byte)0xA1, 0x63, 0x6B, 0x65, 0x79, 0x65, 0x76, 0x61, 0x6C, 0x75, 0x65}, bytes);
	}

	@Test
	public void testWriteStringArray () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue("value");
		json.writeArrayEnd();
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {(byte)0x81, 0x65, 0x76, 0x61, 0x6C, 0x75, 0x65}, bytes);
	}

	@Test
	public void testWriteNullArray () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue(null);
		json.writeArrayEnd();
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {(byte)0x81, (byte)0xF6}, bytes);
	}

	@Test
	public void testWriteFloatArray () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue(100000.0F);
		json.writeArrayEnd();
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {(byte)0x81, (byte)0xfa, 0x47, (byte)0xc3, 0x50, 0x00}, bytes);
	}

	@Test
	public void testWriteDoubleArray () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue(3.141592653589793);
		json.writeArrayEnd();
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {(byte)0x81, (byte)0xFB, 0x40, 0x09, 0x21, (byte)0xFB, 0x54, 0x44, 0x2D, 0x18}, bytes);
	}

	@Test
	public void testWriteIntArray () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue(58);
		json.writeArrayEnd();
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {(byte)0x81, 0x18, 0x3A}, bytes);
	}

	@Test
	public void testWriteLongArray () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue(2147483648L);
		json.writeArrayEnd();
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {(byte)0x81, 0x1A, (byte)0x80, 0x00, 0x00, 0x00}, bytes);
	}

	@Test
	public void testWriteBooleanArray () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue(true);
		json.writeArrayEnd();
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {(byte)0x81, (byte)0xF5}, bytes);
	}

	@Test
	public void testWriteCharArray () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue('Z');
		json.writeArrayEnd();
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {(byte)0x81, 0x61, 0x5A}, bytes);
	}

	@Test
	public void testWriteArrayArray () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeArrayStart();
		json.writeArrayEnd();
		json.writeArrayEnd();
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {(byte)0x81, (byte)0x80}, bytes);
	}

	@Test
	public void testWriteStringValue () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue("value");
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {0x65, 0x76, 0x61, 0x6C, 0x75, 0x65}, bytes);
	}

	@Test
	public void testWriteDoubleValue () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue(3.141592653589793);
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {(byte)0xFB, 0x40, 0x09, 0x21, (byte)0xFB, 0x54, 0x44, 0x2D, 0x18}, bytes);
	}

	@Test
	public void testWriteLongValue () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue(2147483648L);
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {0x1A, (byte)0x80, 0x00, 0x00, 0x00}, bytes);
	}

	@Test
	public void testWriteBooleanValue () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue(true);
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {(byte)0xF5}, bytes);
	}

	@Test
	public void testWriteNullValue () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue(null);
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {(byte)0xF6}, bytes);
	}

	@Test
	public void testWriteFloatValue () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue(100000.0F);
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {(byte)0xfa, 0x47, (byte)0xc3, 0x50, 0x00}, bytes);
	}

	@Test
	public void testWriteIntValue () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue(58);
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {0x18, 0x3A}, bytes);
	}

	@Test
	public void testWriteCharAsString () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue('Z');
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {0x61, 0x5A}, bytes);
	}

	@Test
	public void testWriteCharAsNumber () {
		((CborWriter)json.getWriter()).writeCharAsString(false);
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue('Z');
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {0x18, 0x5A}, bytes);
	}

	@Test
	public void testPop () {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeObjectStart();
		json.writeValue("key", "value");
		byte[] bytes = json.toCbor();
		assertArrayEquals(new byte[] {(byte)0xA1, 0x63, 0x6B, 0x65, 0x79, 0x65, 0x76, 0x61, 0x6C, 0x75, 0x65}, bytes);
	}
}
