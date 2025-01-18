
package dev.simonit.gdx.cbor;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

	@Test
	void charsToBytes () {
		char[] data = new char[] {0x1234, 0x5678, 0x9ABC, 0xDEF0};
		byte[] bytes = Utils.charsToBytes(data);
		assertArrayEquals(new byte[] {0x12, 0x34, 0x56, 0x78, (byte)0x9A, (byte)0xBC, (byte)0xDE, (byte)0xF0}, bytes);
	}

	@Test
	void bytesToChars () {
		byte[] data = new byte[] {0x12, 0x34, 0x56, 0x78, (byte)0x9A, (byte)0xBC, (byte)0xDE, (byte)0xF0};
		char[] chars = Utils.bytesToChars(data);
		assertArrayEquals(new char[] {0x1234, 0x5678, 0x9ABC, 0xDEF0}, chars);
	}

	@Test
	void readerToBytes () throws IOException {
		Reader reader = new Reader() {
			boolean first = true;

			@Override
			public int read (char[] cbuf, int off, int len) {
				if (first) {
					first = false;
					cbuf[off] = 0x1234;
					return 0;
				} else {
					return -1;
				}
			}

			@Override
			public void close () {
			}
		};
		byte[] bytes = Utils.readerToBytes(reader);
		assertArrayEquals(new byte[] {0x12, 0x34}, bytes);
	}
}
