package ide.impl;

import view.View;

public class FileLoader extends FileIO {

	private static FileLoader instance;
	
	public static FileLoader instance(View view) {
		if(instance == null)
			instance = new FileLoader(view);
		return instance;
	}

	private FileLoader(View view){
		super(view.getComponent());
	}


}
