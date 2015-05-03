package com.rembol.jme3.obliterator.app;

import com.jme3.app.state.AppStateManager;
import com.jme3.input.controls.TouchListener;
import com.jme3.input.event.TouchEvent;
import com.jme3.input.event.TouchEvent.Type;
import com.rembol.jme3.obliterator.util.Rectangle;
import com.rembol.jme3.obliterator.weapon.Gun;

public class ShootListener implements TouchListener {

	private AppStateManager stateManager;
	private Rectangle aimingBoundaries;

	public ShootListener(AppStateManager stateManager, Rectangle aimingBoundaries) {
		this.stateManager = stateManager;
		this.aimingBoundaries = aimingBoundaries;
	}


	@Override
	public void onTouch(String name, TouchEvent event, float tpf) {

		if (event.getType() == Type.MOVE) {
			Gun gun = stateManager.getState(Gun.class);
			if (gun != null) {
				if (aimingBoundaries.contains(event.getX(), event.getY())) {
					gun.fire(aimingBoundaries.xPart(event.getX()), aimingBoundaries.YPart(event.getY()));
				}
			}
		}
	}

}
