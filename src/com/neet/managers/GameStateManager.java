package com.neet.managers;

import com.neet.gamestates.GameState;
import com.neet.gamestates.PlayState;

public class GameStateManager
{
	// Current game state
	private GameState gameState;

	public static final int MENU = 0;
	public static final int PLAY = 1;

	public GameStateManager()
	{
		setState( PLAY );
	}

	public void setState( int state )
	{

		if ( gameState != null )
		{
			gameState.dispose();
		}

		if ( state == MENU )
		{
			// Switch to menu state
			// gameState = new MenuState(this);
		}

		if ( state == PLAY )
		{
			// Switch to play state
			gameState = new PlayState( this );
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
