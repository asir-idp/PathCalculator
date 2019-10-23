package fvarrui.path.calculator;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ComputerTreeItem extends TreeItem<String> {

	private static final Image COMPUTER_IMAGE = new Image("/images/computer.png");

	private boolean includeFiles;
	
	public ComputerTreeItem(boolean includeFiles) {
		super();

		this.includeFiles = includeFiles;
		
		String hostName = "Mi PC";
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		}		
		setValue(hostName);
		setExpanded(true);		
		setGraphic(new ImageView(COMPUTER_IMAGE));
		populate();
	}
	
	public ComputerTreeItem() {
		this(true);
	}

	private void populate() {
		Iterable<Path> rootDirectories = FileSystems.getDefault().getRootDirectories();
		for (Path path : rootDirectories) {
			FileTreeItem treeNode = new FileTreeItem(path.toFile(), true, includeFiles);
			getChildren().add(treeNode);
			treeNode.populate();
		}
	}

}
