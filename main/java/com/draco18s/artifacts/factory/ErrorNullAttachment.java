package com.draco18s.artifacts.factory;

public class ErrorNullAttachment extends Exception
{
	public ErrorNullAttachment ()
	{
	}

	public ErrorNullAttachment (String message)
	{
		super (message);
	}

	public ErrorNullAttachment (Throwable cause)
	{
		super (cause);
	}

	public ErrorNullAttachment (String message, Throwable cause)
	{
		super (message, cause);
	}
}