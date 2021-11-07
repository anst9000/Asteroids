package com.neet.gamestates;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.neet.entities.Asteroid;
import com.neet.entities.Bullet;
import com.neet.entities.Particle;
import com.neet.entities.Player;
import com.neet.main.Game;
import com.neet.managers.GameKeys;
import com.neet.managers.GameStateManager;
import com.neet.managers.Jukebox;

public class PlayState extends GameState
{
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;

	private BitmapFont font;

	private Player player;
	private Player hudPlayer;

	private ArrayList<Bullet> bullets;
	private ArrayList<Asteroid> asteroids;
	private ArrayList<Particle> particles;

	private int level;
	private int totalAsteroids;
	private int numAsteroidsLeft;

	private float maxDelay;
	private float minDelay;
	private float currentDelay;
	private float bgTimer;
	private boolean playLowPulse;

	private final int SPLITS = 2;

	public PlayState( GameStateManager gsm )
	{
		super( gsm );
	}

	@Override
	public void init()
	{
		level = 1;

		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();

		// Set font
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator( Gdx.files.internal( "fonts/Hyperspace Bold.ttf" ) );
		font = gen.generateFont( 20 );

		bullets = new ArrayList<Bullet>();
		player = new Player( bullets );

		particles = new ArrayList<Particle>();
		asteroids = new ArrayList<Asteroid>();
		spawnAsteroids();

		hudPlayer = new Player( null );

		// Set up bg music
		maxDelay = 1.0f;
		minDelay = 0.25f;
		currentDelay = maxDelay;
		bgTimer = maxDelay;
		playLowPulse = true;
	}

	private void createParticles( float x, float y )
	{
		for ( int pa = 0; pa < 6; pa++ )
		{
			particles.add( new Particle( x, y ) );
		}
	}

	private void spawnAsteroids()
	{
		asteroids.clear();
		int numToSpawn = 4 + level - 1;
		totalAsteroids = numToSpawn * 7;
		numAsteroidsLeft = totalAsteroids;
		currentDelay = maxDelay;

		float dist = 0.0f;
		float x = 0.0f;
		float y = 0.0f;
		float dx = 0.0f;
		float dy = 0.0f;

		for ( int as = 0; as < numToSpawn; as++ )
		{
			do
			{
				x = MathUtils.random( Game.WIDTH );
				y = MathUtils.random( Game.HEIGHT );
				dx = x - player.getx();
				dy = y - player.gety();
				dist = (float) Math.sqrt( dx * dx + dy * dy );
			} while ( dist < 100.0f );

			asteroids.add( new Asteroid( x, y, Asteroid.LARGE ) );
		}
	}

	private void splitAsteroids( Asteroid asteroid )
	{
		createParticles( asteroid.getx(), asteroid.gety() );

		numAsteroidsLeft--;
		currentDelay = ( ( maxDelay - minDelay ) * numAsteroidsLeft / totalAsteroids ) + minDelay;

		if ( asteroid.getType() == Asteroid.LARGE )
		{

			for ( int as = 0; as < SPLITS; as++ )
			{
				asteroids.add( new Asteroid( asteroid.getx(), asteroid.gety(), Asteroid.MEDIUM ) );
			}
		}

		if ( asteroid.getType() == Asteroid.MEDIUM )
		{

			for ( int as = 0; as < SPLITS; as++ )
			{
				asteroids.add( new Asteroid( asteroid.getx(), asteroid.gety(), Asteroid.SMALL ) );
			}
		}
	}

