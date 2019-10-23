package fvarrui.path.calculator;

import java.net.URL;
import java.nio.file.Path;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;

public class PathCalcController implements Initializable {
	private ObjectProperty<Path> sourcePath = new SimpleObjectProperty<>(this, "sourcePath");
	private ObjectProperty<Path> targetPath = new SimpleObjectProperty<>(this, "targetPath");
	private ReadOnlyObjectWrapper<Path> calculatedPath = new ReadOnlyObjectWrapper<>(this, "calculatedPath");

    @FXML
    private TreeView<String> sourceTree;

    @FXML
    private TextField sourceText;

    @FXML
    private TreeView<String> targetTree;

    @FXML
    private TextField targetText;

    @FXML
    private TextField pathText;
    
	@Override
	public void initialize(URL resource, ResourceBundle bundle) {
		
		sourceTree.setRoot(new ComputerTreeItem(false));
		sourceTree.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
			if (nv instanceof FileTreeItem) {
				FileTreeItem fti = (FileTreeItem) nv;
				sourcePath.set(fti.getFile().toPath());
			} else {
				sourcePath.set(null);
			}
		});
		
		targetTree.setRoot(new ComputerTreeItem(true));
		targetTree.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> {
			if (nv instanceof FileTreeItem) {
				FileTreeItem fti = (FileTreeItem) nv;
				targetPath.set(fti.getFile().toPath());
			} else {
				targetPath.set(null);
			}
		});
		
		sourceText.textProperty().bind(Bindings.when(sourcePath.isNull()).then("").otherwise(sourcePath.asString()));
		targetText.textProperty().bind(Bindings.when(targetPath.isNull()).then("").otherwise(targetPath.asString()));
		pathText.textProperty().bind(Bindings.when(calculatedPath.isNull()).then(".").otherwise(calculatedPath.asString()));
		
		calculatedPath.bind(Bindings.createObjectBinding(() -> {
				if (sourcePath.get() != null && targetPath.get() != null) {
					return sourcePath.get().relativize(targetPath.get()).normalize();
				}
				return null;
			}, sourcePath, targetPath));
		
	}

}
