
package dev.simonit.gdx.cbor;

import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import org.junit.jupiter.api.Test;

import static dev.simonit.gdx.cbor.CborTest.readBytesFromResource;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class CborValueTest {

	@Test
	void toCborString () {
		CborValue cborValue = new CborValue("value");
		byte[] bytes = cborValue.toCbor(JsonWriter.OutputType.json);
		assertArrayEquals(new byte[] {0x65, 0x76, 0x61, 0x6C, 0x75, 0x65}, bytes);
	}

	@Test
	void toCborDouble () {
		CborValue cborValue = new CborValue(1.0);
		byte[] bytes = cborValue.toCbor(JsonWriter.OutputType.json);
		assertArrayEquals(new byte[] {(byte)0xFB, 0x3F, (byte)0xF0, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}, bytes);
	}

	@Test
	void toCborLong () {
		CborValue cborValue = new CborValue(1L);
		byte[] bytes = cborValue.toCbor(JsonWriter.OutputType.json);
		assertArrayEquals(new byte[] {0x01}, bytes);
	}

	@Test
	void toCborBoolean () {
		CborValue cborValue = new CborValue(true);
		byte[] bytes = cborValue.toCbor(JsonWriter.OutputType.json);
		assertArrayEquals(new byte[] {(byte)0xF5}, bytes);
	}

	@Test
	void toCborObjectEmpty () {
		CborValue cborValue = new CborValue(JsonValue.ValueType.object);
		byte[] bytes = cborValue.toCbor(JsonWriter.OutputType.json);
		assertArrayEquals(new byte[] {(byte)0xA0}, bytes);
	}

	@Test
	void toCborObject () {
		CborValue cborValue = new CborValue(JsonValue.ValueType.object);
		cborValue.addChild("key", new CborValue("value"));
		byte[] bytes = cborValue.toCbor(JsonWriter.OutputType.json);
		assertArrayEquals(new byte[] {(byte)0xA1, 0x63, 0x6B, 0x65, 0x79, 0x65, 0x76, 0x61, 0x6C, 0x75, 0x65}, bytes);
	}

	@Test
	void toCborArrayEmpty () {
		CborValue cborValue = new CborValue(JsonValue.ValueType.array);
		byte[] bytes = cborValue.toCbor(JsonWriter.OutputType.json);
		assertArrayEquals(new byte[] {(byte)0x80}, bytes);
	}

	@Test
	void toCborObjectComplex () {
		CborValue value = new CborValue(JsonValue.ValueType.object);

		CborValue family = new CborValue(JsonValue.ValueType.object);
		family.addChild("surname", new CborValue("Smith"));

// Family members array
		CborValue members = new CborValue(JsonValue.ValueType.array);

		CborValue member1 = new CborValue(JsonValue.ValueType.object);
		member1.addChild("name", new CborValue("John"));
		member1.addChild("age", new CborValue(40));
		members.addChild(member1);

		CborValue member2 = new CborValue(JsonValue.ValueType.object);
		member2.addChild("name", new CborValue("Jane"));
		member2.addChild("age", new CborValue(38));
		members.addChild(member2);

		CborValue member3 = new CborValue(JsonValue.ValueType.object);
		member3.addChild("name", new CborValue("Alice"));
		member3.addChild("age", new CborValue(15));
		members.addChild(member3);

		family.addChild("members", members);

// Pets array
		CborValue pets = new CborValue(JsonValue.ValueType.array);

		CborValue pet1 = new CborValue(JsonValue.ValueType.object);
		pet1.addChild("type", new CborValue("Dog"));
		pet1.addChild("name", new CborValue("Buddy"));
		pets.addChild(pet1);

		CborValue pet2 = new CborValue(JsonValue.ValueType.object);
		pet2.addChild("type", new CborValue("Cat"));
		pet2.addChild("name", new CborValue("Whiskers"));
		pets.addChild(pet2);

		family.addChild("pets", pets);

// Contact information
		CborValue contactInfo = new CborValue(JsonValue.ValueType.object);
		contactInfo.addChild("email", new CborValue("smithfamily@example.com"));
		contactInfo.addChild("phone", new CborValue("123-456-7890"));
		contactInfo.addChild("address", new CborValue((String)null));

		family.addChild("contactInfo", contactInfo);

		value.addChild("family", family);
		byte[] bytes = value.toCbor(JsonWriter.OutputType.json);
		assertArrayEquals(readBytesFromResource("toCborObjectComplex.cbor"), bytes);
	}

	@Test
	public void testWriteArrayComplex () {
		CborValue value = new CborValue(JsonValue.ValueType.array);
		value.addChild(new CborValue("value"));
		value.addChild(new CborValue(1.0));
		value.addChild(new CborValue(1L));
		value.addChild(new CborValue(true));
		value.addChild(new CborValue(JsonValue.ValueType.object));
		value.addChild(new CborValue(JsonValue.ValueType.array));
		byte[] bytes = value.toCbor(JsonWriter.OutputType.json);
		assertArrayEquals(readBytesFromResource("testWriteArrayComplex.cbor"), bytes);
	}
}
