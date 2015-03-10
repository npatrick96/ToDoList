package application;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
	@FXML
	private Button addButton;
	@FXML
	private Button upButton;
	@FXML
	private Button downButton;
	@FXML
	private Button deleteButton;
	@FXML
	private Button markAsDoneButton;
	@FXML
	private Button searchButton;
	@FXML
	private Button saveButton;
	@FXML
	private TextField searchTextField;
	@FXML
	private TextField addTaskTextField;
	@FXML
	private TextArea taskDetailsTextArea;
	@FXML
	private ListView<String> taskList;
	private Model model = new Model();
	
	@FXML
	private void initialize() throws IOException{
		if (model.fileExists()){
			model.openFile();
		}
		taskList.setItems(model.getObservable());
		
	}
	@FXML		
	private void add(){
		model.add(addTaskTextField.getText());
		addTaskTextField.setText("");
	}
	@FXML
	private void addDetails(){
		model.addDetails(getSelectedIndex(), taskDetailsTextArea.getText());
	}
	@FXML
	private void save() throws IOException{
		model.saveToFile();
	}
	@FXML
	private void displayDetails(){
		if (! model.isLegal(getSelectedIndex())){taskDetailsTextArea.setText("");}
		String selectedTask = taskList.getItems().get(getSelectedIndex());
		taskDetailsTextArea.setText(model.getDetails(selectedTask));
	}
	private int getSelectedIndex(){
		return taskList.getSelectionModel().getSelectedIndex();
	}
	
	private void move(int offset){
		int target = model.move(getSelectedIndex(), offset);
		taskList.getSelectionModel().select(target);
	}
	
	@FXML
	public void delete(){
		model.remove(getSelectedIndex());
	}
	
	@FXML
	public void search(){
		int target =  model.search(searchTextField.getText(), getSelectedIndex());
		taskList.getSelectionModel().select(target);
		if (target>=0){
			taskList.getSelectionModel().select(target);
			displayDetails();
			}
		}
	
	@FXML
	public void markAsDone() {
		model.markAsDone(getSelectedIndex());
	}
	
	@FXML
	private void up(){
		move(-1);	
	}
	@FXML
	private void down(){
		move(1);	
	}
}
