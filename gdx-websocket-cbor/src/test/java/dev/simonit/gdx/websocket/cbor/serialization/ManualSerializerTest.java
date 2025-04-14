
package dev.simonit.gdx.websocket.cbor.serialization;

import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.reflect.ArrayReflection;
import com.github.czyzby.websocket.serialization.SerializationException;
import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.ManualSerializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManualSerializerTest {

	static ManualSerializer serializer;

	@BeforeAll
	static void setUp () {
		serializer = new ManualSerializer(new CborSerializer(), new CborDeserializer());
		serializer.register(new Ping());
		serializer.register(new Pong());
		serializer.register(new Test1());
		serializer.register(new TestMapGraph());
	}

	@Test
	void testPing () {
		Ping ping = new Ping(1, true);
		byte[] serialized = serializer.serialize(ping);
		assertArrayEquals(new byte[] {0x00, (byte)0x82, 0x01, (byte)0xF5}, serialized);
		Ping p = (Ping)serializer.deserialize(serialized);
		assertEquals(ping, p);
	}

	@Test
	void testPong () {
		Pong pong = new Pong(-99999, false);
		byte[] serialized = serializer.serialize(pong);
		assertArrayEquals(new byte[] {0x01, (byte)0x82, (byte)0xFA, (byte)0xC7, (byte)0xC3, 0x4F, (byte)0x80, (byte)0xF4},
			serialized);
		Pong p = (Pong)serializer.deserialize(serialized);
		assertEquals(pong, p);
	}

	@Test
	void test () {
		Test1 test = new Test1();
		test.booleanField = true;
		test.byteField = 123;
		test.charField = 'Z';
		test.shortField = 12345;
		test.intField = 123456;
		test.longField = 123456789;
		test.floatField = 123.456f;
		test.doubleField = 1.23456d;
		test.BooleanField = true;
		test.ByteField = -12;
		test.CharacterField = 'X';
		test.ShortField = -12345;
		test.IntegerField = -123456;
		test.LongField = -123456789L;
		test.FloatField = -123.3f;
		test.DoubleField = -0.121231d;
		test.stringField = "stringvalue";
		test.byteArrayField = new byte[] {2, 1, 0, -1, -2};
		test.stringArray = new Array<>();
		test.stringArray.add("meow");
		test.stringArray.add("moo");
		test.someEnum = SomeEnum.b;

		byte[] serialized = serializer.serialize(test);
		Test1 test1 = (Test1)serializer.deserialize(serialized);
		assertEquals(test, test1);
	}

	static public class Test1 implements Transferable<Test1> {
		// Primitives.
		public boolean booleanField;
		public byte byteField;
		public char charField;
		public short shortField;
		public int intField;
		public long longField;
		public float floatField;
		public double doubleField;
		// Primitive wrappers.
		public Boolean BooleanField;
		public Byte ByteField;
		public Character CharacterField;
		public Short ShortField;
		public Integer IntegerField;
		public Long LongField;
		public Float FloatField;
		public Double DoubleField;
		// Other.
		public String stringField;
		public byte[] byteArrayField;
		public Transferable<?> object;
		public ObjectMap<String, Integer> map;
		public Array<String> stringArray;
		public Array<Transferable<?>> objectArray;
		public LongMap<String> longMap;
		public ObjectFloatMap<String> stringFloatMap;
		public SomeEnum someEnum;
		public IntMap<Integer> intsToIntsBoxed;
		public IntIntMap intsToIntsUnboxed;

		public boolean equals (Object obj) {
			if (this == obj) return true;
			if (obj == null) return false;
			if (getClass() != obj.getClass()) return false;
			Test1 other = (Test1)obj;
			if (BooleanField == null) {
				if (other.BooleanField != null) return false;
			} else if (!BooleanField.equals(other.BooleanField)) return false;
			if (ByteField == null) {
				if (other.ByteField != null) return false;
			} else if (!ByteField.equals(other.ByteField)) return false;
			if (CharacterField == null) {
				if (other.CharacterField != null) return false;
			} else if (!CharacterField.equals(other.CharacterField)) return false;
			if (DoubleField == null) {
				if (other.DoubleField != null) return false;
			} else if (!DoubleField.equals(other.DoubleField)) return false;
			if (FloatField == null) {
				if (other.FloatField != null) return false;
			} else if (!FloatField.equals(other.FloatField)) return false;
			if (IntegerField == null) {
				if (other.IntegerField != null) return false;
			} else if (!IntegerField.equals(other.IntegerField)) return false;
			if (LongField == null) {
				if (other.LongField != null) return false;
			} else if (!LongField.equals(other.LongField)) return false;
			if (ShortField == null) {
				if (other.ShortField != null) return false;
			} else if (!ShortField.equals(other.ShortField)) return false;
			if (stringField == null) {
				if (other.stringField != null) return false;
			} else if (!stringField.equals(other.stringField)) return false;
			if (booleanField != other.booleanField) return false;

			Object list1 = arrayToList(byteArrayField);
			Object list2 = arrayToList(other.byteArrayField);
			if (list1 != list2) {
				if (list1 == null || list2 == null) return false;
				if (!list1.equals(list2)) return false;
			}

			if (object != other.object) {
				if (object == null || other.object == null) return false;
				if (object != this && !object.equals(other.object)) return false;
			}

			if (map != other.map) {
				if (map == null || other.map == null) return false;
				if (!map.keys().toArray().equals(other.map.keys().toArray())) return false;
				if (!map.values().toArray().equals(other.map.values().toArray())) return false;
			}

			if (stringArray != other.stringArray) {
				if (stringArray == null || other.stringArray == null) return false;
				if (!stringArray.equals(other.stringArray)) return false;
			}

			if (objectArray != other.objectArray) {
				if (objectArray == null || other.objectArray == null) return false;
				if (!objectArray.equals(other.objectArray)) return false;
			}

			if (longMap != other.longMap) {
				if (longMap == null || other.longMap == null) return false;
				if (!longMap.equals(other.longMap)) return false;
			}

			if (stringFloatMap != other.stringFloatMap) {
				if (stringFloatMap == null || other.stringFloatMap == null) return false;
				if (!stringFloatMap.equals(other.stringFloatMap)) return false;
			}

			if (intsToIntsBoxed != other.intsToIntsBoxed) {
				if (intsToIntsBoxed == null || other.intsToIntsBoxed == null) return false;
				if (!intsToIntsBoxed.equals(other.intsToIntsBoxed)) return false;
			}

			if (intsToIntsUnboxed != other.intsToIntsUnboxed) {
				if (intsToIntsUnboxed == null || other.intsToIntsUnboxed == null) return false;
				if (!intsToIntsUnboxed.equals(other.intsToIntsUnboxed)) return false;
			}

			if (byteField != other.byteField) return false;
			if (charField != other.charField) return false;
			if (Double.doubleToLongBits(doubleField) != Double.doubleToLongBits(other.doubleField)) return false;
			if (Float.floatToIntBits(floatField) != Float.floatToIntBits(other.floatField)) return false;
			if (intField != other.intField) return false;
			if (longField != other.longField) return false;
			if (shortField != other.shortField) return false;
			return true;
		}

		@Override
		public void serialize (Serializer serializer) throws SerializationException {
			serializer.serializeBoolean(booleanField).serializeByte(byteField).serializeInt(charField).serializeShort(shortField)
				.serializeInt(intField).serializeLong(longField).serializeFloat(floatField).serializeDouble(doubleField)
				.serializeBoolean(BooleanField).serializeByte(ByteField).serializeInt(CharacterField).serializeShort(ShortField)
				.serializeInt(IntegerField).serializeLong(LongField).serializeFloat(FloatField).serializeDouble(DoubleField)
				.serializeString(stringField).serializeByteArray(byteArrayField)
				// .serializeTransferable(object)
				.serializeStringArray(stringArray.toArray(String[]::new))
				// .serializeTransferableArray(objectArray.toArray())
				.serializeEnum(someEnum);
		}

		@Override
		public Test1 deserialize (Deserializer deserializer) throws SerializationException {
			Test1 test1 = new Test1();
			test1.booleanField = deserializer.deserializeBoolean();
			test1.byteField = deserializer.deserializeByte();
			test1.charField = (char)deserializer.deserializeInt();
			test1.shortField = deserializer.deserializeShort();
			test1.intField = deserializer.deserializeInt();
			test1.longField = deserializer.deserializeLong();
			test1.floatField = deserializer.deserializeFloat();
			test1.doubleField = deserializer.deserializeDouble();
			test1.BooleanField = deserializer.deserializeBoolean();
			test1.ByteField = deserializer.deserializeByte();
			test1.CharacterField = (char)deserializer.deserializeInt();
			test1.ShortField = deserializer.deserializeShort();
			test1.IntegerField = deserializer.deserializeInt();
			test1.LongField = deserializer.deserializeLong();
			test1.FloatField = deserializer.deserializeFloat();
			test1.DoubleField = deserializer.deserializeDouble();
			test1.stringField = deserializer.deserializeString();
			test1.byteArrayField = deserializer.deserializeByteArray();
			// test1.object = deserializer.deserializeTransferable();
			test1.stringArray = new Array<>(deserializer.deserializeStringArray());
			// test1.objectArray = new Array<>(deserializer.deserializeTransferableArray());
			test1.someEnum = deserializer.deserializeEnum(SomeEnum.values());
			return test1;
		}
	}

	static Object arrayToList (Object array) {
		if (array == null || !array.getClass().isArray()) return array;
		ArrayList<Object> list = new ArrayList<>(ArrayReflection.getLength(array));
		for (int i = 0, n = ArrayReflection.getLength(array); i < n; i++)
			list.add(arrayToList(ArrayReflection.get(array, i)));
		return list;
	}

	public static class TestMapGraph implements Transferable<TestMapGraph> {
		public Map<String, String> map = new HashMap<>();
		public ObjectMap<String, String> objectMap = new ObjectMap<>();
		public ArrayMap<String, String> arrayMap = new ArrayMap<>();

		public TestMapGraph () {
			map.put("a", "b");
			map.put("c", "d");
			objectMap.put("a", "b");
			objectMap.put("c", "d");
			arrayMap.put("a", "b");
			arrayMap.put("c", "d");
		}

		@Override
		public void serialize (Serializer serializer) throws SerializationException {

		}

		@Override
		public TestMapGraph deserialize (Deserializer deserializer) throws SerializationException {
			return null;
		}
	}

	public enum SomeEnum {
		a, b, c;
	}
}
