package com.neet.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;

public class Particle extends SpaceObject
{
	private float timer;
	private float time;
	private boolean remove;
	
	public Particle( float x, float y )
	{
		this.x = x;
		this.y = y;
		width = height = 2;
		
		speed = 50;
		radians = MathUtils.random( 2 * MathUtils.PI );
		dx = MathUtils.cos(radians) * speed;
		dy = MathUtils.sin(radians) * speed;
		
		timer = 0;
		time = 1;
	}
	
	public boolean shouldRemove() { return remove; }
	
	public void update( float deltaTime )
	{
		x += dx * deltaTime;
		y += dy * deltaTime;
		
		timer += deltaTime;

		if ( timer > time )
		{
			remove = true;
		}
	}
	
	public void draw( ShapeRenderer shapeRenderer )
	{
		shapeRenderer.setColor( Color.WHITE );
		shapeRenderer.begin( ShapeType.Circle );
		shapeRenderer.circle( x - width / 2, y - width / 2, width / 2 );
		shapeRenderer.end();
	}
}

















