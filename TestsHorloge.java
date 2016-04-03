package tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import source.Horloge;

public class TestsHorloge {
	
	private Horloge horloge;
	
	@Before
	public void init()
	{
		horloge= new Horloge(0);
	}

	@Test
	public void testRemiseZero() {
		horloge.RemiseZero();
		assertEquals(0, horloge.getCycleEcoules());
		assertEquals(false, horloge.isSuspendu());	
	}
	
	@Test
	public void testSetPaused()
	{
		horloge.setPaused(false);
		assertEquals(false, horloge.isSuspendu());
		
		horloge.setPaused(true);
		assertEquals(true, horloge.isSuspendu());
	}
	
	@Test
	public void testCycleEstEcoule()
	{
		horloge.RemiseZero();
		assertEquals(false,horloge.cycleEstEcoule() );
	}
	
	
	
	

}
