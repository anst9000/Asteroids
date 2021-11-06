package com.neet.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.neet.managers.GameInputProcessor;
//import com.neet.managers.GameInputProcessor;
//import com.neet.managers.GameKeys;
//import com.neet.managers.GameStateManager;
//import com.neet.managers.Jukebox;
import com.neet.managers.GameKeys;

public class Game implements ApplicationListener
{
	public static int WIDTH;
	public static int HEIGHT;
	public static OrthographicCamera cam;

	@Override
	public void create()
	{
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();

		cam = new OrthographicCamera( WIDTH, HEIGHT );
		cam.translate( WIDTH / 2, HEIGHT / 2 );
		cam.update();

		Gdx.input.setInputProcessor( new GameInputProcessor() );
	}

	@Override
	public void render()
	{
		// clear screen to black
		Gdx.gl.glClearColor( 0, 0, 0, 1 );
		Gdx.gl.glClear( GL10.GL_COLOR_BUFFER_BIT );

		GameKeys.update();
	}

	@Override
	public void resize( int width, int height )
	{
	}

	@Override
	public void pause()
	{
	}

	@Override
	public void resume()
	{
	}

	@Override
	public void dispose()
	{
	}
}
