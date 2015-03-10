package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



public class Model {
	private ObservableList<String> words =  FXCollections.observableArrayList();
	ObservableList<String> getObservable() {return words;}
	File tasksFile = new File("tasks.txt");
	HashMap<String, String> allTasksDetails = new HashMap<String, String>();
	final String WHERE_TO_SPLIT = "!SPLIT_HERE!";
	final String SPLIT_HERE = "!SPLIT_HERE!,";
	final String MARK_AS_DONE = "!DONE! - "; 
	
	
	boolean fileExists(){
		return ((tasksFile.exists() && !tasksFile.isDirectory()));
	}
	
	public void openFile() throws IOException{
		if (fileExists()){ 
			BufferedReader reader = new BufferedReader(new FileReader(tasksFile));
			String line;
			String fileText="";
			while ((line = reader.readLine()) != null) {
				   fileText  = fileText + line;
				}
			reader.close();
	        int lenSavedText = fileText.length() - WHERE_TO_SPLIT.length();
	        String savedText = fileText.substring(1,  lenSavedText-1); 
	        String[] allTasksArray = savedText.split(SPLIT_HERE);
	        for (int i = 0; i<allTasksArray.length; ++i){
	        	String taskDetails;
	        	String currentTaskAndDetails = allTasksArray[i].trim();	      
		        String[] currentTaskAndDetailsArray = currentTaskAndDetails.split("=");
		        String taskText = currentTaskAndDetailsArray[0].trim();
		        if( currentTaskAndDetailsArray.length == 1){
		        	taskDetails = "";
		        }else{taskDetails = currentTaskAndDetailsArray[1].trim();}
		        words.add(taskText);
		        allTasksDetails.put(taskText, taskDetails);	         
	       	}
		}
	} 
	
	
	private void prepareToSave(){
		for (int i = 0; i<words.size(); ++i){
			String task = words.get(i);
			if (!allTasksDetails.containsKey(task)) {
				allTasksDetails.put(task,WHERE_TO_SPLIT);			
			}
			String taskDetails = allTasksDetails.get(task);
			if (!taskDetails.contains(WHERE_TO_SPLIT)){
				allTasksDetails.put(task, taskDetails+WHERE_TO_SPLIT);
			}
		}
	}
	
	public boolean isLegal(int index){
		return index >= 0 && index< words.size();
	}
	public void add(String task) {
		if (task.length() > 0){
			words.add(task);
		}	
	}
	public void addDetails(int currentIndex, String details) {
		if (isLegal(currentIndex)){
			if (!details.contains(WHERE_TO_SPLIT)){
				allTasksDetails.put(words.get(currentIndex), details+WHERE_TO_SPLIT);
			}else{allTasksDetails.put(words.get(currentIndex), details);}
		}
	}
	private boolean hasDetails(String key){
		return allTasksDetails.containsKey(key);
	}
	
	public String getDetails(String keyString) {
		String taskDetails;
		if (allTasksDetails.containsKey(keyString)){
		taskDetails = allTasksDetails.get(keyString);
		}else{taskDetails = "";}
		if (taskDetails.contains(WHERE_TO_SPLIT)){
			return taskDetails.substring(0,taskDetails.length() - WHERE_TO_SPLIT.length());
		}else{return taskDetails;}	
	}
	
	
	public int move(int start, int offset){
		int end = start + offset;
		if (isLegal(start) && isLegal(end)){
			String temp = words.get(start);
			words.set(start, words.get(end));
			words.set(end, temp);
			return end;
		}
		return start;
	}
	private boolean isDone(int i){
		String selected = words.get(i);
		return (selected.contains(MARK_AS_DONE));
	}
	
	public void markAsDone(int i){
		String selectedTaskDetails;
		if (isLegal(i) && !isDone(i)){
			String selectedTaskTitle = words.get(i);
			if (allTasksDetails.containsKey(selectedTaskTitle)){
				selectedTaskDetails = allTasksDetails.get(selectedTaskTitle);
			}else{selectedTaskDetails = "";}
			String newSelectedTaskTitle = MARK_AS_DONE+selectedTaskTitle;
			words.set(i, newSelectedTaskTitle);
			allTasksDetails.remove(selectedTaskTitle, selectedTaskDetails);
			addDetails(i, selectedTaskDetails);
		}
	}
	
	public void saveToFile() throws IOException{
		prepareToSave();
		FileWriter writer = new FileWriter(tasksFile);
		writer.write("");
		writer.write(allTasksDetails.toString());
		writer.close();
	}
	
	
	
	public void remove(int i){
		if (isLegal(i)){
			String temp = words.get(i);
			words.remove(i);
			allTasksDetails.remove(temp);
		}
	}
	
	
	
	private int searchBetweenIndexes(String value, int startIndex, int endIndex){
		for (int i = startIndex; i<endIndex;++i){
			String currentTaskDetails;
			String currentTaskTitle = words.get(i);
			if (hasDetails(currentTaskTitle)){
			currentTaskDetails = allTasksDetails.get(currentTaskTitle).toLowerCase();
			}else{currentTaskDetails = "";}
			if ((currentTaskTitle.contains(value) || currentTaskDetails.contains(value))){
				return i;
			}
		}     
		return -1;
	}
	public int search(String target, int currentIndex){
		String lowerCaseTarget = target.toLowerCase();
		if (!isLegal(currentIndex)){
			 return searchBetweenIndexes(lowerCaseTarget, 0, words.size());
		}
		
		else if ((lowerCaseTarget.length() > 0) && isLegal(currentIndex)) {
			int valueIndex;
			valueIndex =   searchBetweenIndexes(lowerCaseTarget, currentIndex+1, words.size());
			if (valueIndex  == -1){
				if (searchBetweenIndexes(lowerCaseTarget, 0, currentIndex)==-1){
					return currentIndex;
					}else{return searchBetweenIndexes(lowerCaseTarget, 0, currentIndex+1);}
			}else{return valueIndex;}
		}
		return currentIndex;
	}
	
	
	public int size(){
		return words.size();
	}
	public String get(int i){
		return words.get(i);
	}
	public int getIndex(String value){
		return words.indexOf(value);
	}
	

}
