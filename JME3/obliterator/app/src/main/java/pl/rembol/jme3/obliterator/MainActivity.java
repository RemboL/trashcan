package pl.rembol.jme3.obliterator;

import com.jme3.app.AndroidHarness;

import pl.rembol.jme3.obliterator.app.MainApplication;

public class MainActivity extends AndroidHarness {

    public MainActivity() {
        appClass = MainApplication.class.getCanonicalName();

        // Invert the MouseEvents X (default = true)
        mouseEventsInvertX = true;

        // Invert the MouseEvents Y (default = true)
        mouseEventsInvertY = true;
    }

}
