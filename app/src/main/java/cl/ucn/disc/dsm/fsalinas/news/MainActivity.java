        /*
 * Copyright 2020 Felipe Salinas-Urra, piipebysonic@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package cl.ucn.disc.dsm.fsalinas.news;

        import android.os.Bundle;
        import android.view.Menu;
        import android.view.MenuItem;

        import androidx.appcompat.app.AppCompatActivity;
        import androidx.appcompat.app.AppCompatDelegate;

        /**
         *  The Main Class.
         * @author Felipe Salinas-Urra.
         */

public class MainActivity extends AppCompatActivity {

            /**
             * Creates the night mode menu option.
             *
             * @param menu The menu in the action bar
             * @return True to display the menu, false to hide it
             */
            @Override
            public boolean onCreateOptionsMenu(Menu menu) {
                getMenuInflater().inflate(R.menu.main_menu, menu);
                // Change the label of the menu based on the state of the app.
                int nightMode = AppCompatDelegate.getDefaultNightMode();
                if(nightMode == AppCompatDelegate.MODE_NIGHT_YES){
                    menu.findItem(R.id.night_mode).setTitle(R.string.day_mode);
                } else{
                    menu.findItem(R.id.night_mode).setTitle(R.string.night_mode);
                }
                return true;
            }

            /**
             * Handles options menu item clicks.
             *
             * @param item The item that was pressed
             * @return returns true since the item click wa handled
             */
            @Override
            public boolean onOptionsItemSelected(MenuItem item) {
                // Check if the correct item was clicked.
                if (item.getItemId() == R.id.night_mode) {
                    // Get the night mode state of the app.
                    int nightMode = AppCompatDelegate.getDefaultNightMode();
                    // Set the theme mode for the restarted activity.
                    if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                        AppCompatDelegate.setDefaultNightMode
                                (AppCompatDelegate.MODE_NIGHT_NO);
                    } else {
                        AppCompatDelegate.setDefaultNightMode
                                (AppCompatDelegate.MODE_NIGHT_YES);
                    }
                    // Recreate the activity for the theme change to take effect.
                    recreate();
                }
                return true;
            }

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
            }
}