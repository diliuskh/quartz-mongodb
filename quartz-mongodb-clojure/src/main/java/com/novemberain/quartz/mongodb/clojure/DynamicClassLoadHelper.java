package com.novemberain.quartz.mongodb.clojure;

import clojure.lang.DynamicClassLoader;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import org.quartz.spi.ClassLoadHelper;

/**
 * Makes it possible for Quartz to load and instantiate jobs that are defined using Clojure
 * defrecord without AOT compilation.
 */
public class DynamicClassLoadHelper implements ClassLoadHelper {

  @Override
  public ClassLoader getClassLoader() {
    try {
      return AccessController.doPrivileged(
          (PrivilegedExceptionAction<ClassLoader>) DynamicClassLoader::new);
    } catch (PrivilegedActionException e) {
      throw new SecurityException(e);
    }
  }

  @Override
  public URL getResource(String name) {
    return null;
  }

  @Override
  public InputStream getResourceAsStream(String name) {
    return null;
  }

  @Override
  public void initialize() {}

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    return null;
  }

  @Override
  public <T> Class<? extends T> loadClass(String name, Class<T> clazz)
      throws ClassNotFoundException {
    return null;
  }
}
