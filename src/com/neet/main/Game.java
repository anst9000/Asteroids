package com.neet.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.neet.managers.GameInputProcessor;
//import com.neet.managers.GameKeys;
//import com.neet.managers.GameStateManager;
//import com.neet.managers.Jukebox;

public class Game implements ApplicationListener {
	
	public static int WIDTH;
	public static int HEIGHT;
	
	public static OrthographicCamera cam;
	
//	private GameStateManager gsm;
	
	@Override
	public void create() {
		
//		WIDTH = Gdx.graphics.getWidth();
//		HEIGHT = Gdx.graphics.getHeight();
//		
//		cam = new OrthographicCamera(WIDTH, HEIGHT);
//		cam.translate(WIDTH / 2, HEIGHT / 2);
//		cam.update();
//		
//		Gdx.input.setInputProcessor(
//			new GameInputProcessor()
//		);
//		
//		Jukebox.load("sounds/explode.ogg", "explode");
//		Jukebox.load("sounds/extralife.ogg", "extralife");
//		Jukebox.load("sounds/largesaucer.ogg", "largesaucer");
//		Jukebox.load("sounds/pulsehigh.ogg", "pulsehigh");
//		Jukebox.load("sounds/pulselow.ogg", "pulselow");
//		Jukebox.load("sounds/saucershoot.ogg", "saucershoot");
//		Jukebox.load("sounds/shoot.ogg", "shoot");
//		Jukebox.load("sounds/smallsaucer.ogg", "smallsaucer");
//		Jukebox.load("sounds/thruster.ogg", "thruster");
//		
//		gsm = new GameStateManager();
		
	}
	
	@Override
	public void render() {
		
//		// clear screen to black
//		Gdx.gl.glClearColor(0, 0, 0, 1);
//		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//		
//		gsm.update(Gdx.graphics.getDeltaTime());
//		gsm.draw();
//		
//		GameKeys.update();
		
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
