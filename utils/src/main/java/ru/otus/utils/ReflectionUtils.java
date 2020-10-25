package ru.otus.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {

    private ReflectionUtils()
    {
        throw new IllegalAccessError();
    }

    public static Object invokeMethod(
            final Method method,
            final Object receiver,
            final Object... args) throws RuntimeException
    {
        Contracts.requireNonNullArgument(method);

        try {
            return method.invoke(receiver, args);
        } catch (final IllegalAccessException ex) {
            throw new RuntimeException("Couldn't invoke method " + method, ex);
        } catch (final InvocationTargetException ex) {
            throw new RuntimeException(
                    "Invoked method " + method + " thrown exception",
                    ex.getCause()
            );
        }
    }

    public static <T> T invokeConstructor(
            final Constructor<T> constructor,
            final Object... args) throws RuntimeException
    {
        Contracts.requireNonNullArgument(constructor);

        try {
            return constructor.newInstance(args);
        } catch (final IllegalAccessException | InstantiationException ex) {
            throw new RuntimeException("Couldn't invoke constructor " + constructor, ex);
        } catch (final InvocationTargetException ex) {
            throw new RuntimeException(
                    "Invoked constructor " + constructor + " thrown exception",
                    ex.getCause()
            );
        }
    }
}
