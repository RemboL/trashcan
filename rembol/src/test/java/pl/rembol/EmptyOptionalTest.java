package pl.rembol;

import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class EmptyOptionalTest {

    @BeforeClass
    public static void doTheMagic() throws NoSuchFieldException, IllegalAccessException {
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);

        Field valueField = Optional.class.getDeclaredField("value");

        modifiersField.setInt(valueField, valueField.getModifiers() & ~Modifier.FINAL);
        valueField.setAccessible(true);

        valueField.set(Optional.empty(), "a hidden message");
    }

    @Test
    public void emptyOptionalIsNotNull() {
        assertNotNull(Optional.empty().get());
    }

    @Test
    public void emptyOptionalIsPresent() {
        assertTrue(Optional.empty().isPresent());
    }

    @Test
    public void andEmptyOptionalDoesntReactToOrElses() {
        Optional.empty().orElseThrow(AssertionError::new);
    }

    @Test
    public void emptyStreamIsNotReallyEmpty() {
        assertNotNull(Stream.empty().findFirst().get());
    }


}
