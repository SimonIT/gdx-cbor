
package dev.simonit.gdx.websocket.cbor.serialization;

import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;

import java.util.Objects;

public class Ping implements Transferable<Ping> {
	private int value;
	private boolean client;

	public Ping () {
	}

	public Ping (final int value, final boolean client) {
		this.value = value;
		this.client = client;
	}

	@Override
	public void serialize (final Serializer serializer) {
		serializer.serializeInt(value).serializeBoolean(client);
	}

	@Override
	public Ping deserialize (final Deserializer deserializer) {
		return new Ping(deserializer.deserializeInt(), deserializer.deserializeBoolean());
	}

	public int getValue () {
		return value;
	}

	public boolean isClient () {
		return client;
	}

	public void setValue (final int value) {
		this.value = value;
	}

	public void setClient (final boolean client) {
		this.client = client;
	}

	@Override
	public boolean equals (Object o) {
		if (!(o instanceof Ping)) return false;
		Ping ping = (Ping)o;
		return value == ping.value && client == ping.client;
	}

	@Override
	public int hashCode () {
		return Objects.hash(value, client);
	}

	@Override
	public String toString () {
		return "Ping [value=" + value + ", client=" + client + "]";
	}
}
