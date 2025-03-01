
package dev.simonit.gdx.cbor;

import co.nstant.in.cbor.CborDecoder;
import co.nstant.in.cbor.CborException;
import co.nstant.in.cbor.model.*;
import co.nstant.in.cbor.model.Array;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.*;
import lombok.Getter;

import java.io.*;
import java.util.List;

@Getter
public class CborReader extends JsonReader {

	/** Uses the provided base64 encoded string to parse a CBOR value.
	 * @param base64 The base64 encoded string to parse
	 * @return The parsed CBOR value */
	@Override
	public CborValue parse (String base64) {
		return parse(Base64Coder.decode(base64));
	}

	@Override
	public CborValue parse (InputStream input) {
		try {
			CborDecoder decoder = new CborDecoder(input);
			List<DataItem> dataItems = decoder.decode();
			if (dataItems.isEmpty()) {
				return null;
			} else {
				return parseDataItem(dataItems.get(0));
			}
		} catch (CborException e) {
			throw new SerializationException(e);
		}
	}

	@Override
	public CborValue parse (FileHandle file) {
		return parse(file.read());
	}

	/** Parses the provided char array as Base64 encoded CBOR value.
	 * @param data The char array to parse
	 * @param offset The offset in the char array
	 * @param length The length of the data to parse
	 * @return The parsed CBOR value */
	@Override
	public CborValue parse (char[] data, int offset, int length) {
		return parse(new String(data, offset, length));
	}

	public CborValue parse (byte[] bytes) {
		return parse(new ByteArrayInputStream(bytes));
	}

	private CborValue parseDataItem (DataItem dataItem) {
		switch (dataItem.getMajorType()) {
		case ARRAY:
			CborValue cborArray = new CborValue(JsonValue.ValueType.array);
			Array array = (Array)dataItem;
			for (DataItem item : array.getDataItems()) {
				cborArray.addChild(parseDataItem(item));
			}
			return cborArray;
		case MAP:
			CborValue cborMap = new CborValue(JsonValue.ValueType.object);
			Map map = (Map)dataItem;
			for (DataItem key : map.getKeys()) {
				CborValue cborKey = parseDataItem(key);
				if (!cborKey.isString()) {
					throw new SerializationException("Key must be a string");
					// TODO Maybe allow other keys
				}
				cborMap.addChild(cborKey.asString(), parseDataItem(map.get(key)));
			}
			return cborMap;
		case UNSIGNED_INTEGER:
			CborValue cborUnsignedInteger = new CborValue(JsonValue.ValueType.longValue);
			UnsignedInteger unsignedInteger = (UnsignedInteger)dataItem;
			cborUnsignedInteger.set(unsignedInteger.getValue().longValueExact(), null);
			return cborUnsignedInteger;
		case NEGATIVE_INTEGER:
			CborValue cborNegativeInteger = new CborValue(JsonValue.ValueType.longValue);
			NegativeInteger negativeInteger = (NegativeInteger)dataItem;
			cborNegativeInteger.set(negativeInteger.getValue().longValueExact(), null);
			return cborNegativeInteger;
		case BYTE_STRING:
			CborValue cborBytes = new CborValue(JsonValue.ValueType.array);
			ByteString byteString = (ByteString)dataItem;
			for (byte b : byteString.getBytes()) {
				CborValue cborByte = new CborValue(JsonValue.ValueType.longValue);
				cborByte.set(b, null);
				cborBytes.addChild(cborByte);
			}
			return cborBytes;
		case SPECIAL:
			Special special = (Special)dataItem;
			switch (special.getSpecialType()) {
			case IEEE_754_DOUBLE_PRECISION_FLOAT:
				DoublePrecisionFloat doublePrecisionFloat = (DoublePrecisionFloat)dataItem;
				CborValue cborDouble = new CborValue(JsonValue.ValueType.doubleValue);
				cborDouble.set(doublePrecisionFloat.getValue(), null);
				return cborDouble;
			case IEEE_754_HALF_PRECISION_FLOAT:
				HalfPrecisionFloat halfPrecisionFloat = (HalfPrecisionFloat)dataItem;
				CborValue cborHalf = new CborValue(JsonValue.ValueType.doubleValue);
				cborHalf.set(halfPrecisionFloat.getValue(), null);
				return cborHalf;
			case IEEE_754_SINGLE_PRECISION_FLOAT:
				SinglePrecisionFloat singlePrecisionFloat = (SinglePrecisionFloat)dataItem;
				CborValue cborFloat = new CborValue(JsonValue.ValueType.doubleValue);
				cborFloat.set(singlePrecisionFloat.getValue(), null);
				return cborFloat;
			case SIMPLE_VALUE:
				SimpleValue simpleValue = (SimpleValue)dataItem;
				switch (simpleValue.getSimpleValueType()) {
				case FALSE:
					CborValue cborFalse = new CborValue(JsonValue.ValueType.booleanValue);
					cborFalse.set(false);
					return cborFalse;
				case TRUE:
					CborValue cborTrue = new CborValue(JsonValue.ValueType.booleanValue);
					cborTrue.set(true);
					return cborTrue;
				case NULL:
					CborValue cborNull = new CborValue(JsonValue.ValueType.nullValue);
					cborNull.set(null);
					return cborNull;
				}
				break;
			}
			break;
		case UNICODE_STRING:
			CborValue cborString = new CborValue(JsonValue.ValueType.stringValue);
			UnicodeString unicodeString = (UnicodeString)dataItem;
			cborString.set(unicodeString.getString());
			return cborString;
		}
		throw new SerializationException("Unknown major type: " + dataItem.getMajorType());
	}
}
