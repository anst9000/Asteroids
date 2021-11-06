package com.neet.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.neet.managers.GameInputProcessor;
//import com.neet.managers.GameKeys;
//import com.neet.managers.GameStateManager;
//import com.neet.managers.Jukebox;

public class Game implements ApplicationListener {
	
	public static int WIDTH;
	public static int HEIGHT;
	
	public static OrthographicCamera cam;
	
	
	@Override
	public void create() {
		
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
	}
	
	@Override
	public void render() {
		// clear screen to black
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
	}
	
	@Override
	public void resize(int width, int height) {}
	@Override
	public void pause() {}
	@Override
	public void resume() {}
	@Override
	public void dispose() {}
	
}
