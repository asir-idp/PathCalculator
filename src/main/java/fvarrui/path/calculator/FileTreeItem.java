package fvarrui.path.calculator;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.DosFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.SystemUtils;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class FileTreeItem extends TreeItem<String> {

	private static final Image FOLDER_COLLAPSE_IMAGE = new Image("/images/folder.png");
	private static final Image FOLDER_EXPAND_IMAGE = new Image("/images/folder-open.png");
	private static final Image FILE_IMAGE = new Image("/images/text-x-generic.png");
	private static final Image DRIVE_IMAGE = new Image("/images/drive-harddisk.png");

	private File file;
	private Image collapseImage, expandImage;
	private boolean includeFiles;

	public FileTreeItem(File file, boolean root, boolean includeFiles) {
		super();
		
		this.includeFiles = includeFiles;

		if (file == null) throw new IllegalArgumentException("Debe especificar un fichero o un directorio.");
		
		if (root) {
			collapseImage = DRIVE_IMAGE;
			expandImage = DRIVE_IMAGE;
		} else if (file.isDirectory()) {
			collapseImage = FOLDER_COLLAPSE_IMAGE;
			expandImage = FOLDER_EXPAND_IMAGE;
		} else {
			collapseImage = FILE_IMAGE;
			expandImage = FILE_IMAGE;
		}
		
		this.setGraphic(new ImageView(collapseImage));

		this.file = file;
		this.setValue(file.getName().isEmpty() ? file.getPath() : file.getName());
		this.expandedProperty().addListener((o, ov, nv) -> {
			if (nv) {
				onItemExpanded();
			} else {
				onItemCollapsed();
			}
		});

	}
	
	public FileTreeItem(Path path) {
		this(new File(path.toUri()), true, true);
	}

	public FileTreeItem(File file) {
		this(file, false, true);
	}
	
	private void onItemCollapsed() {
		if (isDirectory() && !isExpanded()) {
			ImageView iv = (ImageView) getGraphic();
			iv.setImage(collapseImage);
		}
		clear();
	}

	private void onItemExpanded() {
		if (isDirectory() && isExpanded()) {
			ImageView iv = (ImageView) getGraphic();
			iv.setImage(expandImage);
		}
		populate();
	}

	public void clear() {	
		for (TreeItem<?> child : getChildren()) {
			((FileTreeItem) child).clear();
		}
	}

	private void prepopulate() {
		if (isDirectory() && !isDirEmpty(file.toPath())) {
			getChildren().add(new FileTreeItem(new File("."), false, includeFiles));
		}
	}
	
	private boolean isDirEmpty(Path directory) {
	    try(DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
	        return !dirStream.iterator().hasNext();
	    } catch (IOException e) {
			// TODO no hace nada
		}
	    return true;
	}
	
	public void populate() {
		
		try {
			
			if (isDirectory()) {
				
				getChildren().clear();
				
				File[] children = file.listFiles();
				if (children != null) {
					
					List<File> filesList =new ArrayList<>(Arrays.asList(children));
					
					// ordena la lista de ficheros (pone primero los directorios y los ordena por nombre)  
					filesList = filesList.stream().sorted((f1, f2) -> {
						if (f1.isDirectory() && f2.isDirectory() || f1.isFile() && f2.isFile()) {
							return f1.getName().compareToIgnoreCase(f2.getName());
						} else if (f1.isDirectory())
							return -1;
						return 1; 
					}).collect(Collectors.toList());
					
					for (File child : filesList) {
	
						// en Windows descartamos los archivos del sistema
						if (!isSystem(child) && includeFiles || (!includeFiles && !child.isFile())) {
							FileTreeItem treeNode = new FileTreeItem(child, false, includeFiles);
							getChildren().add(treeNode);
							if (this.isExpanded()) treeNode.prepopulate();
						}
						
					}
				}
				
			}
			
		} catch (IOException ex) {
			// TODO no hace nada
		}

	}

	public boolean isSystem(File child) throws IOException {
		boolean isSystem = false;
		if (SystemUtils.IS_OS_WINDOWS) {
			Path path = child.toPath();
			DosFileAttributes dosAttribs = Files.readAttributes(path, DosFileAttributes.class);
			isSystem = dosAttribs.isSystem();
		}
		return isSystem;
	}

	public File getFile() {
		return file;
	}

	public boolean isDirectory() {
		return file.isDirectory();
	}

}
