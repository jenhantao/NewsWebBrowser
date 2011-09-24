/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Engine;

import GUI.NewsWebBrowserMainWindow;

/**
 *
 * @author Bo
 */
public class MainWindowRunnable implements Runnable{
    private NewsWebBrowserMainWindow m_window;

    public MainWindowRunnable(NewsWebBrowserMainWindow window){
        this.m_window = window;
    }

    @Override
    public void run() {
        this.m_window.setVisible(true);
    }
}
