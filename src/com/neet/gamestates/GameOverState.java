package com.neet.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.neet.main.Game;
import com.neet.managers.GameKeys;
import com.neet.managers.GameStateManager;
import com.neet.managers.Save;

public class GameOverState extends GameState
{
	private SpriteBatch spriteBatch;
	private ShapeRenderer shapeRenderer;
	
	private boolean newHighScore;
	private char[] newName;
	private int currentChar;
	
	private BitmapFont gameOverFont;
	private BitmapFont font;
	
	public GameOverState( GameStateManager gsm )
	{
		super(gsm);
	}
	
	@Override
	public void init()
	{
		spriteBatch = new SpriteBatch();
		shapeRenderer = new ShapeRenderer();
		
		newHighScore = Save.gameData.isHighScore( Save.gameData.getTentativeScore() );

		if ( newHighScore )
		{
			newName = new char[] {'A', 'A', 'A'};
			currentChar = 0;
		}
		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
			Gdx.files.internal("fonts/Hyperspace Bold.ttf")
		);
		gameOverFont = gen.generateFont(32);
		font = gen.generateFont(20);
	}
	
	@Override
	public void update( float deltaTime )
	{
		handleInput();
	}
	
	@Override
	public void draw()
	{
		spriteBatch.setProjectionMatrix( Game.cam.combined );
		spriteBatch.begin();
		
		String text;
		float textWidth;
		
		text = "Game Over";
		textWidth = gameOverFont.getBounds( text ).width;
		gameOverFont.draw( spriteBatch, text, ( Game.WIDTH - textWidth ) / 2, 220 );
		
		if ( !newHighScore )
		{
			spriteBatch.end();
			return;
		}
		
		text = "New High Score: " + Save.gameData.getTentativeScore();
		textWidth = font.getBounds( text ).width;
		font.draw( spriteBatch, text, ( Game.WIDTH - textWidth ) / 2, 180 );
		
		for ( int i = 0; i < newName.length; i++ )
		{
			font.draw( spriteBatch, Character.toString( newName[i] ), 230 + 14 * i, 120 );
		}
		
		spriteBatch.end();
		
		shapeRenderer.begin( ShapeType.Line );
		shapeRenderer.line( 230 + 14 * currentChar, 100, 244 + 14 * currentChar, 100 );
		shapeRenderer.end();
	}
	
	@Override
	public void handleInput()
	{

		if ( GameKeys.isPressed( GameKeys.ENTER ) )
		{

			if ( newHighScore )
			{
				Save.gameData.addHighScore( Save.gameData.getTentativeScore(),
					new String(newName)
				);
				Save.save();
			}
			gsm.setState(GameStateManager.MENU);
		}
		
		if ( GameKeys.isPressed( GameKeys.UP ) )
		{

			if ( newName[currentChar] == ' ' )
			{
				newName[currentChar] = 'Z';
			}
			else
			{
				newName[currentChar]--;

				if ( newName[currentChar] < 'A' )
				{
					newName[currentChar] = ' ';
				}
			}
		}
		
		if ( GameKeys.isPressed( GameKeys.DOWN ) )
		{

			if ( newName[currentChar] == ' ' )
			{
				newName[currentChar] = 'A';
			}
			else
			{
				newName[currentChar]++;

				if ( newName[currentChar] > 'Z' )
				{
					newName[currentChar] = ' ';
				}
			}
		}
		
		if ( GameKeys.isPressed( GameKeys.RIGHT ) )
		{

			if ( currentChar < newName.length - 1 )
			{
				currentChar++;
			}
		}
		
		if ( GameKeys.isPressed( GameKeys.LEFT ) )
		{

			if ( currentChar > 0 )
			{
				currentChar--;
			}
		}
	}
	
	@Override
	public void dispose()
	{
		spriteBatch.dispose();
		shapeRenderer.dispose();
		gameOverFont.dispose();
		font.dispose();
	}
}
