package com.neet.managers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.badlogic.gdx.Gdx;

public class Save
{
	public static GameData gameData;
	
	public static void save()
	{
		try
		{
			ObjectOutputStream out = new ObjectOutputStream(
				new FileOutputStream("highscores.sav")
			);
			out.writeObject( gameData );
			out.close();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			Gdx.app.exit();
		}
	}
	
	public static void load()
	{

		try
		{

			if ( !saveFileExists() )
			{
				init();
				return;
			}

			ObjectInputStream in = new ObjectInputStream(
				new FileInputStream("highscores.sav")
			);
			gameData = (GameData) in.readObject();
			in.close();
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			Gdx.app.exit();
		}
	}
	
	public static boolean saveFileExists()
	{
		File file = new File( "highscores.sav" );
		return file.exists();
	}
	
	public static void init()
	{
		gameData = new GameData();
		gameData.init();
		save();
	}
}
