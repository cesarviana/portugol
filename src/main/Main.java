package main;

import ide.Ide;

public class Main {
	public static void main(String[] args) {
		Ide ide = Ide.instance();
		ide.start();
	}
}
