package pl.rembol;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class BooleanTest {

    @Test
    public void shouldBlowYourMindsAway() throws NoSuchFieldException, IllegalAccessException {
        doTheMagic();

        if (TRUE) {
            Assert.fail();
        }
        
        if (!FALSE) {
            Assert.fail();
        }
    }

    private void doTheMagic() throws NoSuchFieldException, IllegalAccessException {
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);

        Field valueField = Boolean.class.getDeclaredField("value");

        modifiersField.setInt(valueField, valueField.getModifiers() & ~Modifier.FINAL);
        valueField.setAccessible(true);

        valueField.setBoolean(TRUE, false);
        valueField.setBoolean(FALSE, true);
    }
}
