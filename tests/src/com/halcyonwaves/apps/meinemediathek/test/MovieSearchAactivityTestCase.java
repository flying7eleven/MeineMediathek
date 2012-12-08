package com.halcyonwaves.apps.meinemediathek.test;

import com.halcyonwaves.apps.meinemediathek.activities.HomeActivity;
import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

public class MovieSearchAactivityTestCase extends ActivityInstrumentationTestCase2< HomeActivity > {

	private Solo solo = null;
	private EditText searchEdit = null;

	public MovieSearchAactivityTestCase() {
		super( HomeActivity.class );
	}

	@Override
	protected void setUp() throws Exception {
		this.solo = new Solo( this.getInstrumentation(), this.getActivity() );
		this.searchEdit = (EditText) this.getActivity().findViewById( com.halcyonwaves.apps.meinemediathek.R.id.et_searchfortitle );
	}

	@Override
	protected void tearDown() throws Exception {
		this.solo.finishOpenedActivities();
	}

	public void testSearchForTooShortKeyword() {
		// be sure that we found the search control
		assertNotNull( this.searchEdit );
		
		// try to search for a too short word
		this.solo.enterText( this.searchEdit, "at" );
		this.solo.clickOnButton( 0 );
		
		// wait for a dialog which pops up
		this.getInstrumentation().waitForIdleSync();
		this.solo.takeScreenshot( "MovieSearchAactivityTestCase_testSearchForTooShortKeyword" );
		
		// click on the okay button to close the dialog again
		this.solo.clickOnButton( this.solo.getString( android.R.string.ok ) );
	}
	
	public void testSearchWithNoresults() {
		// be sure that we found the search control
		assertNotNull( this.searchEdit );
		
		// try to search for a too short word
		this.solo.enterText( this.searchEdit, "wadde hadde duuude daaahaha wadde hadde duuuuuhe daha" );
		this.solo.clickOnButton( 0 );
		
		// wait for a dialog which pops up
		this.getInstrumentation().waitForIdleSync();
		this.solo.takeScreenshot( "MovieSearchAactivityTestCase_testSearchWithNoresults" );
		
		// click on the okay button to close the dialog again
		this.solo.clickOnButton( this.solo.getString( android.R.string.ok ) );
		this.getInstrumentation().waitForIdleSync();
		
	}
	
	public void testDoNormalSearch() {
		// be sure that we found the search control
		assertNotNull( this.searchEdit );
		
		// try to search for a too short word
		this.solo.enterText( this.searchEdit, "wiso vorschau" );
		this.solo.clickOnButton( 0 );
		
		// wait for a dialog which pops up
		this.getInstrumentation().waitForIdleSync();
		this.solo.takeScreenshot( "MovieSearchAactivityTestCase_testDoNormalSearch" );
		
		//
	}
}
