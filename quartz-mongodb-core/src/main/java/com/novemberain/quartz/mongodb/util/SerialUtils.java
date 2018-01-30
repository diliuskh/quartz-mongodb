package com.novemberain.quartz.mongodb.util;

import java.io.*;
import java.util.Base64;
import java.util.Map;
import org.bson.types.Binary;
import org.quartz.Calendar;
import org.quartz.JobDataMap;
import org.quartz.JobPersistenceException;

public final class SerialUtils {

  private SerialUtils() {
    throw new AssertionError();
  }

  public static Object serialize(Calendar calendar) throws JobPersistenceException {
    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    try {
      ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
      objectStream.writeObject(calendar);
      objectStream.close();
      return byteStream.toByteArray();
    } catch (IOException e) {
      throw new JobPersistenceException("Could not serialize Calendar.", e);
    }
  }

  public static <T> T deserialize(Binary serialized, Class<T> clazz)
      throws JobPersistenceException {
    ByteArrayInputStream byteStream = new ByteArrayInputStream(serialized.getData());
    try {
      ObjectInputStream objectStream = new ObjectInputStream(byteStream);
      Object deserialized = objectStream.readObject();
      objectStream.close();
      if (clazz.isInstance(deserialized)) {
        @SuppressWarnings("unchecked")
        T obj = (T) deserialized;
        return obj;
      }

      throw new JobPersistenceException("Deserialized object is not of the desired type");
    } catch (IOException | ClassNotFoundException e) {
      throw new JobPersistenceException("Could not deserialize.", e);
    }
  }

  public static String serialize(JobDataMap jobDataMap) throws IOException {
    byte[] bytes = stringMapToBytes(jobDataMap.getWrappedMap());
    return Base64.getEncoder().encodeToString(bytes);
  }

  public static Map<String, ?> deserialize(String clob) throws IOException {
    try {
      byte[] bytes = Base64.getDecoder().decode(clob);
      return stringMapFromBytes(bytes);
    } catch (IllegalArgumentException | ClassNotFoundException e) {
      throw new IOException(
          String.format("Could not deserialize '%s' into the JobDataMap", clob), e);
    }
  }

  private static byte[] stringMapToBytes(Object object) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(baos);
    out.writeObject(object);
    out.flush();
    return baos.toByteArray();
  }

  private static Map<String, ?> stringMapFromBytes(byte[] bytes)
      throws IOException, ClassNotFoundException {
    ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
    ObjectInputStream ois = new ObjectInputStream(bais);
    @SuppressWarnings("unchecked")
    Map<String, ?> map = (Map<String, ?>) ois.readObject();
    ois.close();
    return map;
  }
}
