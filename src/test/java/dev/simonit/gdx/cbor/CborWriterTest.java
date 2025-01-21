
package dev.simonit.gdx.cbor;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class CborWriterTest {
	Json json;
	CborWriter cborWriter;
	ByteArrayOutputStream outputStream;

	@BeforeEach
	public void setUp () {
		outputStream = new ByteArrayOutputStream();
		json = new Json();
		cborWriter = new CborWriter(outputStream);
		json.setWriter(cborWriter);
	}

	@Test
	public void testWriteObject () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeObjectStart();
		json.writeValue("key", "value");
		json.writeObjectEnd();
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {(byte)0xA1, 0x63, 0x6B, 0x65, 0x79, 0x65, 0x76, 0x61, 0x6C, 0x75, 0x65}, bytes);
	}

	@Test
	public void testWriteStringArray () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue("value");
		json.writeArrayEnd();
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {(byte)0x81, 0x65, 0x76, 0x61, 0x6C, 0x75, 0x65}, bytes);
	}

	@Test
	public void testWriteNullArray () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue(null);
		json.writeArrayEnd();
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {(byte)0x81, (byte)0xF6}, bytes);
	}

	@Test
	public void testWriteFloatArray () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue(100000.0F);
		json.writeArrayEnd();
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {(byte)0x81, (byte)0xfa, 0x47, (byte)0xc3, 0x50, 0x00}, bytes);
	}

	@Test
	public void testWriteDoubleArray () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue(3.141592653589793);
		json.writeArrayEnd();
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {(byte)0x81, (byte)0xFB, 0x40, 0x09, 0x21, (byte)0xFB, 0x54, 0x44, 0x2D, 0x18}, bytes);
	}

	@Test
	public void testWriteIntArray () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue(58);
		json.writeArrayEnd();
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {(byte)0x81, 0x18, 0x3A}, bytes);
	}

	@Test
	public void testWriteLongArray () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue(2147483648L);
		json.writeArrayEnd();
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {(byte)0x81, 0x1A, (byte)0x80, 0x00, 0x00, 0x00}, bytes);
	}

	@Test
	public void testWriteBooleanArray () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue(true);
		json.writeArrayEnd();
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {(byte)0x81, (byte)0xF5}, bytes);
	}

	@Test
	public void testWriteCharArray () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeValue('Z');
		json.writeArrayEnd();
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {(byte)0x81, 0x61, 0x5A}, bytes);
	}

	@Test
	public void testWriteArrayArray () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeArrayStart();
		json.writeArrayStart();
		json.writeArrayEnd();
		json.writeArrayEnd();
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {(byte)0x81, (byte)0x80}, bytes);
	}

	@Test
	public void testWriteStringValue () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue("value");
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {0x65, 0x76, 0x61, 0x6C, 0x75, 0x65}, bytes);
	}

	@Test
	public void testWriteDoubleValue () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue(3.141592653589793);
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {(byte)0xFB, 0x40, 0x09, 0x21, (byte)0xFB, 0x54, 0x44, 0x2D, 0x18}, bytes);
	}

	@Test
	public void testWriteLongValue () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue(2147483648L);
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {0x1A, (byte)0x80, 0x00, 0x00, 0x00}, bytes);
	}

	@Test
	public void testWriteBooleanValue () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue(true);
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {(byte)0xF5}, bytes);
	}

	@Test
	public void testWriteNullValue () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue(null);
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {(byte)0xF6}, bytes);
	}

	@Test
	public void testWriteFloatValue () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue(100000.0F);
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {(byte)0xfa, 0x47, (byte)0xc3, 0x50, 0x00}, bytes);
	}

	@Test
	public void testWriteIntValue () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue(58);
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {0x18, 0x3A}, bytes);
	}

	@Test
	public void testWriteChar () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeValue('Z');
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {0x61, 0x5A}, bytes);
	}

	@Test
	public void testPop () throws IOException {
		json.getWriter().setOutputType(JsonWriter.OutputType.json);
		json.writeObjectStart();
		json.writeValue("key", "value");
		cborWriter.close();
		byte[] bytes = outputStream.toByteArray();
		assertArrayEquals(new byte[] {(byte)0xA1, 0x63, 0x6B, 0x65, 0x79, 0x65, 0x76, 0x61, 0x6C, 0x75, 0x65}, bytes);
	}

	@AfterEach
	public void tearDown () {
	}
}
