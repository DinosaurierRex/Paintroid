/*    Catroid: An on-device graphical programming language for Android devices
 *    Copyright (C) 2010  Catroid development team
 *    (<http://code.google.com/p/catroid/wiki/Credits>)
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package at.tugraz.ist.paintroid.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Environment;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import at.tugraz.ist.paintroid.MainActivity;
import at.tugraz.ist.paintroid.dialog.DialogColorPicker;
import at.tugraz.ist.paintroid.graphic.DrawingSurface.Mode;

import com.jayway.android.robotium.solo.Solo;

public class ImportPngTests extends ActivityInstrumentationTestCase2<MainActivity> {

  private Solo solo;
  private MainActivity mainActivity;
  private int screenWidth;
  private int screenHeight;

  // Buttonindexes
  final int COLORPICKER = 0;
  final int STROKE = 0;
  final int HAND = 1;
  final int MAGNIFIY = 2;
  final int BRUSH = 3;
  final int EYEDROPPER = 4;
  final int WAND = 5;
  final int UNDO = 6;
  final int REDO = 7;
  final int FILE = 8;
  
  final int STROKERECT = 0;
  final int STROKECIRLCE = 1;
  final int STROKE1 = 2;
  final int STROKE2 = 3;
  final int STROKE3 = 4;
  final int STROKE4 = 5;

  public ImportPngTests() {
    super("at.tugraz.ist.paintroid", MainActivity.class);
  }

  public void setUp() throws Exception {
    solo = new Solo(getInstrumentation(), getActivity());
    String languageToLoad_before  = "en";
    Locale locale_before = new Locale(languageToLoad_before);
    Locale.setDefault(locale_before);
    
    Configuration config_before = new Configuration();
    config_before.locale = locale_before;
    
    mainActivity = (MainActivity) solo.getCurrentActivity();
    mainActivity.getBaseContext().getResources().updateConfiguration(config_before, mainActivity.getBaseContext().getResources().getDisplayMetrics());
    
    screenWidth = solo.getCurrentActivity().getWindowManager()
      .getDefaultDisplay().getWidth();
    screenHeight = solo.getCurrentActivity().getWindowManager()
      .getDefaultDisplay().getHeight();
  }

  /**
   * Check if the import works even if other modes like cursor, middlepoint, etc. are activated.
   * 
   */
  public void testImportOnDifferentModes() throws Exception {
    //save picture to import
    solo.clickOnImageButton(FILE);
    solo.clickOnButton("New Drawing");
    assertTrue(solo.waitForActivity("MainActivity", 500));
    
    File file = new File(Environment.getExternalStorageDirectory().toString() + "/Paintroid/import_png_test_1_save.png");
    
    solo.clickOnImageButton(FILE);
    solo.clickOnButton("Save");
    solo.enterText(0, "import_png_test_1_save");
    solo.clickOnButton("Done");
    
    // Override
    if(file.exists()){
      solo.clickOnButton("Yes");
    }
    Thread.sleep(500);
    
    assertTrue(file.exists());
    
    mainActivity.setFloatingBoxPng(Environment.getExternalStorageDirectory().toString() + "/Paintroid/import_png_test_1_save.png");
    Thread.sleep(400);
    assertEquals(Mode.FLOATINGBOX, mainActivity.getMode());
    
    solo.clickOnMenuItem("Stamp");
    Thread.sleep(500);
    assertEquals(Mode.DRAW, mainActivity.getMode());
    
    solo.clickOnImageButton(BRUSH);
    solo.clickOnScreen(screenWidth/2, screenHeight/2);
    
    solo.clickOnScreen(screenWidth/2, screenHeight/2);
    solo.drag(screenWidth/2, screenWidth/2+1, screenHeight/2, screenHeight/2, 50);
    Thread.sleep(400);
    assertEquals(Mode.CURSOR, mainActivity.getMode());
    
    mainActivity.setFloatingBoxPng(Environment.getExternalStorageDirectory().toString() + "/Paintroid/import_png_test_1_save.png");
    Thread.sleep(400);
    assertEquals(Mode.FLOATINGBOX, mainActivity.getMode());
    
    solo.clickOnMenuItem("Stamp");
    Thread.sleep(200);
    assertEquals(Mode.DRAW, mainActivity.getMode());
    
    solo.clickOnMenuItem("Define Middle Point");
    Thread.sleep(200);
    assertEquals(Mode.MIDDLEPOINT, mainActivity.getMode());
    
    mainActivity.setFloatingBoxPng(Environment.getExternalStorageDirectory().toString() + "/Paintroid/import_png_test_1_save.png");
    Thread.sleep(400);
    assertEquals(Mode.FLOATINGBOX, mainActivity.getMode());
    
    solo.clickOnMenuItem("Stamp");
    Thread.sleep(200);
    assertEquals(Mode.DRAW, mainActivity.getMode());
    
    solo.clickOnMenuItem("Stamp");
    Thread.sleep(200);
    assertEquals(Mode.FLOATINGBOX, mainActivity.getMode());
    
    mainActivity.setFloatingBoxPng(Environment.getExternalStorageDirectory().toString() + "/Paintroid/import_png_test_1_save.png");
    Thread.sleep(400);
    assertEquals(Mode.FLOATINGBOX, mainActivity.getMode());
    
    solo.clickOnMenuItem("Stamp");
    Thread.sleep(200);
    assertEquals(Mode.DRAW, mainActivity.getMode());
    
    file.delete();
  }
  
  /**
   * Check if the floating box stamps the correct picture
   * 
   */
  public void testImport() throws Exception {
    solo.clickOnImageButton(FILE);
    solo.clickOnButton("New Drawing");
    assertTrue(solo.waitForActivity("MainActivity", 500));
    
    //Choosing color red
    solo.clickOnButton(COLORPICKER);
    solo.waitForView(DialogColorPicker.ColorPickerView.class, 1, 200);
    ArrayList<View> actual_views = solo.getViews();
    View colorPickerView = null;
    for (View view : actual_views) {
      if(view instanceof DialogColorPicker.ColorPickerView)
      {
        colorPickerView = view;
      }
    }
    assertNotNull(colorPickerView);
    int[] colorPickerViewCoordinates = new int[2];
    colorPickerView.getLocationOnScreen(colorPickerViewCoordinates);
    solo.clickOnScreen(colorPickerViewCoordinates[0]+10, colorPickerViewCoordinates[1]+18);
    Thread.sleep(500);
    solo.clickOnScreen(colorPickerViewCoordinates[0]+265, colorPickerViewCoordinates[1]+50);
    solo.clickOnScreen(colorPickerViewCoordinates[0]+20, colorPickerViewCoordinates[1]+340);
    assertEquals(String.valueOf(Color.RED), mainActivity.getCurrentSelectedColor());
    Thread.sleep(500);
    
    solo.clickOnImageButton(WAND);
    solo.clickOnScreen(screenWidth/2, screenWidth/2);
    
    File file = new File(Environment.getExternalStorageDirectory().toString() + "/Paintroid/import_png_test_1_save.png");
    
    solo.clickOnImageButton(FILE);
    solo.clickOnButton("Save");
    solo.enterText(0, "import_png_test_1_save");
    solo.clickOnButton("Done");
    
    // Override
    if(file.exists()){
      solo.clickOnButton("Yes");
    }
    Thread.sleep(500);
    
    assertTrue(file.exists());
    
    solo.clickOnImageButton(FILE);
    solo.clickOnButton("New Drawing");
    assertTrue(solo.waitForActivity("MainActivity", 500));
    
    solo.clickOnImageButton(BRUSH);
    
    solo.clickOnScreen(screenWidth/2, screenHeight/2);
    
    mainActivity.setFloatingBoxPng(Environment.getExternalStorageDirectory().toString() + "/Paintroid/import_png_test_1_save.png");
    Thread.sleep(400);
    assertEquals(Mode.FLOATINGBOX, mainActivity.getMode());
    
    Point boxSize = mainActivity.getFloatingBoxSize();
    assertNotNull(boxSize);
    Point boxCoordinates = new Point(mainActivity.getFloatingBoxCoordinates());
    assertNotNull(boxCoordinates);
    
    Thread.sleep(500);
    
    solo.clickOnScreen(screenWidth/2, screenHeight/2);
    
    Thread.sleep(500);
    
    solo.clickOnMenuItem("Stamp");
    Thread.sleep(200);
    assertEquals(Mode.DRAW, mainActivity.getMode());
    
    Point boxPixelCoordinates = mainActivity.getPixelCoordinates(boxCoordinates.x, boxCoordinates.y);
    Point boxPixelSize = mainActivity.getPixelCoordinates(boxSize.x, boxSize.y);
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x, boxPixelCoordinates.y));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x+boxPixelSize.x/2-5, boxPixelCoordinates.y));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x-boxPixelSize.x/2+5, boxPixelCoordinates.y));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x, boxPixelCoordinates.y+boxPixelSize.y/2-5));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x, boxPixelCoordinates.y-boxPixelSize.y/2+5));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x+boxPixelSize.x/2-5, boxPixelCoordinates.y+boxPixelSize.y/2-5));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x-boxPixelSize.x/2+5, boxPixelCoordinates.y-boxPixelSize.y/2+5));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x-boxPixelSize.x/2+5, boxPixelCoordinates.y+boxPixelSize.y/2-5));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x+boxPixelSize.x/2-5, boxPixelCoordinates.y-boxPixelSize.y/2+5));
    
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x+boxPixelSize.x/2+5, boxPixelCoordinates.y));
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x-boxPixelSize.x/2-5, boxPixelCoordinates.y));
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x, boxPixelCoordinates.y+boxPixelSize.y/2+10));
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x, boxPixelCoordinates.y-boxPixelSize.y/2-10));
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x+boxPixelSize.x/2+5, boxPixelCoordinates.y+boxPixelSize.y/2+10));
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x-boxPixelSize.x/2-5, boxPixelCoordinates.y-boxPixelSize.y/2-10));
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x-boxPixelSize.x/2-5, boxPixelCoordinates.y+boxPixelSize.y/2+10));
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x+boxPixelSize.x/2+5, boxPixelCoordinates.y-boxPixelSize.y/2-10));
    
    file.delete();
  }
  
  /**
   * Check if the floating box stamps the correct picture
   * after loading two different pictures in a row
   * 
   */
  public void testDoubleImport() throws Exception {
    solo.clickOnImageButton(FILE);
    solo.clickOnButton("New Drawing");
    assertTrue(solo.waitForActivity("MainActivity", 500));
    
    File file1 = new File(Environment.getExternalStorageDirectory().toString() + "/Paintroid/import_png_test_1_save.png");
    
    solo.clickOnImageButton(FILE);
    solo.clickOnButton("Save");
    solo.enterText(0, "import_png_test_1_save");
    solo.clickOnButton("Done");
    
    // Override
    if(file1.exists()){
      solo.clickOnButton("Yes");
    }
    Thread.sleep(500);
    
    assertTrue(file1.exists());
    
    solo.clickOnImageButton(FILE);
    solo.clickOnButton("New Drawing");
    assertTrue(solo.waitForActivity("MainActivity", 500));
    
    //Choosing color red
    solo.clickOnButton(COLORPICKER);
    solo.waitForView(DialogColorPicker.ColorPickerView.class, 1, 200);
    ArrayList<View> actual_views = solo.getViews();
    View colorPickerView = null;
    for (View view : actual_views) {
      if(view instanceof DialogColorPicker.ColorPickerView)
      {
        colorPickerView = view;
      }
    }
    assertNotNull(colorPickerView);
    int[] colorPickerViewCoordinates = new int[2];
    colorPickerView.getLocationOnScreen(colorPickerViewCoordinates);
    solo.clickOnScreen(colorPickerViewCoordinates[0]+10, colorPickerViewCoordinates[1]+18);
    Thread.sleep(500);
    solo.clickOnScreen(colorPickerViewCoordinates[0]+265, colorPickerViewCoordinates[1]+50);
    solo.clickOnScreen(colorPickerViewCoordinates[0]+20, colorPickerViewCoordinates[1]+340);
    assertEquals(String.valueOf(Color.RED), mainActivity.getCurrentSelectedColor());
    Thread.sleep(500);
    
    solo.clickOnImageButton(WAND);
    solo.clickOnScreen(screenWidth/2, screenWidth/2);
    
    File file2 = new File(Environment.getExternalStorageDirectory().toString() + "/Paintroid/import_png_test_2_save.png");
    
    solo.clickOnImageButton(FILE);
    solo.clickOnButton("Save");
    solo.enterText(0, "import_png_test_2_save");
    solo.clickOnButton("Done");
    
    // Override
    if(file2.exists()){
      solo.clickOnButton("Yes");
    }
    Thread.sleep(500);
    
    assertTrue(file2.exists());
    
    solo.clickOnImageButton(FILE);
    solo.clickOnButton("New Drawing");
    assertTrue(solo.waitForActivity("MainActivity", 500));
    
    solo.clickOnImageButton(BRUSH);
    
    solo.clickOnScreen(screenWidth/2, screenHeight/2);
    
    mainActivity.setFloatingBoxPng(Environment.getExternalStorageDirectory().toString() + "/Paintroid/import_png_test_1_save.png");
    assertEquals(Mode.FLOATINGBOX, mainActivity.getMode());
    
    mainActivity.setFloatingBoxPng(Environment.getExternalStorageDirectory().toString() + "/Paintroid/import_png_test_2_save.png");
    assertEquals(Mode.FLOATINGBOX, mainActivity.getMode());
    
    Point boxSize = mainActivity.getFloatingBoxSize();
    assertNotNull(boxSize);
    Point boxCoordinates = new Point(mainActivity.getFloatingBoxCoordinates());
    assertNotNull(boxCoordinates);
    
    Thread.sleep(500);
    
    solo.clickLongOnScreen(screenWidth/2, screenHeight/2);
    
    Thread.sleep(500);
    
    solo.clickOnMenuItem("Stamp");
    Thread.sleep(200);
    assertEquals(Mode.DRAW, mainActivity.getMode());
    
    Point boxPixelCoordinates = mainActivity.getPixelCoordinates(boxCoordinates.x, boxCoordinates.y);
    Point boxPixelSize = mainActivity.getPixelCoordinates(boxSize.x, boxSize.y);
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x, boxPixelCoordinates.y));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x+boxPixelSize.x/2-5, boxPixelCoordinates.y));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x-boxPixelSize.x/2+5, boxPixelCoordinates.y));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x, boxPixelCoordinates.y+boxPixelSize.y/2-5));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x, boxPixelCoordinates.y-boxPixelSize.y/2+5));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x+boxPixelSize.x/2-5, boxPixelCoordinates.y+boxPixelSize.y/2-5));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x-boxPixelSize.x/2+5, boxPixelCoordinates.y-boxPixelSize.y/2+5));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x-boxPixelSize.x/2+5, boxPixelCoordinates.y+boxPixelSize.y/2-5));
    assertEquals(Color.RED, mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x+boxPixelSize.x/2-5, boxPixelCoordinates.y-boxPixelSize.y/2+5));
    
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x+boxPixelSize.x/2+5, boxPixelCoordinates.y));
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x-boxPixelSize.x/2-5, boxPixelCoordinates.y));
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x, boxPixelCoordinates.y+boxPixelSize.y/2+10));
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x, boxPixelCoordinates.y-boxPixelSize.y/2-10));
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x+boxPixelSize.x/2+5, boxPixelCoordinates.y+boxPixelSize.y/2+10));
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x-boxPixelSize.x/2-5, boxPixelCoordinates.y-boxPixelSize.y/2-10));
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x-boxPixelSize.x/2-5, boxPixelCoordinates.y+boxPixelSize.y/2+10));
    assertTrue(Color.RED != mainActivity.getCurrentImage().getPixel(boxPixelCoordinates.x+boxPixelSize.x/2+5, boxPixelCoordinates.y-boxPixelSize.y/2-10));
    
    file1.delete();
    file2.delete();
  }

  @Override
  public void tearDown() throws Exception {
    solo.clickOnMenuItem("More");
    solo.clickInList(0);
//    solo.clickOnMenuItem("Quit");
    try {
      solo.finalize();
    } catch (Throwable e) {
      e.printStackTrace();
    }
    getActivity().finish();
    super.tearDown();
  }

}