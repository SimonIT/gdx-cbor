
package dev.simonit.gdx.cbor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Reader;

public class Utils {

	private Utils () {
	}

	public static byte[] charsToBytes (char[] data) {
		return charsToBytes(data, 0, data.length);
	}

	public static byte[] charsToBytes (char[] data, int offset, int length) {
		byte[] bytes = new byte[length * 2];
		for (int i = 0; i < length; i++) {
			byte[] charBytes = charToBytes(data[i + offset]);
			bytes[i * 2] = charBytes[0];
			bytes[i * 2 + 1] = charBytes[1];
		}
		return bytes;
	}

	public static byte[] charToBytes (char data) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte)(data >> 8);
		bytes[1] = (byte)data;
		return bytes;
	}

	public static char[] bytesToChars (byte[] data) {
		return bytesToChars(data, 0, data.length);
	}

	public static char[] bytesToChars (byte[] data, int offset, int length) {
		char[] chars = new char[length / 2];
		for (int i = 0; i < length / 2; i++) {
			chars[i] = (char)((data[i * 2 + offset] << 8) | (data[i * 2 + 1 + offset] & 0xFF));
		}
		return chars;
	}

	public static byte[] readerToBytes (Reader reader) throws IOException {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		int read;
		while ((read = reader.read()) != -1) {
			outputStream.write(Utils.charToBytes((char)read));
		}
		return outputStream.toByteArray();
	}
}
