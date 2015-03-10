package application;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class ModelTest {
	Model model =  new Model();
	final int LARGE_NUMBER = 1000000;
	final String LOREM_IPSUM_TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.";
	final String LOREM_IPSUM_TEXT2 = "Fusce hendrerit nunc placerat dui porta mollis.";

	@Test
	public void testAdd() {
		for (int i = 0; i<LARGE_NUMBER; ++i){
			model.add(Integer.toBinaryString(i));
			if (Math.random() < 0.5){
				model.add("");
			}
		}
	assertEquals(LARGE_NUMBER, model.size());	
	}
	
	@Test
	public void testRemove(){
		model.remove(0);
		model.add("a");
		model.add("b");
		model.remove(0);
		assertEquals(1, model.size());
		assertEquals(0, model.getIndex("b"));
	}
	@Test 
	public void testMarkAsDone(){
		model.markAsDone(0);
		model.add(LOREM_IPSUM_TEXT);
		int originalSize=LOREM_IPSUM_TEXT.length();
		model.markAsDone(0);
		int expectedFinalSize = originalSize + model.MARK_AS_DONE.length();
		int actualFinalSize = model.get(0).length();
		assertEquals(expectedFinalSize, actualFinalSize);
		
	}
	
	@Test
	public void testSearch() throws IOException{
		assertEquals(-1, model.search(LOREM_IPSUM_TEXT, 0));
		model.add(LOREM_IPSUM_TEXT);
		model.add(LOREM_IPSUM_TEXT2);
		model.saveToFile();
		assertEquals(model.getObservable().indexOf(LOREM_IPSUM_TEXT), model.search(LOREM_IPSUM_TEXT, 0));
	}
	
	
	@Test
	public void testMove() {
		model.add("a");
		model.add("b");
		model.move(0,  1);
		testMovePassed();
	}
	
	void testMovePassed(){
		assertEquals("b", model.get(0));
		assertEquals("a", model.get(1));
	}
	@Test
	public void testMoveBoundary(){
		model.move(0, 1);
		testMove();
		model.move(0, -1);
		testMovePassed();
		model.move(1, 1);
		testMovePassed();
	}
	@Test
	public void testIsLegal(){
		assertFalse(model.isLegal(0));
		assertFalse(model.isLegal(-1));
		for (int i = 0; i < LARGE_NUMBER; ++i){
			model.add(Integer.toString(i));
			assertTrue(model.isLegal(i));
			assertFalse(model.isLegal(i+1));
		}
	}
	@Test
	public void fileSaved() throws IOException{
		model.add(LOREM_IPSUM_TEXT);
		model.saveToFile();
		assertEquals(true, model.fileExists());
	}
	public void fileOpened() throws IOException{
		model.add(LOREM_IPSUM_TEXT);
		model.saveToFile();
		model.openFile();
		assertEquals(1, model.allTasksDetails.size());
	}
	
	@Test(expected = IndexOutOfBoundsException.class) 
	public void testGet(){                             
		model.get(0);                                 
	}

}
