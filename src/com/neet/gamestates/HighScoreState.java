package com.neet.gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.neet.main.Game;
import com.neet.managers.GameKeys;
import com.neet.managers.GameStateManager;
import com.neet.managers.Save;

public class HighScoreState extends GameState
{
	private SpriteBatch spriteBatch;
	
	private BitmapFont font;
	
	private long[] highScores;
	private String[] names;
	
	public HighScoreState( GameStateManager gsm )
	{
		super(gsm);
	}
	
	@Override
	public void init()
	{
		spriteBatch = new SpriteBatch();
		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(
			Gdx.files.internal("fonts/Hyperspace Bold.ttf")
		);

		font = gen.generateFont(20);
		
		Save.load();
		highScores = Save.gameData.getHighScores();
		names = Save.gameData.getNames();
	}
	
	@Override
	public void update( float dt )
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
		
		text = "High Scores";
		textWidth = font.getBounds( text ).width;
		font.draw( spriteBatch, text, ( Game.WIDTH - textWidth ) / 2, 300 );
		
		for ( int hs = 0; hs < highScores.length; hs++ )
		{
			text = String.format( "%2d. %7s %s", hs + 1, highScores[hs], names[hs] );

			textWidth = font.getBounds( text ).width;
			font.draw( spriteBatch, text, ( Game.WIDTH - textWidth ) / 2, 270 - 20 * hs );
		}
		
		spriteBatch.end();
	}
	
	@Override
	public void handleInput()
	{

		if ( GameKeys.isPressed( GameKeys.ENTER ) || GameKeys.isPressed( GameKeys.ESCAPE ) )
		{
			gsm.setState(GameStateManager.MENU);
		}
	}
	
	@Override
	public void dispose()
	{
		spriteBatch.dispose();
		font.dispose();
	}
	
}