	@Override
	public void update( float deltaTime )
	{
		// Get user input
		handleInput();

		// Next level
		if ( asteroids.size() == 0 )
		{
			level++;
			spawnAsteroids();
		}

		// Update player
		player.update( deltaTime );

		if ( player.isDead() )
		{

			if ( player.getLives() == 0 )
			{
				gsm.setState( GameStateManager.MENU );
			}

			player.reset();
			player.loseLife();
			return;
		}

		// Update bullets
		for ( int bu = 0; bu < bullets.size(); bu++ )
		{
			bullets.get( bu ).update( deltaTime );

			if ( bullets.get( bu ).shouldRemove() )
			{
				bullets.remove( bu );
				bu--;
			}
		}

		// Update asteroids
		for ( int as = 0; as < asteroids.size(); as++ )
		{
			asteroids.get( as ).update( deltaTime );

			if ( asteroids.get( as ).shouldRemove() )
			{
				asteroids.remove( as );
				as--;
			}
		}

		// Update particles
		for ( int pa = 0; pa < particles.size(); pa++ )
		{
			particles.get( pa ).update( deltaTime );

			if ( particles.get( pa ).shouldRemove() )
			{
				particles.remove( pa );
				pa--;
			}
		}
		// Check collisions
		checkCollisions();

		// Play background music
		bgTimer += deltaTime;

		if ( !player.isHit() && bgTimer >= currentDelay )
		{

			if ( playLowPulse )
			{
				Jukebox.play( "pulselow" );
			}
			else
			{
				Jukebox.play( "pulsehigh" );
			}

			playLowPulse = !playLowPulse;
			bgTimer = 0.0f;
		}
	}

	private void checkCollisions()
	{
		// Player - Asteroid collision
		if ( !player.isHit() )
		{
			for ( int as = 0; as < asteroids.size(); as++ )
			{
				Asteroid asteroid = asteroids.get( as );

				// If player intersects asteroid
				if ( asteroid.intersects( player ) )
				{
					player.hit();
					asteroids.remove( as );
					as--;

					splitAsteroids( asteroid );
					Jukebox.play( "explode" );

					break;
				}
			}
		}

		// Bullet - Asteroid collision
		for ( int bu = 0; bu < bullets.size(); bu++ )
		{
			Bullet bullet = bullets.get( bu );

			for ( int as = 0; as < asteroids.size(); as++ )
			{
				Asteroid asteroid = asteroids.get( as );

				// If asteroid contains the point bullet
				if ( asteroid.contains( bullet.getx(), bullet.gety() ) )
				{
					bullets.remove( bu );
					bu--;
					asteroids.remove( as );
					as--;

					splitAsteroids( asteroid );

					// Increment player score
					player.incrementScore( asteroid.getScore() );
					Jukebox.play( "explode" );

					break;
				}
			}
		}
	}

	@Override
	public void draw()
	{
		spriteBatch.setProjectionMatrix( Game.cam.combined );
		shapeRenderer.setProjectionMatrix( Game.cam.combined );

		// Draw player
		player.draw( shapeRenderer );

		// Draw bullets
		for ( int bu = 0; bu < bullets.size(); bu++ )
		{
			bullets.get( bu ).draw( shapeRenderer );
		}

		// Draw asteroids
		for ( int as = 0; as < asteroids.size(); as++ )
		{
			asteroids.get( as ).draw( shapeRenderer );
		}

		// Draw particles
		for ( int pa = 0; pa < particles.size(); pa++ )
		{
			particles.get( pa ).draw( shapeRenderer );
		}
		// Draw score
		spriteBatch.setColor( Color.WHITE );
		spriteBatch.begin();

		font.draw( spriteBatch, Long.toString( player.getScore() ), 40, 390 );

		spriteBatch.end();

		// Draw lives
		for ( int li = 0; li < player.getLives(); li++ )
		{
			hudPlayer.setPosition( 40 + li * 12, 360 );
			hudPlayer.draw( shapeRenderer );
		}
	}

	@Override
	public void handleInput()
	{
		player.setLeft( GameKeys.isDown( GameKeys.LEFT ) );
		player.setRight( GameKeys.isDown( GameKeys.RIGHT ) );
		player.setUp( GameKeys.isDown( GameKeys.UP ) );

		if ( GameKeys.isPressed( GameKeys.SPACE ) )
		{
			player.shoot();
		}
	}

	@Override
	public void dispose()
	{

	}
}
