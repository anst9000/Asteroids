package com.neet.managers;

import com.neet.gamestates.GameState;
import com.neet.gamestates.HighScoreState;
import com.neet.gamestates.MenuState;
import com.neet.gamestates.PlayState;

public class GameStateManager
{
	// Current game state
	private GameState gameState;

	public static final int MENU = 0;
	public static final int PLAY = 1;
	public static final int HIGHSCORE = 2;
	public static final int GAMEOVER = 3;

	public GameStateManager()
	{
		setState( MENU );
	}

	public void setState( int state )
	{

		if ( gameState != null )
		{
			gameState.dispose();
		}

		switch ( state )
		{
			case MENU:
				// Switch to menu state
				gameState = new MenuState( this );
				break;
			case PLAY:
				// Switch to play state
				gameState = new PlayState( this );
				break;
			case HIGHSCORE:
				// Switch to play state
				gameState = new HighScoreState( this );
				break;
		}
	}

	public void update( float dt )
	{
		gameState.update( dt );
	}

	public void draw()
	{
		gameState.draw();
	}
}
