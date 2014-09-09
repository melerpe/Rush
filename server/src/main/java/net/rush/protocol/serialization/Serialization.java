package net.rush.protocol.serialization;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import lombok.Getter;
import net.rush.protocol.Packet;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

public class Serialization {

	public static class SerializorWriter<T extends Packet> {

		@SuppressWarnings({ "unchecked", "rawtypes" })
		public void send(ByteBuf buf, T packet) throws IllegalArgumentException, IllegalAccessException, IOException {
			List<SerializationInfo> serInfos = getSerializationInfos(packet);

			ListIterator<SerializationInfo> iterator = serInfos.listIterator();

			while (iterator.hasNext()) {
				SerializationInfo now = iterator.next();
				Serializor serializor = now.getSerialize().type().getSerializor();
				serializor.write(buf, now.getFieldType());
			}
		}
	}

	private static final Cache<Packet, List<SerializationInfo>> serializationInfoCache = CacheBuilder.newBuilder().weakKeys().softValues().expireAfterAccess(1, TimeUnit.MINUTES)
			.build(new CacheLoader<Packet, List<SerializationInfo>>() {
				@Override
				public List<SerializationInfo> load(Packet packet) throws Exception {

					// *sigh* now let's get the attributes
					ArrayList<SerializationInfo> serInfos = new ArrayList<SerializationInfo>();
					Field[] fields = packet.getClass().getDeclaredFields();

					for (Field f : fields) {
						f.setAccessible(true);
						if (f.isAnnotationPresent(Serialize.class))
							serInfos.add(new SerializationInfo(f.getAnnotation(Serialize.class), f.getType().newInstance()));
						f.setAccessible(false);
					}
					return Collections.unmodifiableList(serInfos);
				}
			});

	static List<SerializationInfo> getSerializationInfos(Packet clazz) {
		return serializationInfoCache.getIfPresent(clazz);
	}

	@Getter
	final static class SerializationInfo {
		private final Serialize serialize;
		private final Object fieldType;

		SerializationInfo(Serialize serialize, Object fieldType) {
			this.serialize = serialize;
			this.fieldType = fieldType;
		}
	}


	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface Serialize {
		Type type();
		/**
		 * @return A Serialize that contains more info.
		 * Very ugly, but I'm unable to find a better solution :/
		 */
		int moreInfo() default -1;
	}

	public static interface Serializor<T> {
		default T read(ByteBuf in) throws IOException {
			return null;
		}

		default T read(ByteBuf in, Object more) throws IOException {
			return null;
		}

		void write(ByteBuf out, T val) throws IOException;
	}

}
