/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utilities;

import java.util.HashMap;

/**
 *
 * @author Anat
 */
   public class TurnInfoMap {

        HashMap<String, TurnInfo> turnInfoMap;

        public TurnInfoMap(HashMap<String, TurnInfo> turnInfoMap) {
            this.turnInfoMap = turnInfoMap;
        }
        
        public TurnInfo getTurnInfo(String gameName)
        {
            return turnInfoMap.get(gameName);
        }
        
        public void putTurnInfo(String gameName, TurnInfo ti)
        {
            turnInfoMap.put(gameName, ti);
        }
    }
