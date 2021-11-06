package com.neet.gamestates;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.neet.entities.Player;
import com.neet.managers.GameKeys;
import com.neet.managers.GameStateManager;

public class PlayState extends GameState
{
	private ShapeRenderer shapeRenderer;
	private Player player;

	public PlayState( GameStateManager gsm )
	{
		super( gsm );
	}

	@Override
	public void init()
	{
		shapeRenderer = new ShapeRenderer();
		player = new Player();
	}

	@Override
	public void update( float dt )
	{
		handleInput();
		player.update( dt );
	}

	@Override
	public void draw()
	{
		player.draw( shapeRenderer );
	}

	@Override
	public void handleInput()
	{
		player.setLeft( GameKeys.isDown( GameKeys.LEFT ) );
		player.setRight( GameKeys.isDown( GameKeys.RIGHT ) );
		player.setUp( GameKeys.isDown( GameKeys.UP ) );
	}

	@Override
	public void dispose()
	{

	}
}
