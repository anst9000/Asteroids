package com.neet.entities;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.neet.main.Game;
import com.neet.managers.Jukebox;

public class Player extends SpaceObject {
	
	private final int MAX_BULLETS = 4;
	private ArrayList<Bullet> bullets;
	
	private float[] flamex;
	private float[] flamey;
	
	private boolean left;
	private boolean right;
	private boolean up;
	
	private float maxSpeed;
	private float acceleration;
	private float deceleration;
	private float acceleratingTimer;
	
	private boolean hit;
	private boolean dead;
	
	private float hitTimer;
	private float hitTime;

	private Line2D.Float[] hitLines;
	private Point2D.Float[] hitLinesVector;
	
	private long score;
	private int extraLives;
	private long requiredScore;
	
	public Player( ArrayList<Bullet> bullets )
	{
		this.bullets = bullets;
		
		x = Game.WIDTH / 2;
		y = Game.HEIGHT / 2;
		
		maxSpeed = 300;
		acceleration = 200;
		deceleration = 10;
		
		shapex = new float[4];
		shapey = new float[4];
		flamex = new float[3];
		flamey = new float[3];
		
		radians = MathUtils.PI / 2.0f;
		rotationSpeed = 3;
		
		hit = false;
		hitTimer = 0;
		hitTime = 2;
		
		score = 0;
		extraLives = 3;
		requiredScore = 10000;
	}
	
	private void setShape()
	{
		shapex[0] = x + MathUtils.cos(radians) * 8;
		shapey[0] = y + MathUtils.sin(radians) * 8;
		
		shapex[1] = x + MathUtils.cos( radians - 4 * MathUtils.PI / 5 ) * 8;
		shapey[1] = y + MathUtils.sin( radians - 4 * MathUtils.PI / 5 ) * 8;
		
		shapex[2] = x + MathUtils.cos( radians + MathUtils.PI ) * 5;
		shapey[2] = y + MathUtils.sin( radians + MathUtils.PI ) * 5;
		
		shapex[3] = x + MathUtils.cos( radians + 4 * MathUtils.PI / 5 ) * 8;
		shapey[3] = y + MathUtils.sin( radians + 4 * MathUtils.PI / 5 ) * 8;
	}
	
	private void setFlame()
	{
		flamex[0] = x + MathUtils.cos( radians - 5 * MathUtils.PI / 6 ) * 5;
		flamey[0] = y + MathUtils.sin( radians - 5 * MathUtils.PI / 6 ) * 5;
		
		flamex[1] = x + MathUtils.cos( radians - MathUtils.PI ) *
					(6 + acceleratingTimer * 50);
		flamey[1] = y + MathUtils.sin( radians - MathUtils.PI ) *
				(6 + acceleratingTimer * 50);
		
		flamex[2] = x + MathUtils.cos( radians + 5 * MathUtils.PI / 6 ) * 5;
		flamey[2] = y + MathUtils.sin( radians + 5 * MathUtils.PI / 6 ) * 5;
	}
	
	public void setLeft( boolean b )
	{ left = b; }

	public void setRight( boolean b )
	{ right = b; }

	public void setUp( boolean state )
	{

		if ( state && !up && !hit )
		{
			Jukebox.loop( "thruster" );
		}
		else if ( !state )
		{
			Jukebox.stop( "thruster" );
		}

		up = state;
	}
	
	@Override
	public void setPosition( float x, float y )
	{
		super.setPosition(x, y);
		setShape();
	}
	
	public boolean isHit()
	{ return hit; }

	public boolean isDead()
	{ return dead; }

	public void reset()
	{
		x = Game.WIDTH / 2;
		y = Game.HEIGHT / 2;

		setShape();
		hit = dead = false;
	}
	
	public long getScore()
	{ return score; }

	public int getLives()
	{ return extraLives; }

	public void loseLife()
	{
		extraLives--;
	}
	
	public void incrementScore( long scoreIncrement )
	{
		score += scoreIncrement;
	}
	
	public void shoot()
	{

		if ( bullets.size() == MAX_BULLETS )
		{
			return;
		}

		bullets.add(new Bullet(x, y, radians));
		Jukebox.play( "shoot" );
	}
	
