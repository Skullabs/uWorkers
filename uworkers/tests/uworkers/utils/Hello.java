package uworkers.utils;

import java.io.Serializable;

import lombok.Value;

@Value
public class Hello implements Serializable {

	private static final long serialVersionUID = -7785227681110925365L;
	final String world;
}