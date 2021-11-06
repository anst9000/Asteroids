package com.neet.managers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class GameInputProcessor extends InputAdapter
{
	@Override
	public boolean keyDown( int key )
	{

		switch ( key )
		{
		case Keys.UP:
			GameKeys.setKey( GameKeys.UP, true );
			break;
		case Keys.LEFT:
			GameKeys.setKey( GameKeys.LEFT, true );
			break;
		case Keys.DOWN:
			GameKeys.setKey( GameKeys.DOWN, true );
			break;
		case Keys.RIGHT:
			GameKeys.setKey( GameKeys.RIGHT, true );
			break;
		case Keys.ENTER:
			GameKeys.setKey( GameKeys.ENTER, true );
			break;
		case Keys.ESCAPE:
			GameKeys.setKey( GameKeys.ESCAPE, true );
			break;
		case Keys.SPACE:
			GameKeys.setKey( GameKeys.SPACE, true );
			break;
		case Keys.SHIFT_LEFT:
		case Keys.SHIFT_RIGHT:
			GameKeys.setKey( GameKeys.SHIFT, true );
			break;
		default:
			break;
		}

		return true;
	}

	@Override
	public boolean keyUp( int key )
	{

		switch ( key )
		{
		case Keys.UP:
			GameKeys.setKey( GameKeys.UP, false );
			break;
		case Keys.LEFT:
			GameKeys.setKey( GameKeys.LEFT, false );
			break;
		case Keys.DOWN:
			GameKeys.setKey( GameKeys.DOWN, false );
			break;
		case Keys.RIGHT:
			GameKeys.setKey( GameKeys.RIGHT, false );
			break;
		case Keys.ENTER:
			GameKeys.setKey( GameKeys.ENTER, false );
			break;
		case Keys.ESCAPE:
			GameKeys.setKey( GameKeys.ESCAPE, false );
			break;
		case Keys.SPACE:
			GameKeys.setKey( GameKeys.SPACE, false );
			break;
		case Keys.SHIFT_LEFT:
		case Keys.SHIFT_RIGHT:
			GameKeys.setKey( GameKeys.SHIFT, false );
			break;
		default:
			break;
		}

		return true;
	}
}
