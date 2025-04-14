
package dev.simonit.gdx.websocket.cbor.serialization;

import com.github.czyzby.websocket.serialization.Transferable;
import com.github.czyzby.websocket.serialization.impl.Deserializer;
import com.github.czyzby.websocket.serialization.impl.Serializer;

import java.util.Objects;

public class Pong implements Transferable<Pong> {
	private float value;
	private boolean server;

	public Pong () {
	}

	public Pong (final float value, final boolean server) {
		this.value = value;
		this.server = server;
	}

	@Override
	public void serialize (final Serializer serializer) {
		serializer.serializeFloat(value).serializeBoolean(server);
	}

	@Override
	public Pong deserialize (final Deserializer deserializer) {
		return new Pong(deserializer.deserializeFloat(), deserializer.deserializeBoolean());
	}

	public float getValue () {
		return value;
	}

	public boolean isServer () {
		return server;
	}

	public void setValue (final float value) {
		this.value = value;
	}

	public void setServer (final boolean server) {
		this.server = server;
	}

	@Override
	public boolean equals (Object o) {
		if (!(o instanceof Pong)) return false;
		Pong pong = (Pong)o;
		return Float.compare(value, pong.value) == 0 && server == pong.server;
	}

	@Override
	public int hashCode () {
		return Objects.hash(value, server);
	}

	@Override
	public String toString () {
		return "Pong [value=" + value + ", server=" + server + "]";
	}
}
