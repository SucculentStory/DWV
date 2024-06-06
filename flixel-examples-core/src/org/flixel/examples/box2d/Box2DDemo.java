package org.flixel.examples.box2d;

import org.flixel.FlxCamera;
import org.flixel.FlxGame;

/**
 * 
 * @author Ka Wing Chin
 */
public class Box2DDemo extends FlxGame
{
	public Box2DDemo()
	{
		super(640, 360, Test.class, 1, 50, 50, false, 640, 360, FlxCamera.FILL_X);
		forceDebugger = true;
	}
}
