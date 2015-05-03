package com.rembol.jme3.obliterator.activity;

import android.content.pm.ActivityInfo;

import com.jme3.app.AndroidHarness;
import com.jme3.system.android.AndroidConfigChooser.ConfigType;
import com.rembol.jme3.obliterator.app.MainApplication;

public class JME3Activity extends AndroidHarness {
	public JME3Activity()
	{
		// Set the application class to run
//		appClass = "com.jme3.test.MyGame";
		appClass = MainApplication.class.getCanonicalName();
	 
		// Try ConfigType.FASTEST; or ConfigType.LEGACY if you have problems
		eglConfigType = ConfigType.BEST;
	 
		// Exit Dialog title & message
		exitDialogTitle = "Quit game?";
		exitDialogMessage = "Do you really want to quit the game?";
	 
		// Choose screen orientation
		screenOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
	 
		// Invert the MouseEvents X (default = true)
		mouseEventsInvertX = true;
	 
		// Invert the MouseEvents Y (default = true)
		mouseEventsInvertY = true;
	}

}
