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
import com.neet.main.Game;
import com.neet.managers.GameKeys;
import com.neet.managers.GameStateManager;
import com.neet.managers.Save;

public class MenuState extends GameState
{
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	
	private BitmapFont titleFont;
	private BitmapFont font;
	
	private final String title = "Asteroids";
	
	private int currentItem;
	private String[] menuItems;
	
	private ArrayList<Asteroid> asteroids;
	
	public MenuState( GameStateManager gsm )
	{
		super(gsm);
	}
	
	@Override
	public void init()
	{
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
			Gdx.files.internal("fonts/Hyperspace Bold.ttf")
		);
		
		titleFont = gen.generateFont(56);
		titleFont.setColor(Color.WHITE);
		
		font = gen.generateFont(20);
		
		menuItems = new String[] { "Play", "Highscores", "Quit" };
		
		asteroids = new ArrayList<Asteroid>();

		for ( int i = 0; i < 6; i++ )
		{
			asteroids.add( new Asteroid(
					MathUtils.random( Game.WIDTH ), MathUtils.random( Game.HEIGHT ), Asteroid.LARGE )
			);
		}
		
		Save.load();
	}
	
	@Override
	public void update( float deltaTime )
	{
		handleInput();
		
		for ( int i = 0; i < asteroids.size(); i++ )
		{
			asteroids.get( i ).update( deltaTime );
		}
		
	}
	
	@Override
	public void draw()
	{
		spriteBatch.setProjectionMatrix( Game.cam.combined );
		shapeRenderer.setProjectionMatrix( Game.cam.combined );
		
		// draw asteroids
		for ( int i = 0; i < asteroids.size(); i++ )
		{
			asteroids.get( i ).draw( shapeRenderer );
		}
		
		spriteBatch.begin();
		
		// draw title
		float width = titleFont.getBounds(title).width;
		titleFont.draw( spriteBatch, title, ( Game.WIDTH - width ) / 2, 300 );
		
		// draw menu
		for ( int i = 0; i < menuItems.length; i++ )
		{
			width = font.getBounds(menuItems[i]).width;

			if ( currentItem == i )
			{
				font.setColor( Color.RED );
			}
			else
			{
				font.setColor( Color.WHITE );
			}

			font.draw( spriteBatch, menuItems[i], ( Game.WIDTH - width ) / 2, 180 - 35 * i );
		}
		
		spriteBatch.end();
	}
	
	@Override
	public void handleInput() {
		
		if ( GameKeys.isPressed( GameKeys.UP ) )
		{

			if ( currentItem > 0 )
			{
				currentItem--;
			}
		}

		if ( GameKeys.isPressed( GameKeys.DOWN ) )
		{

			if ( currentItem < menuItems.length - 1 )
			{
				currentItem++;
			}
		}

		if ( GameKeys.isPressed( GameKeys.ENTER ) )
		{
			select();
		}
		
	}
	
	private void select() {

		switch ( currentItem )
		{
			// play
			case 0:
				gsm.setState( GameStateManager.PLAY );
				break;
			// high scores
			case 1:
				gsm.setState( GameStateManager.HIGHSCORE );
				break;
			// quit
			case 2:
				Gdx.app.exit();
				break;
		}
	}
	
	@Override
	public void dispose()
	{
		spriteBatch.dispose();
		shapeRenderer.dispose();
		titleFont.dispose();
		font.dispose();
	}
}
