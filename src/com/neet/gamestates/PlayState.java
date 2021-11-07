package com.neet.gamestates;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
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

public class PlayState extends GameState
{
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;

	private BitmapFont font;

	private Player player;
	private ArrayList<Bullet> bullets;
	private ArrayList<Asteroid> asteroids;
	private ArrayList<Particle> particles;

	private int level;
	private int totalAsteroids;
	private int numAsteroidsLeft;

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

					break;
				}
			}
		}
	}

	@Override
	public void draw()
	{
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
		spriteBatch.setColor( 1, 1, 1, 1 );
		spriteBatch.begin();

		font.draw( spriteBatch, Long.toString( player.getScore() ), 40, 390 );

		spriteBatch.end();
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
