/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.util.HashMap;

/**
 *
 * @author roei.avraham
 */
public class QuitPlayerMap {
            HashMap<String, QuitPlayer> QuitPlayerMap;

        public QuitPlayerMap(HashMap<String, QuitPlayer> QuitPlayerMap) {
            this.QuitPlayerMap = QuitPlayerMap;
        }
        
        public QuitPlayer getQuitPlayer(String gameName)
        {
            return this.QuitPlayerMap.get(gameName);
        }
        
        public void putQuitPlayer(String gameName, QuitPlayer qp)
        {
            this.QuitPlayerMap.put(gameName, qp);
        }
}
