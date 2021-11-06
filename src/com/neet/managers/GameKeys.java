package com.neet.managers;

public class GameKeys
{
	private static boolean[] keys;
	private static boolean[] prevKeys;
	private static final int NUM_KEYS = 8;

	// --------------------------------------
	public static final int UP = 0;
	public static final int LEFT = 1;
	public static final int DOWN = 2;
	public static final int RIGHT = 3;
	public static final int ENTER = 4;
	public static final int ESCAPE = 5;
	public static final int SPACE = 6;
	public static final int SHIFT = 7;
	// --------------------------------------

	static
	{
		keys = new boolean[NUM_KEYS];
		prevKeys = new boolean[NUM_KEYS];
	}

	public static void update()
	{

		for ( int key = 0; key < NUM_KEYS; key++ )
		{
			prevKeys[key] = keys[key];
		}
	}

	public static void setKey( int key, boolean state )
	{
		keys[key] = state;
	}

	public static boolean isDown( int key )
	{
		return keys[key];
	}

	public static boolean isPressed( int key )
	{
		return keys[key] && !prevKeys[key];
	}
}
