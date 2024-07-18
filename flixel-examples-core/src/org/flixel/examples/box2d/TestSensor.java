package org.flixel.examples.box2d;

import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxCircle;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxShape;
import org.flixel.plugin.flxbox2d.collision.shapes.B2FlxSprite;
import org.flixel.plugin.flxbox2d.common.math.B2FlxMath;
import org.flixel.plugin.flxbox2d.dynamics.B2FlxContactEvent;
import org.flixel.plugin.flxbox2d.dynamics.B2FlxListener;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.utils.Array;

/**
 *
 * @author Ka Wing Chin
 */
public class TestSensor extends Test
{
	private final short CIRCLE = 0x0002;
	private final short SENSOR = 0x0004;
	private B2FlxCircle sensorBody;
	private Vector2 d;
	private float strength = 10.0f;
	private Array<B2FlxCircle> bodies;
	
	@Override
	public void create()
	{
		super.create();
		title.setText("Sensor");
		info.setText("This is used to test sensor shapes");
		
		bodies = new Array<B2FlxCircle>();
		B2FlxCircle circle;
		for(int i = 0; i < 8; i++)
		{
			bodies.add(circle = (B2FlxCircle) new B2FlxCircle(105+i*45, 145, 15)
					.setDensity(1)
					.setCategoryBits((short) CIRCLE)
					.setMaskBits((short) (CIRCLE | SENSOR | WALL))
					.setDraggable(true)
					.create());
			add(circle);
		}
		
		// Make a large sensor in the center.
		sensorBody = (B2FlxCircle) new B2FlxCircle(300-60, 180-60, 60)
			.setType(B2FlxSprite.STATIC)
			.setDensity(0)
			.setCategoryBits((short) SENSOR)
			.setMaskBits((short) CIRCLE)
			.setSensor(true)
			.create();
		add(sensorBody);
		
		// Add event listeners.
		contactListener.addEventListener(B2FlxContactEvent.BEGIN, onContact);
		contactListener.addEventListener(B2FlxContactEvent.END, onRelease);
	}
	
	@Override
	public void update() 
	{
		for(int i = 0; i < bodies.size; i++)
		{
			B2FlxCircle b = bodies.get(i);
			if(b.userData.get("touch") != null && (Boolean)b.userData.get("touch") == true)
			{
				d = B2FlxMath.SubtractVV(sensorBody.body.getWorldCenter(), b.body.getPosition());
				d.nor();
				d.mul(strength);
				b.body.applyForce(d, b.body.getPosition());
			}
		}
		super.update();
	}
	
	B2FlxListener onContact = new B2FlxListener()
	{
		@Override
		public void beginContact(B2FlxShape sprite1, B2FlxShape sprite2, Contact contact)
		{
			if(sprite1.categoryBits == CIRCLE && sprite2.categoryBits == SENSOR)
			{				
				sprite1.userData.put("touch", true);
			}
		}
	};
	
	B2FlxListener onRelease = new B2FlxListener()
	{
		@Override
		public void endContact(B2FlxShape sprite1, B2FlxShape sprite2, Contact contact)
		{
			if(sprite1.categoryBits == CIRCLE && sprite2.categoryBits == SENSOR)
			{				
				sprite1.userData.put("touch", false);
			}
		}
	};
	
	@Override
	public void destroy() 
	{
		super.destroy();
		sensorBody = null;
		d = null;
		bodies.clear();
		bodies = null;
		onContact = null;
		onRelease = null;
	}
	
}