	public void hit()
	{
		if ( hit )
		{
			return;
		}
		
		hit = true;
		dx = dy = 0;
		left = right = up = false;
		Jukebox.stop( "thruster" );
		
		hitLines = new Line2D.Float[4];

		for ( int i = 0, j = hitLines.length - 1; i < hitLines.length; j = i++ )
		{
			hitLines[i] = new Line2D.Float( shapex[i], shapey[i], shapex[j], shapey[j] );
		}
		
		hitLinesVector = new Point2D.Float[4];

		hitLinesVector[0] = new Point2D.Float(
			MathUtils.cos(radians + 1.5f),
			MathUtils.sin(radians + 1.5f)
		);

		hitLinesVector[1] = new Point2D.Float(
			MathUtils.cos(radians - 1.5f),
			MathUtils.sin(radians - 1.5f)
		);

		hitLinesVector[2] = new Point2D.Float(
			MathUtils.cos(radians - 2.8f),
			MathUtils.sin(radians - 2.8f)
		);

		hitLinesVector[3] = new Point2D.Float(
			MathUtils.cos(radians + 2.8f),
			MathUtils.sin(radians + 2.8f)
		);
	}
	
	public void update( float deltaTime )
	{
		// check if hit
		if ( hit )
		{
			hitTimer += deltaTime;

			if ( hitTimer > hitTime )
			{
				dead = true;
				hitTimer = 0;
			}

			for ( int i = 0; i < hitLines.length; i++ )
			{
				hitLines[i].setLine(
						hitLines[i].x1 + hitLinesVector[i].x * 10 * deltaTime,
						hitLines[i].y1 + hitLinesVector[i].y * 10 * deltaTime,
						hitLines[i].x2 + hitLinesVector[i].x * 10 * deltaTime,
						hitLines[i].y2 + hitLinesVector[i].y * 10 * deltaTime
				);
			}

			return;
		}
		
		// check extra lives
		if ( score >= requiredScore )
		{
			extraLives++;
			requiredScore += 10000;
			Jukebox.play( "extralife" );
		}
		
		// turning
		if ( left )
		{
			radians += rotationSpeed * deltaTime;
		}
		else if ( right )
		{
			radians -= rotationSpeed * deltaTime;
		}
		
		// accelerating
		if ( up )
		{
			dx += MathUtils.cos( radians ) * acceleration * deltaTime;
			dy += MathUtils.sin( radians ) * acceleration * deltaTime;
			acceleratingTimer += deltaTime;

			if ( acceleratingTimer > 0.1f )
			{
				acceleratingTimer = 0;
			}
		}
		else
		{
			acceleratingTimer = 0;
		}
		
		// deceleration
		float vec = (float) Math.sqrt(dx * dx + dy * dy);

		if ( vec > 0 )
		{
			dx -= ( dx / vec ) * deceleration * deltaTime;
			dy -= ( dy / vec ) * deceleration * deltaTime;
		}

		if ( vec > maxSpeed )
		{
			dx = (dx / vec) * maxSpeed;
			dy = (dy / vec) * maxSpeed;
		}
		
		// set position
		x += dx * deltaTime;
		y += dy * deltaTime;
		
		// set shape
		setShape();
		
		// set flame
		if ( up )
		{
			setFlame();
		}
		
		// screen wrap
		wrap();
	}
	
	public void draw( ShapeRenderer shapeRenderer )
	{
		shapeRenderer.setColor( 1, 1, 1, 1 );
		
		shapeRenderer.begin( ShapeType.Line );
		
		// check if hit
		if ( hit )
		{
			for ( int i = 0; i < hitLines.length; i++ )
			{
				shapeRenderer.line( hitLines[i].x1, hitLines[i].y1, hitLines[i].x2, hitLines[i].y2 );
			}

			shapeRenderer.end();

			return;
		}
		
		// draw ship
		for ( int i = 0, j = shapex.length - 1; i < shapex.length; j = i++ )
		{
			shapeRenderer.line( shapex[i], shapey[i], shapex[j], shapey[j] );
		}
		
		// draw flames
		if ( up )
		{
			for ( int i = 0, j = flamex.length - 1; i < flamex.length; j = i++ )
			{
				shapeRenderer.line( flamex[i], flamey[i], flamex[j], flamey[j] );
			}
		}
		
		shapeRenderer.end();
	}
}
